package com.kuang.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.kuang.mapper.CommentMapper
import com.kuang.model.entity.Comment
import com.kuang.service.CommentService
import org.springframework.stereotype.Service

/**
 *
 *
 * 服务实现类
 *
 *
 * @author 遇见狂神说
 * @since 2020-06-30
 */
@Service
class CommentServiceImpl : ServiceImpl<CommentMapper?, Comment?>(), CommentService