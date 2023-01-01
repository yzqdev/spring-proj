package im.zhaojun.zfile.service.base

import cn.hutool.core.util.URLUtil
import cn.hutool.http.HttpUtil
import com.alibaba.fastjson.JSONObject
import im.zhaojun.zfile.model.constant.StorageConfigConstant
import im.zhaojun.zfile.model.constant.ZFileConstant
import im.zhaojun.zfile.model.dto.FileItemDTO
import im.zhaojun.zfile.model.enums.FileTypeEnum
import im.zhaojun.zfile.model.support.OneDriveToken
import im.zhaojun.zfile.repository.StorageConfigRepository
import im.zhaojun.zfile.service.StorageConfigService
import im.zhaojun.zfile.util.StringUtils.getFullPath
import im.zhaojun.zfile.util.StringUtils.isNotNullOrEmpty
import im.zhaojun.zfile.util.StringUtils.removeFirstSeparator
import im.zhaojun.zfile.util.StringUtils.removeLastSeparator
import im.zhaojun.zfile.util.StringUtils.replaceHost
import lombok.extern.slf4j.Slf4j
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.util.*
import javax.annotation.Resource

@Slf4j
abstract class MicrosoftDriveServiceBase : AbstractBaseFileService() {
    protected var proxyDomain: String? = null

    @Resource
    @Lazy
    private val oneDriveRestTemplate: RestTemplate? = null

    @Resource
    private val storageConfigRepository: StorageConfigRepository? = null

    @Resource
    private val storageConfigService: StorageConfigService? = null

    /**
     * 根据 RefreshToken 刷新 AccessToken, 返回刷新后的 Token.
     *
     * @return  刷新后的 Token
     */
    val refreshToken: OneDriveToken
        get() {
            val refreshStorageConfig =
                storageConfigRepository!!.findByDriveIdAndKey(driveId, StorageConfigConstant.REFRESH_TOKEN_KEY)
            val param = "client_id=" + clientId +
                    "&redirect_uri=" + redirectUri +
                    "&client_secret=" + clientSecret +
                    "&refresh_token=" + refreshStorageConfig.value +
                    "&grant_type=refresh_token"
            val fullAuthenticateUrl = AUTHENTICATE_URL.replace("{authenticateEndPoint}", authenticateEndPoint)
            val post = HttpUtil.createPost(fullAuthenticateUrl)
            post.body(param, "application/x-www-form-urlencoded")
            val response = post.execute()
            return JSONObject.parseObject(response.body(), OneDriveToken::class.java)
        }

    /**
     * OAuth2 协议中, 根据 code 换取 access_token 和 refresh_token.
     *
     * @param   code
     * 代码
     *
     * @return  获取的 Token 信息.
     */
    fun getToken(code: String): OneDriveToken {
        val param = "client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&client_secret=" + clientSecret +
                "&code=" + code +
                "&scope=" + scope +
                "&grant_type=authorization_code"
        val fullAuthenticateUrl = AUTHENTICATE_URL.replace("{authenticateEndPoint}", authenticateEndPoint)
        val post = HttpUtil.createPost(fullAuthenticateUrl)
        post.body(param, "application/x-www-form-urlencoded")
        val response = post.execute()
        return JSONObject.parseObject(response.body(), OneDriveToken::class.java)
    }

    override fun fileList(path: String): List<FileItemDTO> {
        var path: String? = path
        path = removeFirstSeparator(path!!)
        var fullPath = getFullPath(basePath!!, path)
        val result: MutableList<FileItemDTO> = ArrayList()
        var nextLink: String? = null
        do {
            var requestUrl: String?
            if (nextLink != null) {
                nextLink = nextLink.replace("+", "%2B")
                requestUrl = URLUtil.decode(nextLink)
            } else if (ZFileConstant.PATH_SEPARATOR.equals(fullPath, ignoreCase = true) || "".equals(
                    fullPath,
                    ignoreCase = true
                )
            ) {
                requestUrl = DRIVER_ROOT_URL
            } else {
                requestUrl = DRIVER_ITEMS_URL
            }
            fullPath = removeLastSeparator(fullPath!!)
            var root: JSONObject?
            val headers = HttpHeaders()
            headers["driveId"] = driveId.toString()
            val entity = HttpEntity<Any>(headers)
            root = try {
                oneDriveRestTemplate!!.exchange(
                    requestUrl,
                    HttpMethod.GET,
                    entity,
                    JSONObject::class.java,
                    graphEndPoint,
                    type,
                    fullPath
                ).body
            } catch (e: HttpClientErrorException) {
                MicrosoftDriveServiceBase.log.debug(
                    "调用 OneDrive 时出现了网络异常, 响应信息: {}, 已尝试重新刷新 token 后再试.",
                    e.responseBodyAsString
                )
                refreshOneDriveToken()
                oneDriveRestTemplate!!.exchange(
                    requestUrl,
                    HttpMethod.GET,
                    entity,
                    JSONObject::class.java,
                    graphEndPoint,
                    type,
                    fullPath
                ).body
            }
            if (root == null) {
                return emptyList()
            }
            nextLink = root.getString("@odata.nextLink")
            val fileList = root.getJSONArray("value")
            for (i in fileList.indices) {
                val fileItemDTO = FileItemDTO()
                val fileItem = fileList.getJSONObject(i)
                fileItemDTO.name = fileItem.getString("name")
                fileItemDTO.size = fileItem.getLong("size")
                fileItemDTO.time = fileItem.getDate("lastModifiedDateTime")
                if (fileItem.containsKey("file")) {
                    var originUrl = fileItem.getString("@microsoft.graph.downloadUrl")
                    if (isNotNullOrEmpty(proxyDomain)) {
                        originUrl = replaceHost(originUrl, proxyDomain)
                    }
                    fileItemDTO.src = originUrl
                    fileItemDTO.type = FileTypeEnum.FILE
                } else {
                    fileItemDTO.type = FileTypeEnum.FOLDER
                }
                fileItemDTO.path = path
                result.add(fileItemDTO)
            }
        } while (nextLink != null)
        return result
    }

