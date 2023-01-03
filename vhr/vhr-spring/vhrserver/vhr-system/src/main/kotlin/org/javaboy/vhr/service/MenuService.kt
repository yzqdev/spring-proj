package org.javaboy.vhr.service

org.springframework.security.core.context.SecurityContextHolderimport org.springframework.stereotype.Service
/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 * @时间 2019-09-27 7:13
 */
@Service
@CacheConfig(cacheNames = ["menus_cache"])
@RequiredArgsConstructor
class MenuService {
    private val menuMapper: MenuMapper? = null
    private val menuRoleMapper: MenuRoleMapper? = null
    val menusByHrId: List<Menu>
        get() = menuMapper.getMenusByHrId(
            (SecurityContextHolder.getContext().getAuthentication().getPrincipal() as Hr).id
        )

    @get:Cacheable
    val allMenusWithRole: List<Menu>
        get() = menuMapper.getAllMenusWithRole()
    val allMenus: List<Menu>
        get() = menuMapper.getAllMenus()

    fun getMidsByRid(rid: Int?): List<Int> {
        return menuMapper.getMidsByRid(rid)
    }

    @Transactional
    fun updateMenuRole(rid: Int?, mids: Array<Int?>?): Boolean {
        menuRoleMapper.deleteByRid(rid)
        if (mids == null || mids.size == 0) {
            return true
        }
        val result: Int = menuRoleMapper.insertRecord(rid, mids)
        return result == mids.size
    }
}