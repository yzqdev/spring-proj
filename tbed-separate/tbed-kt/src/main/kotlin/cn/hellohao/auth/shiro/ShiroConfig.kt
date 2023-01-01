package cn.hellohao.auth.shiro

import cn.hellohao.auth.filter.SubjectFilter
import cn.hutool.core.lang.Console
import org.apache.shiro.spring.web.ShiroFilterFactoryBean
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition
import org.apache.shiro.web.mgt.DefaultWebSecurityManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author Hellohao
 * @version 1.0
 * @date 2021/6/3 10:37
 */
@Configuration
class ShiroConfig {
    @Bean
    fun shiroFilterChainDefinition(): ShiroFilterChainDefinition {
        val definition = DefaultShiroFilterChainDefinition()
        //definition.addPathDefinition("/doLogin", "anon");
        definition.addPathDefinition("/verifyCode", "anon")
        definition.addPathDefinition("/verifyCodeForRegister", "anon")
        definition.addPathDefinition("/verifyCodeForRetrieve", "anon")
        definition.addPathDefinition("/api/**", "anon")
        definition.addPathDefinition("/user/**", "anon")
        definition.addPathDefinition("/user/activation", "anon")
        definition.addPathDefinition("/ota/**", "anon")
        definition.addPathDefinition("/admin/root/**", "roles[admin]")
        definition.addPathDefinition("/**", "JWT")
        definition.addPathDefinition("/swagger-ui/**", "anon")
        return definition
    }

    @Bean
    fun shiroFilterFactoryBean(
        @Qualifier("defaultWebSecurityManager") defaultWebSecurityManager: DefaultWebSecurityManager?,
        shiroFilterChainDefinition: ShiroFilterChainDefinition
    ): ShiroFilterFactoryBean {
        val bean = ShiroFilterFactoryBean()
        bean.securityManager = defaultWebSecurityManager
        val map = shiroFilterChainDefinition.filterChainMap
        //添加filterchainmap
        Console.log("maps=>{}", map)
        bean.filterChainDefinitionMap = map
        val filters = bean.filters
        filters["JWT"] = SubjectFilter()
        bean.filters = filters
        bean.loginUrl = "/jurisError"
        bean.unauthorizedUrl = "/authError"
        return bean
    }

    @Bean
    fun defaultWebSecurityManager(@Qualifier("userRealm") userRealm: UserRealm?): DefaultWebSecurityManager {
        val defaultWebSecurityManager = DefaultWebSecurityManager()
        defaultWebSecurityManager.setRealm(userRealm)
        defaultWebSecurityManager.rememberMeManager = null
        return defaultWebSecurityManager
    }

    @Bean(name = ["userRealm"])
    fun userRealm(): UserRealm {
        return UserRealm()
    }
}