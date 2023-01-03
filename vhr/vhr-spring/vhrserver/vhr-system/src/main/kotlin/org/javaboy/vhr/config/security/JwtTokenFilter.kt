package org.javaboy.vhr.config.security

import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.io.IOException
import java.util.List
import javax.annotation.Resource
import javax.servlet.FilterChain

@Component
class JwtTokenFilter : OncePerRequestFilter() {
    @Resource
    var hrService: HrService? = null
    @Throws(ServletException::class, IOException::class)
    protected override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        // Get authorization header and validate
        val header: String = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response)
            return
        }

        // Get jwt token and validate
        val token = header.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].trim { it <= ' ' }
        if (!JwtUtil.verifyToken(token)) {
            chain.doFilter(request, response)
            return
        }

        // Get user identity and set it on the spring security context
        val userDetails: UserDetails = hrService
            .loadUserByUsername(JwtUtil.getUserId(token))
        val authentication = UsernamePasswordAuthenticationToken(
            userDetails, null,
            if (userDetails == null) List.of() else userDetails.getAuthorities()
        )
        authentication.setDetails(
            WebAuthenticationDetailsSource().buildDetails(request)
        )
        SecurityContextHolder.getContext().setAuthentication(authentication)
        chain.doFilter(request, response)
    }
}