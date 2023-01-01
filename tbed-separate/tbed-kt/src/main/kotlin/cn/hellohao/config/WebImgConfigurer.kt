package cn.hellohao.config

import org.springframework.boot.web.servlet.MultipartConfigFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.unit.DataSize
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.io.File
import javax.servlet.MultipartConfigElement

@Configuration
class WebImgConfigurer : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val filePath = GlobalConstant.LOCPATH + File.separator
        registry.addResourceHandler("/ota/**").addResourceLocations("file:$filePath")
    }

    /**
     * 文件上传配置
     * @return
     */
    @Bean
    fun multipartConfigElement(): MultipartConfigElement {
        val factory = MultipartConfigFactory()
        //  单个数据大小
        factory.setMaxFileSize(DataSize.parse("102400KB")) // KB,MB
        /// 总上传数据大小
        return factory.createMultipartConfig()
    }
}