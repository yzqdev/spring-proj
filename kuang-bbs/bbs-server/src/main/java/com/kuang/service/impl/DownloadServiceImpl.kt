package com.kuang.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.kuang.mapper.DownloadMapper
import com.kuang.model.entity.Download
import com.kuang.service.DownloadService
import org.springframework.stereotype.Service

/**
 *
 *
 * 服务实现类
 *
 *
 * @author 遇见狂神说
 * @since 2020-07-08
 */
@Service
class DownloadServiceImpl : ServiceImpl<DownloadMapper?, Download?>(), DownloadService