package com.kuang.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.kuang.mapper.SayMapper
import com.kuang.model.entity.Say
import com.kuang.service.SayService
import org.springframework.stereotype.Service

/**
 *
 *
 * 服务实现类
 *
 *
 * @author 遇见狂神说
 * @since 2020-07-01
 */
@Service
class SayServiceImpl : ServiceImpl<SayMapper?, Say?>(), SayService