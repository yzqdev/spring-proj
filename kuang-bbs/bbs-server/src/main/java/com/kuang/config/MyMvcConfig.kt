package com.kuang.config

import com.kuang.interceptor.CorsInterceptor
import com.kuang.interceptor.LoginInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * 我的mvc配置
 *
 * @author yanni
 * @date 2021/11/22
 */
@Configuration
class MyMvcConfig : WebMvcConfigurer {
    @get:Bean
    val login: LoginInterceptor
        get() = LoginInterceptor()

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(CorsInterceptor()).addPathPatterns("/**")
        registry.addInterceptor(login).addPathPatterns("/**")
            .excludePathPatterns("/register", "/login", "/swagger-ui/**", "/v3/**", "/swagger-ui.html")
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/upload/**")
            .addResourceLocations("file:" + System.getProperty("user.dir") + "/upload/")
    }
}