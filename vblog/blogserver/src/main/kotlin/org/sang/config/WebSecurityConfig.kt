package org.sang.config

import lombok.extern.slf4j.Slf4j
import org.sang.bean.User
import org.sang.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.access.AccessDeniedHandler
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author sang
 * @date 2017/12/17
 */
@Slf4j
@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    var userService: UserService? = null
    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userService)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors().and().authorizeRequests()
            .antMatchers("/admin/category/all")
            .authenticated() ///admin/**的URL都需要有超级管理员角色，如果使用.hasAuthority()方法来配置，需要在参数中加上ROLE_,如下.hasAuthority("ROLE_超级管理员")
            .antMatchers("/admin/**", "/reg").hasRole("超级管理员")
            .anyRequest().authenticated() //其他的路径都是登录后即可访问
            .and().formLogin() //登录成功后的返回结果
            .successHandler { httpServletRequest: HttpServletRequest?, httpServletResponse: HttpServletResponse, authentication: Authentication? ->
                httpServletResponse.contentType = "application/json;charset=utf-8"

                //登录成功后获取当前登录用户
                val userDetail = SecurityContextHolder.getContext().authentication.principal as User
                WebSecurityConfig.log.info("用户[{}]于[{}]登录成功!", userDetail.username, Date())
                val out = httpServletResponse.writer
                out.write("{\"status\":\"success\",\"msg\":\"登录成功\"}")
                out.flush()
                out.close()
            } //登录失败后的返回结果
            .failureHandler { httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse, e: AuthenticationException? ->
                httpServletResponse.contentType = "application/json;charset=utf-8"
                val username = httpServletRequest.getParameter("username")
                WebSecurityConfig.log.info(
                    "用户[{}]于[{}]登录失败,失败原因：[{}]",
                    username,
                    Date(),
                    httpServletResponse.status
                )
                val out = httpServletResponse.writer
                out.write("{\"status\":\"error\",\"msg\":\"登录失败\"}")
                out.flush()
                out.close()
            } //这里配置的loginProcessingUrl为页面中对应表单的 action ，该请求为 post，并设置可匿名访问
            //.loginProcessingUrl("/login")
            //.usernameParameter("username").passwordParameter("password").permitAll()
            .and() //前后端分离采用JWT 不需要session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and() //添加JWT过滤器 除已配置的其它请求都需经过此过滤器
            .addFilter(JWTAuthenticationFilter(authenticationManager(), 7)) //退出登录
            .logout().permitAll()
            .logoutSuccessHandler { httpServletRequest: HttpServletRequest?, httpServletResponse: HttpServletResponse, authentication: Authentication? ->
                if (authentication != null) {
                    WebSecurityConfig.log.info(
                        "用户[{}]于[{}]注销成功!",
                        (authentication.principal as User).username,
                        Date()
                    )
                }
                val out = httpServletResponse.writer
                out.write(/* !!! Hit visitElement for element type: class org.jetbrains.kotlin.nj2k.tree.JKErrorExpression !!! */)
                out.flush()
                out.close()
            }.and().csrf().disable().exceptionHandling().accessDeniedHandler(accessDeniedHandler)
    }

    @Throws(Exception::class)
    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/blogimg/**", "/index.html", "/static/**")
    }

    @get:Bean
    val accessDeniedHandler: AccessDeniedHandler
        get() = AuthenticationAccessDeniedHandler()
}