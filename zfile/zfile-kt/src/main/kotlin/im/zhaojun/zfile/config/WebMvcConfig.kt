package im.zhaojun.zfile.config

import im.zhaojun.zfile.model.enums.StorageTypeEnumDeSerializerConvert
import org.apache.catalina.connector.Connector
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.server.ConfigurableWebServerFactory
import org.springframework.boot.web.server.ErrorPage
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.boot.web.servlet.server.ServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * @author zhaojun
 */
@Configuration
class WebMvcConfig : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(StorageTypeEnumDeSerializerConvert())
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/front/")
    }

    @Bean
    fun webServerFactory(): ServletWebServerFactory {
        val webServerFactory = TomcatServletWebServerFactory()

        // 添加对 URL 中特殊符号的支持.
        webServerFactory.addConnectorCustomizers(TomcatConnectorCustomizer { connector: Connector ->
            connector.setProperty("relaxedPathChars", "<>[\\]^`{|}")
            connector.setProperty("relaxedQueryChars", "<>[\\]^`{|}")
        })
        return webServerFactory
    }

    @Bean
    fun webServerFactoryCustomizer(): WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
        return WebServerFactoryCustomizer { factory: ConfigurableWebServerFactory ->
            val error404Page = ErrorPage(HttpStatus.NOT_FOUND, "/index.html")
            val error200Page = ErrorPage(HttpStatus.OK, "/index.html")
            val errorPages: MutableSet<ErrorPage> = HashSet()
            errorPages.add(error404Page)
            errorPages.add(error200Page)
            factory.setErrorPages(errorPages)
        }
    }
}