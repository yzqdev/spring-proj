package org.javaboy.vhr.service

org.springframework.stereotype.Service
/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 * @时间 2019-09-20 8:21
 */
@Service
@RequiredArgsConstructor
class HrService : UserDetailsService {
    private val hrMapper: HrMapper? = null
    private val hrRoleMapper: HrRoleMapper? = null
    @Throws(UsernameNotFoundException::class)
    fun loadUserByUsername(username: String?): UserDetails? {
        val hr: Hr = hrMapper.loadUserByUsername(username) ?: throw UsernameNotFoundException("用户名不存在!")
        hr.roles = hrMapper.getHrRolesById(hr.id)
        return hr
    }

    fun getAllHrs(keywords: String?): List<Hr> {
        return hrMapper.getAllHrs(HrUtils.getCurrentHr().id, keywords)
    }

    fun updateHr(hr: Hr?): Int {
        return hrMapper.updateByPrimaryKeySelective(hr)
    }

    @Transactional
    fun updateHrRole(hrid: Int?, rids: Array<Int?>): Boolean {
        hrRoleMapper.deleteByHrid(hrid)
        return hrRoleMapper.addRole(hrid, rids) == rids.size
    }

    fun deleteHrById(id: Int?): Int {
        return hrMapper.deleteByPrimaryKey(id)
    }

    val allHrsExceptCurrentHr: List<Any>
        get() = hrMapper.getAllHrsExceptCurrentHr(HrUtils.getCurrentHr().id)

    fun updateHyById(hr: Hr?): Int {
        return hrMapper.updateByPrimaryKeySelective(hr)
    }

    fun updateHrPasswd(oldpass: String?, pass: String?, hrid: Int?): Boolean {
        val hr: Hr = hrMapper.selectByPrimaryKey(hrid)
        val encoder = BCryptPasswordEncoder()
        if (encoder.matches(oldpass, hr.password)) {
            val encodePass: String = encoder.encode(pass)
            val result: Int = hrMapper.updatePasswd(hrid, encodePass)
            if (result == 1) {
                return true
            }
        }
        return false
    }

    fun updateUserface(url: String?, id: Int?): Int {
        return hrMapper.updateUserface(url, id)
    }
}