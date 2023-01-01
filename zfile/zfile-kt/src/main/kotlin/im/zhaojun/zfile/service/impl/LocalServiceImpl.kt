package im.zhaojun.zfile.service.impl

import cn.hutool.core.lang.Console
import im.zhaojun.zfile.exception.InitializeDriveException
import im.zhaojun.zfile.exception.NotExistFileException
import im.zhaojun.zfile.model.constant.StorageConfigConstant
import im.zhaojun.zfile.model.constant.SystemConfigConstant
import im.zhaojun.zfile.model.constant.ZFileConstant
import im.zhaojun.zfile.model.dto.FileItemDTO
import im.zhaojun.zfile.model.entity.StorageConfig
import im.zhaojun.zfile.model.enums.FileTypeEnum
import im.zhaojun.zfile.model.enums.StorageTypeEnum
import im.zhaojun.zfile.repository.SystemConfigRepository
import im.zhaojun.zfile.service.StorageConfigService
import im.zhaojun.zfile.service.base.AbstractBaseFileService
import im.zhaojun.zfile.service.base.BaseFileService
import im.zhaojun.zfile.util.StringUtils.concatUrl
import im.zhaojun.zfile.util.StringUtils.removeDuplicateSeparator
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.nio.file.Files
import java.util.*
import javax.annotation.Resource

/**
 * @author zhaojun
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class LocalServiceImpl : AbstractBaseFileService(), BaseFileService {
    @Resource
    private val storageConfigService: StorageConfigService? = null

    @Resource
    private val systemConfigRepository: SystemConfigRepository? = null
    var filePath: String? = null
    override fun init(driveId: String?) {
        this.driveId = driveId
        val stringStorageConfigMap = storageConfigService!!.selectStorageConfigMapByDriveId(driveId)
        mergeStrategyConfig(stringStorageConfigMap)
        filePath = stringStorageConfigMap[StorageConfigConstant.FILE_PATH_KEY]!!.value
        if (Objects.isNull(filePath)) {
            log.debug("初始化存储策略 [{}] 失败: 参数不完整", storageTypeEnum.description)
            isInitialized = false
            return
        }
        val file = File(filePath)
        isInitialized = if (!file.exists()) {
            throw InitializeDriveException("文件路径: \"" + file.absolutePath + "\"不存在, 请检查是否填写正确.")
        } else {
            testConnection()
            true
        }
    }

    @Throws(IOException::class)
    override fun fileList(path: String): List<FileItemDTO> {
        val fileItemList: MutableList<FileItemDTO> = ArrayList()
        val fullPath = removeDuplicateSeparator(filePath + path)
        val file = File(fullPath)
        Console.log("获取绝对卢林静路径: {}", file.absolutePath)
        Console.log(file.absolutePath)
        if (!file.exists()) {
            throw FileNotFoundException("文件不存在")
        }
        val files = file.listFiles() ?: return fileItemList
        for (f in files) {
            val fileItemDTO = FileItemDTO()
            fileItemDTO.type = if (f.isDirectory) FileTypeEnum.FOLDER else FileTypeEnum.FILE
            fileItemDTO.time = Date(f.lastModified())
            fileItemDTO.size = f.length()
            fileItemDTO.name = f.name
            fileItemDTO.path = path
            fileItemDTO.mimetype = Files.probeContentType(f.toPath())
            if (f.isFile) {
                fileItemDTO.src = getDownloadUrl(concatUrl(path, f.name))
            }
            fileItemList.add(fileItemDTO)
        }
        return fileItemList
    }

    override fun getDownloadUrl(path: String?): String? {
        val usernameConfig = systemConfigRepository!!.findByKey(SystemConfigConstant.DOMAIN)
        return removeDuplicateSeparator(usernameConfig.value + "/file/" + driveId + ZFileConstant.PATH_SEPARATOR + path)
    }

    override val storageTypeEnum: StorageTypeEnum
        get() = StorageTypeEnum.LOCAL

    override fun getFileItem(path: String): FileItemDTO? {
        val fullPath = filePath + path
        Console.log("localServiceImple")
        try {
            Console.log(URLDecoder.decode(fullPath, "utf-8"))
            val file = File(URLDecoder.decode(fullPath, "utf-8"))
            if (!file.exists()) {
                throw NotExistFileException()
            }
            val fileItemDTO = FileItemDTO()
            fileItemDTO.type = if (file.isDirectory) FileTypeEnum.FOLDER else FileTypeEnum.FILE
            fileItemDTO.time = Date(file.lastModified())
            fileItemDTO.size = file.length()
            fileItemDTO.name = file.name
            fileItemDTO.path = filePath
            if (file.isFile) {
                fileItemDTO.src = getDownloadUrl(path)
            }
            return fileItemDTO
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return null
    }

    override fun storageStrategyConfigList(): List<StorageConfig> {
        return object : ArrayList<StorageConfig?>() {
            init {
                add(StorageConfig("filePath", "文件路径"))
            }
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(LocalServiceImpl::class.java)
    }
}