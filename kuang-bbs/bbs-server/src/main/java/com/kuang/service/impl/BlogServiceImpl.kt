package com.kuang.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.kuang.mapper.BlogMapper
import com.kuang.model.entity.Blog
import com.kuang.service.BlogService
import org.springframework.stereotype.Service

/**
 *
 *
 * 服务实现类
 *
 *
 * @author 遇见狂神说
 * @since 2020-06-29
 */
@Service
class BlogServiceImpl : ServiceImpl<BlogMapper?, Blog?>(), BlogService