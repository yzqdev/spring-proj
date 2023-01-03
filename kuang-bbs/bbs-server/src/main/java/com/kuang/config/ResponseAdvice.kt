package com.kuang.config

import com.kuang.result.BaseResponse
import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter
import org.springframework.http.converter.json.MappingJacksonValue
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

/**
 * @author yzqde
 * @date time 2022/9/18 13:28
 * @modified By:
 *
 */
@ControllerAdvice("com.kuang.controller")
class ResponseAdvice : ResponseBodyAdvice<Any> {
    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return AbstractJackson2HttpMessageConverter::class.java.isAssignableFrom(converterType)
//      returnType  AbstractJackson2HttpMessageConverter::class.java.isAssignableFrom(converterType)
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        TODO("Not yet implemented")
    }

    /**
     * Wrap the body in a {@link MappingJacksonValue} value container (for providing
     * additional serialization instructions) or simply cast it if already wrapped.
     */
    fun getOrCreateContainer(body: Any): MappingJacksonValue {

        return if (body is MappingJacksonValue) {
            body
        } else {
            MappingJacksonValue(body)
        }
    }

    fun beforeBodyWriteInternal(
        bodyContainer: MappingJacksonValue,
        contentType: MediaType,
        returnType: MethodParameter,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ) {
        // Get return body
        var returnBody = bodyContainer.value;

        if (returnBody instanceof BaseResponse<?> baseResponse) {
            // If the return body is instance of BaseResponse, then just do nothing
            var status = HttpStatus.resolve(baseResponse.getCode());
            if (status == null) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            response.setStatusCode(status);
            return;
        }

        // get status
        var status = HttpStatus.OK;
        if (response is ServletServerHttpResponse) {
            var servletResponse =
                (response).servletResponse;
            status = HttpStatus.resolve(servletResponse.getStatus());
            if (status == null) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        var baseResponse = BaseResponse<Any>(status.is2xxSuccessful, status.value, status.reasonPhrase, returnBody);
        bodyContainer.setValue(baseResponse);
    }
}