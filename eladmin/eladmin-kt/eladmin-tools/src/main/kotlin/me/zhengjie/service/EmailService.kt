/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.serviceimport

import me.zhengjie.domain.EmailConfig
import me.zhengjie.domain.vo.EmailVo

me.zhengjie.utils.QiNiuUtil.getRegion
import me.zhengjie.utils.QiNiuUtil.getKey
import me.zhengjie.base.BaseDTO
import lombok.RequiredArgsConstructor
import org.springframework.cache.annotation.CacheConfig
import me.zhengjie.service.EmailService
import me.zhengjie.repository.EmailRepository
import org.springframework.cache.annotation.CachePut
import me.zhengjie.domain.EmailConfig
import me.zhengjie.utils.EncryptUtils
import me.zhengjie.domain.vo.EmailVo
import me.zhengjie.exception.BadRequestException
import cn.hutool.extra.mail.MailAccount
import cn.hutool.extra.mail.Mail
import me.zhengjie.service.QiNiuService
import me.zhengjie.repository.QiNiuConfigRepository
import me.zhengjie.repository.QiniuContentRepository
import me.zhengjie.domain.QiniuConfig
import me.zhengjie.service.dto.QiniuQueryCriteria
import me.zhengjie.domain.QiniuContent
import javax.persistence.criteria.CriteriaBuilder
import me.zhengjie.utils.QueryHelp
import org.springframework.web.multipart.MultipartFile
import me.zhengjie.utils.QiNiuUtil
import com.qiniu.storage.UploadManager
import com.qiniu.util.Auth
import com.qiniu.storage.model.DefaultPutRet
import com.qiniu.storage.BucketManager
import com.qiniu.common.QiniuException
import com.qiniu.storage.BucketManager.FileListIterator
import javax.servlet.http.HttpServletResponse
import me.zhengjie.service.AliPayService
import me.zhengjie.repository.AliPayRepository
import me.zhengjie.domain.vo.TradeVo
import com.alipay.api.AlipayClient
import com.alipay.api.DefaultAlipayClient
import com.alipay.api.request.AlipayTradePagePayRequest
import com.alipay.api.response.AlipayTradePagePayResponse
import com.alipay.api.request.AlipayTradeWapPayRequest
import com.alipay.api.response.AlipayTradeWapPayResponse
import me.zhengjie.service.LocalStorageService
import me.zhengjie.repository.LocalStorageRepository
import me.zhengjie.service.mapstruct.LocalStorageMapper
import me.zhengjie.config.FileProperties
import me.zhengjie.service.dto.LocalStorageQueryCriteria
import me.zhengjie.domain.LocalStorage
import me.zhengjie.service.dto.LocalStorageDto
import org.mapstruct.ReportingPolicy
import me.zhengjie.base.BaseMapper

/**
 * @author Zheng Jie
 * @date 2018-12-26
 */
interface EmailService {
    /**
     * 更新邮件配置
     * @param emailConfig 邮箱配置
     * @param old /
     * @return /
     * @throws Exception /
     */
    @Throws(Exception::class)
    fun config(emailConfig: EmailConfig, old: EmailConfig): EmailConfig

    /**
     * 查询配置
     * @return EmailConfig 邮件配置
     */
    fun find(): EmailConfig

    /**
     * 发送邮件
     * @param emailVo 邮件发送的内容
     * @param emailConfig 邮件配置
     * @throws Exception /
     */
    fun send(emailVo: EmailVo, emailConfig: EmailConfig)
}