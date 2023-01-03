package com.kuang.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.kuang.mapper.InviteMapper
import com.kuang.model.entity.Invite
import com.kuang.service.InviteService
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
class InviteServiceImpl : ServiceImpl<InviteMapper?, Invite?>(), InviteService