/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.security.config

import lombok.RequiredArgsConstructor
import me.zhengjie.annotation.AnonymousAccess
import me.zhengjie.modules.security.config.bean.SecurityProperties
import me.zhengjie.modules.security.security.JwtAccessDeniedHandler
import me.zhengjie.modules.security.security.JwtAuthenticationEntryPoint
import me.zhengjie.modules.security.security.TokenConfigurer
import me.zhengjie.modules.security.security.TokenProvider
import me.zhengjie.modules.security.service.OnlineUserService
import me.zhengjie.modules.security.service.UserCacheClean
import me.zhengjie.utils.enums.RequestMethodEnum
import me.zhengjie.utils.enums.RequestMethodEnum.Companion.find
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.core.GrantedAuthorityDefaults
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.filter.CorsFilter
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import java.util.*

/**
 * @author Zheng Jie
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SpringSecurityConfig {
    private val tokenProvider: TokenProvider? = null
    private val corsFilter: CorsFilter? = null
    private val authenticationErrorHandler: JwtAuthenticationEntryPoint? = null
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler? = null
    private val applicationContext: ApplicationContext? = null
    private val properties: SecurityProperties? = null
    private val onlineUserService: OnlineUserService? = null
    private val userCacheClean: UserCacheClean? = null
    @Bean
    fun grantedAuthorityDefaults(): GrantedAuthorityDefaults {
        // 去除 ROLE_ 前缀
        return GrantedAuthorityDefaults("")
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        // 密码加密方式
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        // 搜寻匿名标记 url： @AnonymousAccess
        val requestMappingHandlerMapping =
            applicationContext!!.getBean("requestMappingHandlerMapping") as RequestMappingHandlerMapping
        val handlerMethodMap = requestMappingHandlerMapping.handlerMethods
        // 获取匿名标记
        val anonymousUrls = getAnonymousUrl(handlerMethodMap)
        httpSecurity // 禁用 CSRF
            .csrf().disable()
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter::class.java) // 授权异常
            .exceptionHandling()
            .authenticationEntryPoint(authenticationErrorHandler)
            .accessDeniedHandler(jwtAccessDeniedHandler) // 防止iframe 造成跨域
            .and()
            .headers()
            .frameOptions()
            .disable() // 不创建会话
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests() // 静态资源等等
            .antMatchers(
                HttpMethod.GET,
                "/*.html",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js",
                "/webSocket/**"
            ).permitAll() // swagger 文档
            .antMatchers("/swagger-ui/**").permitAll()
            .antMatchers("/v2/api-docs/**").permitAll()
            .antMatchers("/v3/api-docs/**").permitAll()
            .antMatchers("/swagger-resources/**").permitAll()
            .antMatchers("/webjars/**").permitAll()
            .antMatchers("/swagger-ui.html/**").permitAll() // 文件
            .antMatchers("/avatar/**").permitAll()
            .antMatchers("/file/**").permitAll() // 阿里巴巴 druid
            .antMatchers("/druid/**").permitAll() // 放行OPTIONS请求
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // 自定义匿名访问所有url放行：允许匿名和带Token访问，细腻化到每个 Request 类型
            // GET
            .antMatchers(HttpMethod.GET, *anonymousUrls[RequestMethodEnum.GET.getType()]!!.toTypedArray())
            .permitAll() // POST
            .antMatchers(HttpMethod.POST, *anonymousUrls[RequestMethodEnum.POST.getType()]!!.toTypedArray())
            .permitAll() // PUT
            .antMatchers(HttpMethod.PUT, *anonymousUrls[RequestMethodEnum.PUT.getType()]!!.toTypedArray())
            .permitAll() // PATCH
            .antMatchers(HttpMethod.PATCH, *anonymousUrls[RequestMethodEnum.PATCH.getType()]!!.toTypedArray())
            .permitAll() // DELETE
            .antMatchers(
                HttpMethod.DELETE, *anonymousUrls[RequestMethodEnum.DELETE.getType()]!!
                    .toTypedArray()
            ).permitAll() // 所有类型的接口都放行
            .antMatchers(*anonymousUrls[RequestMethodEnum.ALL.getType()]!!.toTypedArray()).permitAll() // 所有请求都需要认证
            .anyRequest().authenticated()
            .and().apply(securityConfigurerAdapter())
        return httpSecurity.build()
    }

    private fun securityConfigurerAdapter(): TokenConfigurer {
        return TokenConfigurer(tokenProvider, properties, onlineUserService, userCacheClean)
    }

    private fun getAnonymousUrl(handlerMethodMap: Map<RequestMappingInfo, HandlerMethod>): Map<String, Set<String>> {
        val anonymousUrls: MutableMap<String, Set<String>> = HashMap(6)
        val get: MutableSet<String> = HashSet()
        val post: MutableSet<String> = HashSet()
        val put: MutableSet<String> = HashSet()
        val patch: MutableSet<String> = HashSet()
        val delete: MutableSet<String> = HashSet()
        val all: MutableSet<String> = HashSet()
        for ((key, handlerMethod) in handlerMethodMap) {
            val anonymousAccess = handlerMethod.getMethodAnnotation(AnonymousAccess::class.java)
            if (null != anonymousAccess) {
                val requestMethods: List<RequestMethod> = ArrayList(key.methodsCondition.methods)
                val request =
                    find(if (requestMethods.size == 0) RequestMethodEnum.ALL.getType() else requestMethods[0].name)
                when (Objects.requireNonNull(request)) {
                    RequestMethodEnum.GET -> {
                        assert(key.patternsCondition != null)
                        get.addAll(key.patternsCondition!!.patterns)
                    }

                    RequestMethodEnum.POST -> post.addAll(key.patternsCondition!!.patterns)
                    RequestMethodEnum.PUT -> put.addAll(key.patternsCondition!!.patterns)
                    RequestMethodEnum.PATCH -> patch.addAll(key.patternsCondition!!.patterns)
                    RequestMethodEnum.DELETE -> delete.addAll(key.patternsCondition!!.patterns)
                    else -> all.addAll(key.patternsCondition!!.patterns)
                }
            }
        }
        anonymousUrls[RequestMethodEnum.GET.getType()] = get
        anonymousUrls[RequestMethodEnum.POST.getType()] = post
        anonymousUrls[RequestMethodEnum.PUT.getType()] = put
        anonymousUrls[RequestMethodEnum.PATCH.getType()] = patch
        anonymousUrls[RequestMethodEnum.DELETE.getType()] = delete
        anonymousUrls[RequestMethodEnum.ALL.getType()] = all
        return anonymousUrls
    }
}