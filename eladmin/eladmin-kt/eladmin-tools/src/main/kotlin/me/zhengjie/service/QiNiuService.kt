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

import me.zhengjie.domain.QiniuConfig
import me.zhengjie.domain.QiniuContent
import me.zhengjie.service.dto.QiniuQueryCriteria
import org.springframework.data.domain.Pageable
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import javax.servlet.http.HttpServletResponse

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
 * @date 2018-12-31
 */
interface QiNiuService {
    /**
     * 查配置
     * @return QiniuConfig
     */
    fun find(): QiniuConfig

    /**
     * 修改配置
     * @param qiniuConfig 配置
     * @return QiniuConfig
     */
    fun config(qiniuConfig: QiniuConfig): QiniuConfig

    /**
     * 分页查询
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    fun queryAll(criteria: QiniuQueryCriteria, pageable: Pageable?): Any?

    /**
     * 查询全部
     * @param criteria 条件
     * @return /
     */
    fun queryAll(criteria: QiniuQueryCriteria): List<QiniuContent?>

    /**
     * 上传文件
     * @param file 文件
     * @param qiniuConfig 配置
     * @return QiniuContent
     */
    fun upload(file: MultipartFile, qiniuConfig: QiniuConfig): QiniuContent

    /**
     * 查询文件
     * @param id 文件ID
     * @return QiniuContent
     */
    fun findByContentId(id: Long): QiniuContent

    /**
     * 下载文件
     * @param content 文件信息
     * @param config 配置
     * @return String
     */
    fun download(content: QiniuContent, config: QiniuConfig): String

    /**
     * 删除文件
     * @param content 文件
     * @param config 配置
     */
    fun delete(content: QiniuContent, config: QiniuConfig)

    /**
     * 同步数据
     * @param config 配置
     */
    fun synchronize(config: QiniuConfig)

    /**
     * 删除文件
     * @param ids 文件ID数组
     * @param config 配置
     */
    fun deleteAll(ids: Array<Long>, config: QiniuConfig)

    /**
     * 更新数据
     * @param type 类型
     */
    fun update(type: String?)

    /**
     * 导出数据
     * @param queryAll /
     * @param response /
     * @throws IOException /
     */
    @Throws(IOException::class)
    fun downloadList(queryAll: List<QiniuContent>, response: HttpServletResponse?)
}