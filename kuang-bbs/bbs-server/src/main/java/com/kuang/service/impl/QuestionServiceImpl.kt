package com.kuang.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.kuang.mapper.QuestionMapper
import com.kuang.model.entity.Question
import com.kuang.service.QuestionService
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
class QuestionServiceImpl : ServiceImpl<QuestionMapper?, Question?>(), QuestionService