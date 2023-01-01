package cn.hellohao.service.impl

import cn.hellohao.exception.CodeException
import cn.hellohao.mapper.CodeMapper
import cn.hellohao.mapper.UserMapper
import cn.hellohao.model.entity.SysUser
import cn.hellohao.service.UserService
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service

@Service

class UserServiceImpl(  private val userMapper: UserMapper ,
                        private val codeMapper: CodeMapper ) : ServiceImpl<UserMapper, SysUser>(), UserService {

    override fun register(sysUser: SysUser): Int {
        // TODO Auto-generated method stub
        return userMapper.register(sysUser)
    }

    override fun login(email: String, password: String, uid: String): Int {
        // TODO Auto-generated method stub
        return userMapper.login(email, password, uid)
    }

    override fun getUsers(sysUser: SysUser): SysUser {
        // TODO Auto-generated method stub
        return userMapper.getUsers(sysUser)
    }

    override fun change(sysUser: SysUser): Int {
        // TODO Auto-generated method stub
        return userMapper.change(sysUser)
    }

    override fun changeUser(sysUser: SysUser): Int {
        return userMapper.changeUser(sysUser)
    }

    override fun checkUsername(username: String): Int {
        // TODO Auto-generated method stub
        return userMapper.checkUsername(username)
    }

    // TODO Auto-generated method stub
   fun getUserTotal(): Int{
       return   userMapper.userTotal()
   }



    override fun deleteUserById(id: String): Int {
        return userMapper.deleuser(id)
    }

    override fun countusername(username: String): Int {
        return userMapper.countusername(username)
    }

    override fun countmail(email: String): Int {
        return userMapper.countmail(email)
    }

    override fun getUserListByName(page: Page<SysUser>, username: String): Page<SysUser> {
        return userMapper.getUserList(page, username)
    }

    override fun getUserByUid(uid: String): Int {
        return userMapper.getUserByUid(uid)
    }

    override fun getUsersMail(uid: String): SysUser {
        return userMapper.getUsersMail(uid)
    }

    override fun setisok(sysUser: SysUser): Int {
        return userMapper.setIsok(sysUser)
    }

    override fun setmemory(sysUser: SysUser): Int {
        return userMapper.setmemory(sysUser)
    }

    override fun getUsersid(id: String): SysUser {
        return userMapper.getUserById(id)
    }

    override fun getuserlistforgroupid(groupid: String): List<SysUser> {
        return userMapper.getUserListFromGroupId(groupid)
    }

    /**
     * 默认遇到throw new RuntimeException(“…”);会回滚
     *
     * @param sysUser       用户
     * @param codestring codestring
     * @return [Integer]
     */
    @Transactional
    override fun usersetmemory(sysUser: SysUser, codestring: String): Int {
        var ret: Int = userMapper.changeUser(sysUser)
        ret = if (ret <= 0) {
            Print.warning("用户空间没有设置成功。回滚")
            throw CodeException("用户没有设置成功。")
        } else {
            codeMapper.deleteCode(codestring)
        }
        return ret
    }
}