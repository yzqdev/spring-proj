package com.kuang.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.kuang.mapper.UserMapper
import com.kuang.model.entity.User
import com.kuang.service.UserService
import org.springframework.stereotype.Service

/**
 *
 *
 * 服务实现类
 *
 *
 * @author 遇见狂神说
 * @since 2020-06-28
 */
// UserDetailsService接口用于返回用户相关数据。
// 它有loadUserByUsername()方法，根据username查询用户实体，可以实现该接口覆盖该方法，实现自定义获取用户过程。
// 该接口实现类被DaoAuthenticationProvider 类使用，用于认证过程中载入用户信息。
@Service
class UserServiceImpl : ServiceImpl<UserMapper?, User?>(), UserService