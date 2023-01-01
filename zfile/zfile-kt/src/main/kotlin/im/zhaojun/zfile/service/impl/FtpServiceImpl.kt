package im.zhaojun.zfile.service.impl

import cn.hutool.core.util.URLUtil
import cn.hutool.extra.ftp.Ftp
import im.zhaojun.zfile.model.constant.StorageConfigConstant
import im.zhaojun.zfile.model.dto.FileItemDTO
import im.zhaojun.zfile.model.entity.StorageConfig
import im.zhaojun.zfile.model.enums.FileTypeEnum
import im.zhaojun.zfile.model.enums.StorageTypeEnum
import im.zhaojun.zfile.service.StorageConfigService
import im.zhaojun.zfile.service.base.AbstractBaseFileService
import im.zhaojun.zfile.service.base.BaseFileService
import im.zhaojun.zfile.util.StringUtils.concatUrl
import im.zhaojun.zfile.util.StringUtils.getFullPath
import im.zhaojun.zfile.util.StringUtils.isNullOrEmpty
import lombok.SneakyThrows
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPFile
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*
import javax.annotation.Resource

/**
 * @author zhaojun
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class FtpServiceImpl : AbstractBaseFileService(), BaseFileService {
    @Resource
    private val storageConfigService: StorageConfigService? = null
    private var ftp: Ftp? = null
    private var domain: String? = null
    private var host: String? = null
    private var port: String? = null
    private var username: String? = null
    private var password: String? = null
    @SneakyThrows(IOException::class)
    override fun init(driveId: String?) {
        this.driveId = driveId
        val stringStorageConfigMap = storageConfigService!!.selectStorageConfigMapByDriveId(driveId)
        mergeStrategyConfig(stringStorageConfigMap)
        host = stringStorageConfigMap[StorageConfigConstant.HOST_KEY]!!.value
        port = stringStorageConfigMap[StorageConfigConstant.PORT_KEY]!!.value
        username = stringStorageConfigMap[StorageConfigConstant.USERNAME_KEY]!!.value
        password = stringStorageConfigMap[StorageConfigConstant.PASSWORD_KEY]!!.value
        domain = stringStorageConfigMap[StorageConfigConstant.DOMAIN_KEY]!!.value
        super.basePath = stringStorageConfigMap[StorageConfigConstant.BASE_PATH]!!.value
        if (Objects.isNull(host) || Objects.isNull(port)) {
            isInitialized = false
        } else {
            ftp = Ftp(host, port.toInt(), username, password, StandardCharsets.UTF_8)
            ftp!!.client.type(FTP.BINARY_FILE_TYPE)
            testConnection()
            isInitialized = true
        }
    }

    override fun fileList(path: String): List<FileItemDTO> {
        ftp!!.reconnectIfTimeout()
        val fullPath = getFullPath(basePath!!, path)
        ftp!!.cd(fullPath)
        var ftpFiles = arrayOf<FTPFile>()
        try {
            ftp!!.client.changeWorkingDirectory("/")
            ftpFiles = ftp!!.client.listFiles(fullPath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val fileItemList: MutableList<FileItemDTO> = ArrayList()
        for (ftpFile in ftpFiles) {
            val fileItemDTO = FileItemDTO()
            fileItemDTO.name = ftpFile.name
            fileItemDTO.size = ftpFile.size
            fileItemDTO.time = ftpFile.timestamp.time
            fileItemDTO.type = if (ftpFile.isDirectory) FileTypeEnum.FOLDER else FileTypeEnum.FILE
            fileItemDTO.path = path
            if (ftpFile.isFile) {
                fileItemDTO.src = getDownloadUrl(concatUrl(path, fileItemDTO.name))
            }
            fileItemList.add(fileItemDTO)
        }
        return fileItemList
    }

    override fun getDownloadUrl(path: String?): String? {
        val fullPath = getFullPath(basePath!!, path!!)
        return if (isNullOrEmpty(domain)) {
            ("ftp://"
                    + URLUtil.encodeQuery(username)
                    + ":"
                    + URLUtil.encodeQuery(password)
                    + "@"
                    + host + ":" + port + fullPath)
        } else URLUtil.completeUrl(domain, fullPath)
    }

    override val storageTypeEnum: StorageTypeEnum
        get() = StorageTypeEnum.FTP

    override fun getFileItem(path: String): FileItemDTO? {
        val fileItemDTO = FileItemDTO()
        fileItemDTO.src = getDownloadUrl(path)
        return fileItemDTO
    }

    override fun storageStrategyConfigList(): List<StorageConfig> {
        return object : ArrayList<StorageConfig?>() {
            init {
                add(StorageConfig("host", "域名或IP"))
                add(StorageConfig("port", "端口"))
                add(StorageConfig("username", "用户名"))
                add(StorageConfig("password", "密码"))
                add(StorageConfig("domain", "加速域名"))
                add(StorageConfig("basePath", "基路径"))
            }
        }
    }
}