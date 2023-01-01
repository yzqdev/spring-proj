package me.zhengjie.modules.security.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 这个类并没有什么作用,只是测试过滤器和拦截器的区别
 */
@Slf4j

public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug("start to auth request validate...111");
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        if (token  == null) {
            //    :TODO check token
            log.info("auth success");

        } else {
            log.error("auth failed");
        }
        chain.doFilter(request, response);
    }
}