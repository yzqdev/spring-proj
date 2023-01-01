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
class SharePointChinaServiceImpl : AbstractSharePointServiceBase(), BaseFileService {
    @Resource
    private val storageConfigService: StorageConfigService? = null

    @Value("\${zfile.onedrive-china.clientId}")
    private val clientId: String? = null

    @Value("\${zfile.onedrive-china.redirectUri}")
    private val redirectUri: String? = null

    @Value("\${zfile.onedrive-china.clientSecret}")
    private val clientSecret: String? = null

    @Value("\${zfile.onedrive-china.scope}")
    private val scope: String? = null
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
        isInitialized = if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(
                refreshToken
            )
        ) {
            SharePointChinaServiceImpl.log.debug("初始化存储策略 [{}] 失败: 参数不完整", storageTypeEnum.description)
            false
        } else {
            refreshOneDriveToken()
            testConnection()
            true
        }
    }

    override fun getStorageTypeEnum(): StorageTypeEnum {
        return StorageTypeEnum.SHAREPOINT_DRIVE_CHINA
    }

    override fun getGraphEndPoint(): String {
        return "microsoftgraph.chinacloudapi.cn"
    }

    override fun getAuthenticateEndPoint(): String {
        return "login.partner.microsoftonline.cn"
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