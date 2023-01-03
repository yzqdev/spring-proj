package org.javaboy.vhr.config.security

import com.fasterxml.jackson.databind.ObjectMapperimport

org.springframework.context.annotation .Beanimport org.springframework.context.annotation .Configurationimport org.springframework.core.annotation .Orderimport org.springframework.security.authentication.*import java.io.PrintWriterimport

javax.annotation .Resource
/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 */
@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Resource
    var hrService: HrService? = null

    @Resource
    var customFilterInvocationSecurityMetadataSource: CustomFilterInvocationSecurityMetadataSource? = null

    @Resource
    var customUrlDecisionManager: CustomUrlDecisionManager? = null
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    /**
     * 用户详细信息服务
     *
     * @return [UserDetailsService]
     */
    @Bean
    @Throws(Exception::class)
    fun authenticationManagerBean(objectPostProcessor: ObjectPostProcessor<Any?>?): AuthenticationManager {
        return AuthenticationManagerBuilder(objectPostProcessor)
            .userDetailsService(hrService)
            .passwordEncoder(passwordEncoder())
            .and().build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService? {
        return hrService
    }

    /**
     * 网络安全编辑器
     * [原因](https://github.com/spring-projects/spring-security/issues/10938)
     * @return [WebSecurityCustomizer]
     */
    //@Bean
    //public WebSecurityCustomizer webSecurityCustomizer()  {
    //  return web ->  web.ignoring().antMatchers("/css/**", "/js/**", "/index.html", "/img/**", "/fonts/**", "/favicon.ico", "/verifyCode","/swagger-ui/*","/v2/api-docs/**","/v3/api-docs/**",
    //            "/swagger-resources",
    //            "/swagger-resources/**",
    //            "/configuration/ui",
    //            "/configuration/security",
    //            "/swagger-ui.html/**",
    //            "/webjars/**" );
    //
    //}
    @Bean
    @Order(0)
    @Throws(Exception::class)
    fun resources(http: HttpSecurity): SecurityFilterChain {
        http
            .requestMatchers { matchers ->
                matchers.antMatchers(
                    "/css/**",
                    "/js/**",
                    "/index.html",
                    "/img/**",
                    "/fonts/**",
                    "/favicon.ico",
                    "/verifyCode",
                    "/swagger-ui/*",
                    "/v2/api-docs/**",
                    "/v3/api-docs/**",
                    "/swagger-resources",
                    "/swagger-resources/**",
                    "/configuration/ui",
                    "/configuration/security",
                    "/swagger-ui.html/**",
                    "/webjars/**"
                )
            }
            .authorizeHttpRequests { authorize -> authorize.anyRequest().permitAll() }
            .requestCache().disable()
            .securityContext().disable()
            .sessionManagement().disable()
        return http.build()
    }

    @Bean
    @Throws(Exception::class)
    fun loginFilter(): LoginFilter {
        val loginFilter = LoginFilter()
        loginFilter.setAuthenticationSuccessHandler { request, response, authentication ->
            response.setContentType("application/json;charset=utf-8")
            val out: PrintWriter = response.getWriter()
            val hr: Hr = authentication.getPrincipal() as Hr
            hr.password = null
            val ok: RespBean = RespBean.ok("登录成功!", hr)
            val s = ObjectMapper().writeValueAsString(ok)
            out.write(s)
            out.flush()
            out.close()
        }
        loginFilter.setAuthenticationFailureHandler { request, response, exception ->
            response.setContentType("application/json;charset=utf-8")
            val out: PrintWriter = response.getWriter()
            val respBean: RespBean = RespBean.error(exception.getMessage())
            if (exception is LockedException) {
                respBean.setMsg("账户被锁定，请联系管理员!")
            } else if (exception is CredentialsExpiredException) {
                respBean.setMsg("密码过期，请联系管理员!")
            } else if (exception is AccountExpiredException) {
                respBean.setMsg("账户过期，请联系管理员!")
            } else if (exception is DisabledException) {
                respBean.setMsg("账户被禁用，请联系管理员!")
            } else if (exception is BadCredentialsException) {
                respBean.setMsg("用户名或者密码输入错误，请重新输入!")
            }
            out.write(ObjectMapper().writeValueAsString(respBean))
            out.flush()
            out.close()
        }
        loginFilter.setAuthenticationManager(authenticationManagerBean(null))
        loginFilter.setFilterProcessesUrl("/doLogin")
        val sessionStrategy = ConcurrentSessionControlAuthenticationStrategy(sessionRegistry())
        sessionStrategy.setMaximumSessions(1)
        loginFilter.setSessionAuthenticationStrategy(sessionStrategy)
        return loginFilter
    }

    @Bean
    fun sessionRegistry(): SessionRegistryImpl {
        return SessionRegistryImpl()
    }

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors().and().authorizeRequests()
            .withObjectPostProcessor(object : ObjectPostProcessor<FilterSecurityInterceptor?>() {
                fun <O : FilterSecurityInterceptor?> postProcess(`object`: O): O {
                    `object`.setAccessDecisionManager(customUrlDecisionManager)
                    `object`.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource)
                    return `object`
                }
            })
            .and()
            .logout()
            .logoutSuccessHandler { req, resp, authentication ->
                resp.setContentType("application/json;charset=utf-8")
                val out: PrintWriter = resp.getWriter()
                out.write(ObjectMapper().writeValueAsString(RespBean.ok("注销成功!")))
                out.flush()
                out.close()
            }
            .permitAll()
            .and()
            .csrf().disable().exceptionHandling() //没有认证时，在这里处理结果，不要重定向
            .authenticationEntryPoint { req, resp, authException ->
                resp.setContentType("application/json;charset=utf-8")
                resp.setStatus(401)
                val out: PrintWriter = resp.getWriter()
                val respBean: RespBean = RespBean.error("访问失败!")
                if (authException is InsufficientAuthenticationException) {
                    respBean.setMsg("请求失败，请联系管理员!")
                }
                out.write(ObjectMapper().writeValueAsString(respBean))
                out.flush()
                out.close()
            }
        http.addFilterAt(ConcurrentSessionFilter(sessionRegistry()) { event ->
            val resp: HttpServletResponse = event.getResponse()
            resp.setContentType("application/json;charset=utf-8")
            resp.setStatus(401)
            val out: PrintWriter = resp.getWriter()
            out.write(ObjectMapper().writeValueAsString(RespBean.error("您已在另一台设备登录，本次登录已下线!")))
            out.flush()
            out.close()
        }, ConcurrentSessionFilter::class.java)
        http.sessionManagement().disable()
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}