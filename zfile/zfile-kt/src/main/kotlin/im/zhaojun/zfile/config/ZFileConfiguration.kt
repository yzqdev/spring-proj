package im.zhaojun.zfile.config

import org.apache.http.client.HttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.nio.charset.StandardCharsets

/**
 * @author zhaojun
 */
@Configuration
class ZFileConfiguration {
    @Bean
    fun restTemplate(): RestTemplate {
        val httpRequestFactory = HttpComponentsClientHttpRequestFactory()
        val httpClient: HttpClient = HttpClientBuilder.create().build()
        httpRequestFactory.setHttpClient(httpClient)
        val restTemplate = RestTemplate(httpRequestFactory)
        restTemplate.messageConverters.set(1, StringHttpMessageConverter(StandardCharsets.UTF_8))
        restTemplate.interceptors =
            listOf(ClientHttpRequestInterceptor { request: HttpRequest?, body: ByteArray?, execution: ClientHttpRequestExecution ->
                val response = execution.execute(request, body)
                val headers = response.headers
                headers["Content-Type"] = listOf("application/text")
                response
            })
        return restTemplate
    }
}