package com.kuang.config;

import com.kuang.result.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Priority;
import javax.validation.constraints.NotNull;

/**
 * Controller advice for comment result.
 *
 * @author johnniang
 */
@ControllerAdvice("com.kuang.controller")
@Slf4j

public class CommonResultControllerAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType,
        @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
        return AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    @NonNull
    public final Object beforeBodyWrite(@Nullable Object body,
        @NotNull MethodParameter returnType,
        @NotNull MediaType contentType,
        @NotNull Class<? extends HttpMessageConverter<?>> converterType,
        @NotNull ServerHttpRequest request,
        @NotNull ServerHttpResponse response) {
        MappingJacksonValue container = getOrCreateContainer(body);
        // The contain body will never be null
        beforeBodyWriteInternal(container, contentType, returnType, request, response);
        return container;
    }

    /**
     * Wrap the body in a {@link MappingJacksonValue} value container (for providing
     * additional serialization instructions) or simply cast it if already wrapped.
     */
    private MappingJacksonValue getOrCreateContainer(Object body) {
        return body instanceof MappingJacksonValue ? (MappingJacksonValue) body :
            new MappingJacksonValue(body);
    }

    private void beforeBodyWriteInternal(MappingJacksonValue bodyContainer,
        MediaType contentType,
        MethodParameter returnType,
        ServerHttpRequest request,
        ServerHttpResponse response) {
        // Get return body
        Object returnBody = bodyContainer.getValue();
        log.info("重写json中");
        if (returnBody instanceof BaseResponse<?> baseResponse) {
            // If the return body is instance of BaseResponse, then just do nothing
            HttpStatus status = HttpStatus.resolve(baseResponse.getCode());
            if (status == null) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            response.setStatusCode(status);
            return;
        }

        // get status
        var status = HttpStatus.OK;
        if (response instanceof ServletServerHttpResponse) {
            var servletResponse =
                ((ServletServerHttpResponse) response).getServletResponse();
            status = HttpStatus.resolve(servletResponse.getStatus());
            if (status == null) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        var baseResponse = new BaseResponse<>(status.is2xxSuccessful(),status.value(), status.getReasonPhrase(),  returnBody);
        bodyContainer.setValue(baseResponse);
    }

}
