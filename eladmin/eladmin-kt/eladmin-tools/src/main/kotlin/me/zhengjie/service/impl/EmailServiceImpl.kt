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
package me.zhengjie.service.impl

import cn.hutool.extra.mail.Mail
import cn.hutool.extra.mail.MailAccount
import lombok.RequiredArgsConstructor
import me.zhengjie.domain.EmailConfig
import me.zhengjie.domain.vo.EmailVo
import me.zhengjie.exception.BadRequestException
import me.zhengjie.repository.EmailRepository
import me.zhengjie.service.EmailService
import me.zhengjie.utils.EncryptUtils
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Zheng Jie
 * @date 2018-12-26
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = ["email"])
class EmailServiceImpl : EmailService {
    private val emailRepository: EmailRepository? = null

    @CachePut(key = "'config'")
    @Transactional(rollbackFor = [Exception::class])
    @Throws(
        Exception::class
    )
    override fun config(emailConfig: EmailConfig, old: EmailConfig): EmailConfig {
        emailConfig.id = 1L
        if (emailConfig.pass != old.pass) {
            // 对称加密
            emailConfig.pass = EncryptUtils.desEncrypt(emailConfig.pass)
        }
        return emailRepository!!.save(emailConfig)
    }

    @Cacheable(key = "'config'")
    override fun find(): EmailConfig {
        val emailConfig = emailRepository!!.findById(1L)
        return emailConfig.orElseGet { EmailConfig() }
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun send(emailVo: EmailVo, emailConfig: EmailConfig) {
        if (emailConfig.id == null) {
            throw BadRequestException("请先配置，再操作")
        }
        // 封装
        val account = MailAccount()
        // 设置用户
        val user = emailConfig.fromUser.split("@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        account.user = user
        account.host = emailConfig.host
        account.port = emailConfig.port.toInt()
        account.isAuth = true
        try {
            // 对称解密
            account.pass = EncryptUtils.desDecrypt(emailConfig.pass)
        } catch (e: Exception) {
            throw BadRequestException(e.message)
        }
        account.from = emailConfig.user + "<" + emailConfig.fromUser + ">"
        // ssl方式发送
        account.isSslEnable = true
        // 使用STARTTLS安全连接
        account.isStarttlsEnable = true
        val content = emailVo.content
        // 发送
        try {
            val size = emailVo.tos.size
            Mail.create(account)
                .setTos(*emailVo.tos.toTypedArray())
                .setTitle(emailVo.subject)
                .setContent(content)
                .setHtml(true) //关闭session
                .setUseGlobalSession(false)
                .send()
        } catch (e: Exception) {
            throw BadRequestException(e.message)
        }
    }
}