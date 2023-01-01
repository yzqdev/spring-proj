package im.zhaojun.zfile.service.impl

import cn.hutool.core.convert.Convert
import cn.hutool.core.util.StrUtil
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import im.zhaojun.zfile.model.constant.StorageConfigConstant
import im.zhaojun.zfile.model.entity.StorageConfig
import im.zhaojun.zfile.model.enums.StorageTypeEnum
import im.zhaojun.zfile.service.base.AbstractS3BaseFileService
import im.zhaojun.zfile.service.base.BaseFileService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author zhaojun
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class S3ServiceImpl : AbstractS3BaseFileService(), BaseFileService {
    override fun init(driveId: String?) {
        this.driveId = driveId
        val stringStorageConfigMap = storageConfigService!!.selectStorageConfigMapByDriveId(driveId)
        mergeStrategyConfig(stringStorageConfigMap)
        val accessKey = stringStorageConfigMap[StorageConfigConstant.ACCESS_KEY]!!.value
        val secretKey = stringStorageConfigMap[StorageConfigConstant.SECRET_KEY]!!.value
        val endPoint = stringStorageConfigMap[StorageConfigConstant.ENDPOINT_KEY]!!.value
        var region = stringStorageConfigMap[StorageConfigConstant.REGION_KEY]!!.value
        super.domain = stringStorageConfigMap[StorageConfigConstant.DOMAIN_KEY]!!.value
        super.basePath = stringStorageConfigMap[StorageConfigConstant.BASE_PATH]!!.value
        super.bucketName = stringStorageConfigMap[StorageConfigConstant.BUCKET_NAME_KEY]!!.value
        super.isPrivate = Convert.toBool(
            stringStorageConfigMap[StorageConfigConstant.IS_PRIVATE]!!.value, true
        )
        val pathStyle = stringStorageConfigMap[StorageConfigConstant.PATH_STYLE]!!.value
        val isPathStyle = "path-style" == pathStyle
        if (Objects.isNull(accessKey) || Objects.isNull(secretKey) || Objects.isNull(endPoint) || Objects.isNull(
                bucketName
            )
        ) {
            log.debug("初始化存储策略 [{}] 失败: 参数不完整", storageTypeEnum.description)
            isInitialized = false
        } else {
            if (StrUtil.isNotEmpty(endPoint) && StrUtil.isEmpty(region)) {
                region = endPoint.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            }
            val credentials = BasicAWSCredentials(accessKey, secretKey)
            s3Client = AmazonS3ClientBuilder.standard()
                .withPathStyleAccessEnabled(isPathStyle)
                .withCredentials(AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(endPoint, region)).build()
            testConnection()
            isInitialized = true
        }
    }

    override val storageTypeEnum: StorageTypeEnum
        get() = StorageTypeEnum.S3

    override fun storageStrategyConfigList(): List<StorageConfig> {
        return object : ArrayList<StorageConfig?>() {
            init {
                add(StorageConfig("accessKey", "AccessKey"))
                add(StorageConfig("secretKey", "SecretKey"))
                add(StorageConfig("endPoint", "服务地址(EndPoint)"))
                add(StorageConfig("bucketName", "存储空间名称"))
                add(StorageConfig("region", "地域"))
                add(StorageConfig("basePath", "基路径"))
                add(StorageConfig("domain", "加速域名"))
                add(StorageConfig("pathStyle", "域名风格"))
                add(StorageConfig("isPrivate", "是否是私有空间"))
            }
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(S3ServiceImpl::class.java)
    }
}