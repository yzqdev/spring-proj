package cn.hellohao.auth.filter.xss

import java.io.IOException
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest

/**
 * XSS过滤器
 * @author hellohao
 */
@WebFilter(filterName = "xssFilter", urlPatterns = ["/SaveForAlbum", "/register"]) //"/*"为所有请求

class XssFilter : Filter {
    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig) {
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        val request = servletRequest as HttpServletRequest
        val path = request.servletPath
        //由于我的@WebFilter注解配置的是urlPatterns="/*"(过滤所有请求),所以这里对不需要过滤的静态资源url,作忽略处理(大家可以依照具体需求配置)
        val exclusionsUrls =
            arrayOf(".js", ".gif", ".jpg", ".png", ".bmp", ".css", ".ico") //,"/","/index","/admin/root/"
        for (str in exclusionsUrls) {
            //System.out.println(path.contains(str));
            if (path.contains(str)) {
                filterChain.doFilter(servletRequest, servletResponse)
                return
            }
        }
        filterChain.doFilter(XssHttpServletRequestWrapper(request), servletResponse)
    }

    override fun destroy() {}
}