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
package me.zhengjie.config

import com.alibaba.fastjson.serializer.SerializerFeature
import com.alibaba.fastjson.support.config.FastJsonConfig
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.charset.StandardCharsets

/**
 * WebMvcConfigurer
 *
 * @author Zheng Jie
 * @date 2018-11-30
 */
@Configuration
@EnableWebMvc
class ConfigurerAdapter(
    /** 文件配置  */
    private val properties: FileProperties
) : WebMvcConfigurer {
    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOriginPattern("*")
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val path = properties.path
        val avatarUtl = "file:" + path?.avatar!!.replace("\\", "/")
        val pathUtl = "file:" + path.path!!.replace("\\", "/")
        registry.addResourceHandler("/avatar/**").addResourceLocations(avatarUtl).setCachePeriod(0)
        registry.addResourceHandler("/file/**").addResourceLocations(pathUtl).setCachePeriod(0)
        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(0)
    }

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>?>) {
        // 使用 fastjson 序列化，会导致 @JsonIgnore 失效，可以使用 @JSONField(serialize = false) 替换
        val converter = FastJsonHttpMessageConverter()
        val supportMediaTypeList: MutableList<MediaType> = ArrayList()
        supportMediaTypeList.add(MediaType.APPLICATION_JSON)
        val config = FastJsonConfig()
        config.dateFormat = "yyyy-MM-dd HH:mm:ss"
        config.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect)
        converter.fastJsonConfig = config
        converter.supportedMediaTypes = supportMediaTypeList
        converter.defaultCharset = StandardCharsets.UTF_8
        converters.add(converter)
    }
}