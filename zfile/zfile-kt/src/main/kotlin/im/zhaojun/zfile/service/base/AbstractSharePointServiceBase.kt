package im.zhaojun.zfile.service.baseimport

import im.zhaojun.zfile.model.entity.StorageConfig
import im.zhaojun.zfile.service.base.MicrosoftDriveServiceBase

im.zhaojun.zfile.util.StringUtils.removeFirstSeparator
import im.zhaojun.zfile.util.StringUtils.getFullPath
import im.zhaojun.zfile.util.StringUtils.concatUrl
import im.zhaojun.zfile.util.StringUtils.generatorLink
import im.zhaojun.zfile.util.StringUtils.removeDuplicateSeparator
import im.zhaojun.zfile.util.StringUtils.isNotNullOrEmpty
import im.zhaojun.zfile.util.StringUtils.concatPath
import im.zhaojun.zfile.util.StringUtils.removeLastSeparator
import im.zhaojun.zfile.util.StringUtils.replaceHost
import im.zhaojun.zfile.util.StringUtils.isNullOrEmpty
import im.zhaojun.zfile.model.dto.FileItemDTO
import im.zhaojun.zfile.service.base.BaseFileService
import im.zhaojun.zfile.cache.ZFileCache
import im.zhaojun.zfile.exception.InitializeDriveException
import im.zhaojun.zfile.model.enums.StorageTypeEnum
import im.zhaojun.zfile.model.entity.StorageConfig
import im.zhaojun.zfile.service.base.AbstractBaseFileService
import im.zhaojun.zfile.service.StorageConfigService
import im.zhaojun.zfile.model.enums.FileTypeEnum
import im.zhaojun.zfile.model.constant.ZFileConstant
import cn.hutool.core.util.BooleanUtil
import im.zhaojun.zfile.exception.NotExistFileException
import org.springframework.web.client.RestTemplate
import im.zhaojun.zfile.repository.StorageConfigRepository
import im.zhaojun.zfile.model.support.OneDriveToken
import im.zhaojun.zfile.model.constant.StorageConfigConstant
import im.zhaojun.zfile.service.base.MicrosoftDriveServiceBase
import org.springframework.web.client.HttpClientErrorException
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import im.zhaojun.zfile.service.base.AbstractS3BaseFileService
import im.zhaojun.zfile.service.impl.S3ServiceImpl
import cn.hutool.core.util.StrUtil
import cn.hutool.extra.ftp.Ftp
import lombok.SneakyThrows
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPFile
import im.zhaojun.zfile.repository.SystemConfigRepository
import im.zhaojun.zfile.service.impl.LocalServiceImpl
import im.zhaojun.zfile.model.entity.SystemConfig
import im.zhaojun.zfile.model.constant.SystemConfigConstant
import im.zhaojun.zfile.service.impl.MinIOServiceImpl
import im.zhaojun.zfile.service.impl.QiniuServiceImpl
import im.zhaojun.zfile.service.impl.UpYunServiceImpl
import com.UpYun
import cn.hutool.core.util.ObjectUtil
import com.UpYun.FolderItemIter
import com.UpYun.FolderItem
import com.upyun.UpException
import im.zhaojun.zfile.service.impl.AliyunServiceImpl
import im.zhaojun.zfile.service.impl.HuaweiServiceImpl
import im.zhaojun.zfile.service.impl.TencentServiceImpl
import im.zhaojun.zfile.service.base.AbstractOneDriveServiceBase
import im.zhaojun.zfile.service.impl.OneDriveServiceImpl
import im.zhaojun.zfile.service.base.AbstractSharePointServiceBase
import im.zhaojun.zfile.service.impl.SharePointServiceImpl
import im.zhaojun.zfile.service.impl.OneDriveChinaServiceImpl
import im.zhaojun.zfile.service.impl.SharePointChinaServiceImpl
import im.zhaojun.zfile.repository.DriverConfigRepository
import im.zhaojun.zfile.repository.FilterConfigRepository
import im.zhaojun.zfile.repository.ShortLinkConfigRepository
import im.zhaojun.zfile.context.DriveContext
import im.zhaojun.zfile.model.entity.DriveConfig
import im.zhaojun.zfile.model.dto.DriveConfigDTO
import im.zhaojun.zfile.model.dto.StorageStrategyConfig
import im.zhaojun.zfile.service.DriveConfigService
import im.zhaojun.zfile.context.StorageTypeContext
import im.zhaojun.zfile.model.dto.CacheInfoDTO
import im.zhaojun.zfile.service.FilterConfigService
import im.zhaojun.zfile.model.dto.SystemConfigDTO
import im.zhaojun.zfile.service.SystemConfigService
import im.zhaojun.zfile.model.dto.SystemFrontConfigDTO
import im.zhaojun.zfile.exception.InvalidDriveException
import cn.hutool.crypto.SecureUtil
import im.zhaojun.zfile.model.entity.ShortLinkConfig
import org.springframework.data.domain.PageRequest
import javax.persistence.criteria.CriteriaBuilder

abstract class AbstractSharePointServiceBase : MicrosoftDriveServiceBase() {
    protected var siteId: String? = null
    override val type: String
        get() = "sites/$siteId"

    override fun getDownloadUrl(path: String?): String? {
        return null
    }

    override fun storageStrategyConfigList(): List<StorageConfig> {
        return object : ArrayList<StorageConfig?>() {
            init {
                add(StorageConfig("accessToken", "访问令牌"))
                add(StorageConfig("refreshToken", "刷新令牌"))
                add(StorageConfig("proxyDomain", "反代域名"))
                add(StorageConfig("basePath", "基路径"))
                add(StorageConfig("siteName", "站点名称"))
                add(StorageConfig("siteId", "SiteId"))
                add(StorageConfig("siteType", "siteType"))
            }
        }
    }
}