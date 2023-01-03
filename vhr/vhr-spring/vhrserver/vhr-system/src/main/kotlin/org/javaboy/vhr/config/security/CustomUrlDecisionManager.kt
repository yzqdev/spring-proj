package org.javaboy.vhr.config.security

org.springframework.stereotype.Component
/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 * @时间 2019-09-29 7:53
 */
@Component
class CustomUrlDecisionManager : AccessDecisionManager {
    @Throws(AccessDeniedException::class, InsufficientAuthenticationException::class)
    fun decide(authentication: Authentication, `object`: Any?, configAttributes: Collection<ConfigAttribute?>) {
        for (configAttribute in configAttributes) {
            val needRole: String = configAttribute.getAttribute()
            if ("ROLE_LOGIN" == needRole) {
                if (authentication is AnonymousAuthenticationToken) {
                    throw AccessDeniedException("尚未登录，请登录!")
                } else {
                    return
                }
            }
            val authorities: Collection<GrantedAuthority?> = authentication.getAuthorities()
            for (authority in authorities) {
                if (authority.getAuthority().equals(needRole)) {
                    return
                }
            }
        }
        throw AccessDeniedException("权限不足，请联系管理员!")
    }

    fun supports(attribute: ConfigAttribute?): Boolean {
        return true
    }

    fun supports(clazz: Class<*>?): Boolean {
        return true
    }
}