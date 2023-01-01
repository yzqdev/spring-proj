package cn.hellohao.auth.filter

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

/**
 * 跨域请求过滤器
 *
 * @author Hellohao
 * @date 2021/6/10
 */
@Configuration
class OriginFilter {
    @Value("\${CROS_ALLOWED_ORIGINS}")
   lateinit var allowedOrigins: Array<String>

    @Bean
    fun corsFilter(): FilterRegistrationBean<*> {
        val source = UrlBasedCorsConfigurationSource()
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.allowCredentials = true
        corsConfiguration.allowedOriginPatterns = listOf(CorsConfiguration.ALL)
        corsConfiguration.allowedHeaders = listOf(CorsConfiguration.ALL)
        corsConfiguration.allowedMethods = listOf(CorsConfiguration.ALL)
        corsConfiguration.addExposedHeader("Authorization")
        source.registerCorsConfiguration("/**", corsConfiguration)
        val bean: FilterRegistrationBean<*> = FilterRegistrationBean(CorsFilter(source))
        bean.order = Ordered.HIGHEST_PRECEDENCE
        return bean
    }
}