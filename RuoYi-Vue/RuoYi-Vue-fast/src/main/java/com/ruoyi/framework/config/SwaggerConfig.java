package com.ruoyi.framework.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 的配置
 *
 * @author yanni
 * @date 2021/11/21
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "我的博客",
                version = "1.0",
                description = "我的博客",
                contact = @Contact(name = "TOM")
        ),
        security = @SecurityRequirement(name = "token")
)
@SecurityScheme(type = SecuritySchemeType.APIKEY, name = "token",in = SecuritySchemeIn.HEADER)

public class SwaggerConfig {
    @Bean
    public GroupedOpenApi docker() {
        return GroupedOpenApi.builder()
                //.addOpenApiMethodFilter(method -> method.isAnnotationPresent(RestController.class))
                .packagesToScan("com.ruoyi")
                .group("api")
                .pathsToMatch("/**").build();


    }


}