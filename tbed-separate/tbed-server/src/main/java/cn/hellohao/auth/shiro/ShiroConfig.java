package cn.hellohao.auth.shiro;

import cn.hellohao.auth.filter.SubjectFilter;
import cn.hutool.core.lang.Console;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2021/6/3 10:37
 */

@Configuration
public class ShiroConfig {
    @Bean
    ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();
        //definition.addPathDefinition("/doLogin", "anon");

        definition.addPathDefinition("/verifyCode","anon");
        definition.addPathDefinition("/verifyCodeForRegister","anon");
        definition.addPathDefinition("/verifyCodeForRetrieve","anon");
        definition.addPathDefinition("/api/**","anon");
        definition.addPathDefinition("/user/**","anon");
        definition.addPathDefinition("/user/activation","anon");
        definition.addPathDefinition("/ota/**","anon");
        definition.addPathDefinition("/admin/root/**","roles[admin]");
        definition.addPathDefinition("/**","JWT");
        definition.addPathDefinition("/swagger-ui/**", "anon");
        return definition;
    }
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager,ShiroFilterChainDefinition shiroFilterChainDefinition){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(defaultWebSecurityManager);
        Map<String, String> map = shiroFilterChainDefinition.getFilterChainMap();
        //添加filterchainmap
        Console.log("maps=>{}",map);
        bean.setFilterChainDefinitionMap(map);

        Map<String, Filter> filters = bean.getFilters();

        filters.put("JWT",new SubjectFilter());
        bean.setFilters(filters);

        bean.setLoginUrl("/jurisError");
        bean.setUnauthorizedUrl("/authError");

        return bean;

    }

    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(userRealm);
        defaultWebSecurityManager.setRememberMeManager(null);
        return defaultWebSecurityManager;
    }

    @Bean(name = "userRealm")
    public UserRealm userRealm(){
        return new UserRealm();
    }


}

