package im.zhaojun.zfile.service.impl

import im.zhaojun.zfile.model.constant.StorageConfigConstant
import im.zhaojun.zfile.model.enums.StorageTypeEnum
import im.zhaojun.zfile.service.StorageConfigService
import im.zhaojun.zfile.service.base.AbstractSharePointServiceBase
import im.zhaojun.zfile.service.base.BaseFileService
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import javax.annotation.Resource

/**
 * @author zhaojun
 */
@Service
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class SharePointServiceImpl : AbstractSharePointServiceBase(), BaseFileService {
    @Resource
    private val storageConfigService: StorageConfigService? = null

    @Value("\${zfile.onedrive.clientId}")
    protected var clientId: String? = null

    @Value("\${zfile.onedrive.redirectUri}")
    protected var redirectUri: String? = null

    @Value("\${zfile.onedrive.clientSecret}")
    protected var clientSecret: String? = null

    @Value("\${zfile.onedrive.scope}")
    protected var scope: String? = null
    override fun init(driveId: String?) {
        this.driveId = driveId
        val stringStorageConfigMap = storageConfigService!!.selectStorageConfigMapByDriveId(driveId)
        mergeStrategyConfig(stringStorageConfigMap)
        val accessToken = stringStorageConfigMap[StorageConfigConstant.ACCESS_TOKEN_KEY]!!.value
        val refreshToken = stringStorageConfigMap[StorageConfigConstant.REFRESH_TOKEN_KEY]!!.value
        super.siteId = stringStorageConfigMap[StorageConfigConstant.SHAREPOINT_SITE_ID]!!.value
        super.basePath = stringStorageConfigMap[StorageConfigConstant.BASE_PATH]!!.value
        val proxyDomainStorageConfig = stringStorageConfigMap[StorageConfigConstant.PROXY_DOMAIN]
        if (proxyDomainStorageConfig != null) {
            super.proxyDomain = proxyDomainStorageConfig.value
        }
        isInitialized =
            if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(
                    refreshToken
                )
            ) {
                SharePointServiceImpl.log.debug("初始化存储策略 [{}] 失败: 参数不完整", storageTypeEnum.description)
                false
            } else {
                refreshOneDriveToken()
                testConnection()
                true
            }
    }

    override fun getStorageTypeEnum(): StorageTypeEnum {
        return StorageTypeEnum.SHAREPOINT_DRIVE
    }

    override fun getGraphEndPoint(): String {
        return "graph.microsoft.com"
    }

    override fun getAuthenticateEndPoint(): String {
        return "login.microsoftonline.com"
    }

    override fun getClientId(): String? {
        return clientId
    }

    override fun getRedirectUri(): String? {
        return redirectUri
    }

    override fun getClientSecret(): String? {
        return clientSecret
    }

    override fun getScope(): String? {
        return scope
    }
}