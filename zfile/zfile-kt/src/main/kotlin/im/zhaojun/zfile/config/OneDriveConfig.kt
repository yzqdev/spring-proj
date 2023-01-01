package im.zhaojun.zfile.config

import im.zhaojun.zfile.service.StorageConfigService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpRequest
import java.lang.String
import javax.annotation.Resource

/**
 * @author zhaojun
 */
@Configuration
class OneDriveConfig {
    @Resource
    private val storageConfigService: StorageConfigService? = null

    /**
     * OneDrive 请求 RestTemplate, 会在请求头中添加 Bearer: Authorization {token} 信息, 用于 API 认证.
     */
    @Bean
    fun oneDriveRestTemplate(): RestTemplate {
        val restTemplate = RestTemplate()
        val interceptor =
            ClientHttpRequestInterceptor { httpRequest: HttpRequest, bytes: ByteArray?, clientHttpRequestExecution: ClientHttpRequestExecution ->
                val headers = httpRequest.headers
                val driveId = (headers["driveId"] as List<*>?)!![0].toString()
                val accessTokenConfig: StorageConfig =
                    storageConfigService.findByDriveIdAndKey(driveId, StorageConfigConstant.ACCESS_TOKEN_KEY)
                val tokenValue = String.format("%s %s", "Bearer", accessTokenConfig.getValue())
                httpRequest.headers.add("Authorization", tokenValue)
                clientHttpRequestExecution.execute(httpRequest, bytes)
            }
        restTemplate.setInterceptors(listOf<ClientHttpRequestInterceptor>(interceptor))
        return restTemplate
    }
}