    override fun getFileItem(path: String): FileItemDTO? {
        val fullPath = getFullPath(basePath!!, path)
        val headers = HttpHeaders()
        headers["driveId"] = driveId.toString()
        val entity = HttpEntity<Any>(headers)
        val fileItem: JSONObject?
        fileItem = try {
            oneDriveRestTemplate!!.exchange(
                DRIVER_ITEM_URL,
                HttpMethod.GET,
                entity,
                JSONObject::class.java,
                graphEndPoint,
                type,
                fullPath
            ).body
        } catch (e: HttpClientErrorException) {
            MicrosoftDriveServiceBase.log.debug(
                "调用 OneDrive 时出现了网络异常, 响应信息: {}, 已尝试重新刷新 token 后再试.",
                e.responseBodyAsString
            )
            refreshOneDriveToken()
            oneDriveRestTemplate!!.exchange(
                DRIVER_ITEM_URL,
                HttpMethod.GET,
                entity,
                JSONObject::class.java,
                graphEndPoint,
                type,
                fullPath
            ).body
        }
        if (fileItem == null) {
            return null
        }
        val fileItemDTO = FileItemDTO()
        fileItemDTO.name = fileItem.getString("name")
        fileItemDTO.size = fileItem.getLong("size")
        fileItemDTO.time = fileItem.getDate("lastModifiedDateTime")
        if (fileItem.containsKey(ONE_DRIVE_FILE_FLAG)) {
            var originUrl = fileItem.getString("@microsoft.graph.downloadUrl")
            if (isNotNullOrEmpty(proxyDomain)) {
                originUrl = replaceHost(originUrl, proxyDomain)
            }
            fileItemDTO.src = originUrl
            fileItemDTO.type = FileTypeEnum.FILE
        } else {
            fileItemDTO.type = FileTypeEnum.FOLDER
        }
        fileItemDTO.path = path
        return fileItemDTO
    }

    /**
     * 获取存储类型, 对于 OneDrive 或 SharePoint, 此地址会不同.
     * @return          Graph 连接点
     */
    abstract val type: String

    /**
     * 获取 GraphEndPoint, 对于不同版本的 OneDrive, 此地址会不同.
     * @return          Graph 连接点
     */
    abstract val graphEndPoint: String

    /**
     * 获取 AuthenticateEndPoint, 对于不同版本的 OneDrive, 此地址会不同.
     * @return          Authenticate 连接点
     */
    abstract val authenticateEndPoint: String

    /**
     * 获取 Client ID.
     * @return  Client Id
     */
    abstract val clientId: String?

    /**
     * 获取重定向地址.
     * @return  重定向地址
     */
    abstract val redirectUri: String?

    /**
     * 获取 Client Secret 密钥.
     * @return  Client Secret 密钥.
     */
    abstract val clientSecret: String?

    /**
     * 获取 API Scope.
     * @return  Scope
     */
    abstract val scope: String?

    /**
     * 刷新当前驱动器 AccessToken
     */
    fun refreshOneDriveToken() {
        val refreshToken = refreshToken
        if (refreshToken.accessToken == null || refreshToken.refreshToken == null) {
            return
        }
        val accessTokenConfig =
            storageConfigService!!.findByDriveIdAndKey(driveId, StorageConfigConstant.ACCESS_TOKEN_KEY)
        val refreshTokenConfig =
            storageConfigService.findByDriveIdAndKey(driveId, StorageConfigConstant.REFRESH_TOKEN_KEY)
        accessTokenConfig.value = refreshToken.accessToken
        refreshTokenConfig.value = refreshToken.refreshToken
        storageConfigService.updateStorageConfig(Arrays.asList(accessTokenConfig, refreshTokenConfig))
    }

    companion object {
        /**
         * 获取根文件 API URI
         */
        protected const val DRIVER_ROOT_URL = "https://{graphEndPoint}/v1.0/{type}/drive/root/children"

        /**
         * 获取非根文件 API URI
         */
        protected const val DRIVER_ITEMS_URL = "https://{graphEndPoint}/v1.0/{type}/drive/root:{path}:/children"

        /**
         * 获取单文件 API URI
         */
        protected const val DRIVER_ITEM_URL = "https://{graphEndPoint}/v1.0/{type}/drive/root:{path}"

        /**
         * 根据 RefreshToken 获取 AccessToken API URI
         */
        protected const val AUTHENTICATE_URL = "https://{authenticateEndPoint}/common/oauth2/v2.0/token"

        /**
         * OneDrive 文件类型
         */
        private const val ONE_DRIVE_FILE_FLAG = "file"
    }
}