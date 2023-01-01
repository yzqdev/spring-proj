package im.zhaojun.zfile.service.impl

import cn.hutool.core.util.ObjectUtil
import cn.hutool.core.util.URLUtil
import com.UpYun
import com.upyun.UpException
import im.zhaojun.zfile.exception.NotExistFileException
import im.zhaojun.zfile.model.constant.StorageConfigConstant
import im.zhaojun.zfile.model.constant.ZFileConstant
import im.zhaojun.zfile.model.dto.FileItemDTO
import im.zhaojun.zfile.model.entity.StorageConfig
import im.zhaojun.zfile.model.enums.FileTypeEnum
import im.zhaojun.zfile.model.enums.StorageTypeEnum
import im.zhaojun.zfile.service.StorageConfigService
import im.zhaojun.zfile.service.base.AbstractBaseFileService
import im.zhaojun.zfile.service.base.BaseFileService
import im.zhaojun.zfile.util.StringUtils.concatUrl
import im.zhaojun.zfile.util.StringUtils.removeDuplicateSeparator
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.io.IOException
import java.util.*
import javax.annotation.Resource

/**
 * @author zhaojun
 */
@Service
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
open class UpYunServiceImpl : AbstractBaseFileService(), BaseFileService {
    @Resource
    private val storageConfigService: StorageConfigService? = null
    private var domain: String? = null
    private var upYun: UpYun? = null
    private override val basePath: String? = null
    override fun init(driveId: String?) {
        this.driveId = driveId
        val stringStorageConfigMap = storageConfigService!!.selectStorageConfigMapByDriveId(driveId)
        mergeStrategyConfig(stringStorageConfigMap)
        val bucketName = stringStorageConfigMap[StorageConfigConstant.BUCKET_NAME_KEY]!!.value
        val username = stringStorageConfigMap[StorageConfigConstant.USERNAME_KEY]!!.value
        val password = stringStorageConfigMap[StorageConfigConstant.PASSWORD_KEY]!!.value
        domain = stringStorageConfigMap[StorageConfigConstant.DOMAIN_KEY]!!.value
        basePath = stringStorageConfigMap[StorageConfigConstant.BASE_PATH]!!.value
        basePath = ObjectUtil.defaultIfNull(basePath, "")
        if (Objects.isNull(bucketName) || Objects.isNull(username) || Objects.isNull(password)) {
            UpYunServiceImpl.log.debug("初始化存储策略 [{}] 失败: 参数不完整", storageTypeEnum.description)
            isInitialized = false
        } else {
            upYun = UpYun(bucketName, username, password)
            testConnection()
            isInitialized = true
        }
    }

    @Throws(Exception::class)
    override fun fileList(path: String): List<FileItemDTO> {
        val fileItemList = ArrayList<FileItemDTO>()
        var nextMark: String? = null
        do {
            val hashMap = HashMap<String, String?>(24)
            hashMap["x-list-iter"] = nextMark
            hashMap["x-list-limit"] = "100"
            val folderItemIter = upYun!!.readDirIter(URLUtil.encode(basePath + path), hashMap)
            nextMark = folderItemIter.iter
            val folderItems = folderItemIter.files
            if (folderItems != null) {
                for (folderItem in folderItems) {
                    val fileItemDTO = FileItemDTO()
                    fileItemDTO.name = folderItem.name
                    fileItemDTO.size = folderItem.size
                    fileItemDTO.time = folderItem.date
                    fileItemDTO.path = path
                    if ("folder" == folderItem.type) {
                        fileItemDTO.type = FileTypeEnum.FOLDER
                    } else {
                        fileItemDTO.type = FileTypeEnum.FILE
                        fileItemDTO.src = getDownloadUrl(concatUrl(basePath + path, fileItemDTO.name))
                    }
                    fileItemList.add(fileItemDTO)
                }
            }
        } while (END_MARK != nextMark)
        return fileItemList
    }

    override fun getDownloadUrl(path: String?): String? {
        return URLUtil.completeUrl(domain, path)
    }

    override val storageTypeEnum: StorageTypeEnum
        get() = StorageTypeEnum.UPYUN

    override fun getFileItem(path: String): FileItemDTO? {
        try {
            val lastDelimiterIndex = path.lastIndexOf("/")
            val name = path.substring(lastDelimiterIndex + 1)
            val fileInfo = upYun!!.getFileInfo(removeDuplicateSeparator(basePath + ZFileConstant.PATH_SEPARATOR + path))
                ?: throw NotExistFileException()
            val fileItemDTO = FileItemDTO()
            fileItemDTO.name = name
            fileItemDTO.size = java.lang.Long.valueOf(fileInfo["size"])
            fileItemDTO.time = Date(fileInfo["date"]!!.toLong() * 1000)
            fileItemDTO.path = path
            if ("folder" == fileInfo["type"]) {
                fileItemDTO.type = FileTypeEnum.FOLDER
            } else {
                fileItemDTO.type = FileTypeEnum.FILE
                fileItemDTO.src =
                    getDownloadUrl(removeDuplicateSeparator(basePath + ZFileConstant.PATH_SEPARATOR + path))
            }
            return fileItemDTO
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: UpException) {
            e.printStackTrace()
        }
        throw NotExistFileException()
    }

    override fun storageStrategyConfigList(): List<StorageConfig> {
        return object : ArrayList<StorageConfig?>() {
            init {
                add(StorageConfig("bucketName", "云存储服务名称"))
                add(StorageConfig("username", "操作员名称"))
                add(StorageConfig("password", "操作员密码"))
                add(StorageConfig("domain", "加速域名"))
                add(StorageConfig("basePath", "基路径"))
            }
        }
    }

    companion object {
        private const val END_MARK = "g2gCZAAEbmV4dGQAA2VvZg"
    }
}