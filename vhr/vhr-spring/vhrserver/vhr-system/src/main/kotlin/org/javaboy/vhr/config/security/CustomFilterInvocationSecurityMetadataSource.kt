package org.javaboy.vhr.config.security

org.javaboy.vhr.model.Roleimport org.springframework.security.access.ConfigAttributeimport org.springframework.stereotype.Componentimport org.springframework.util.AntPathMatcherimport java.lang.IllegalArgumentException
/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 * @时间 2019-09-29 7:37
 *
 * 这个类的作用，主要是根据用户传来的请求地址，分析出请求需要的角色
 */
@Component
class CustomFilterInvocationSecurityMetadataSource : FilterInvocationSecurityMetadataSource {
    @Autowired
    var menuService: MenuService? = null
    var antPathMatcher = AntPathMatcher()
    @Throws(IllegalArgumentException::class)
    fun getAttributes(`object`: Any): Collection<ConfigAttribute> {
        val requestUrl: String = (`object` as FilterInvocation).getRequestUrl()
        val menus: List<Menu> = menuService.getAllMenusWithRole()
        for (menu in menus) {
            if (antPathMatcher.match(menu.getUrl(), requestUrl)) {
                val roles: List<Role> = menu.getRoles()
                val str = arrayOfNulls<String>(roles.size)
                for (i in roles.indices) {
                    str[i] = roles[i].getName()
                }
                return createList(str)
            }
        }
        return createList("ROLE_LOGIN")
    }

    val allConfigAttributes: Collection<Any>?
        get() = null

    fun supports(clazz: Class<*>?): Boolean {
        return true
    }
}