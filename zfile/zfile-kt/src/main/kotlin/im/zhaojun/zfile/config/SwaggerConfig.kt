package im.zhaojun.zfile.config

import io.swagger.v3.oas.annotations.ExternalDocumentation
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * 的配置
 *
 * @author yanni
 * @date 2021/11/21
 */
@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "Swagger3",
        version = "1.0",
        description = "Swagger3使用演示",
        contact = Contact(name = "TOM")
    ),
    security = [SecurityRequirement(name = "token")],
    externalDocs = ExternalDocumentation(
        description = "参考文档",
        url = "https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations"
    )
)
@SecurityScheme(type = SecuritySchemeType.APIKEY, name = "token", `in` = SecuritySchemeIn.HEADER)
class SwaggerConfig {
    @Bean
    fun docker(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .packagesToScan("im.zhaojun.zfile.controller")
            .group("api")
            .pathsToMatch("/**").build()
    }
}