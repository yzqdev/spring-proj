package com.kuang.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.kuang.mapper.UserRoleMapper
import com.kuang.model.entity.UserRole
import com.kuang.service.UserRoleService
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
@Service
class UserRoleServiceImpl : ServiceImpl<UserRoleMapper?, UserRole?>(), UserRoleService