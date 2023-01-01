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

import com.alibaba.fastjson.JSON
import com.qiniu.common.QiniuException
import com.qiniu.storage.BucketManager
import com.qiniu.storage.Configuration
import com.qiniu.storage.UploadManager
import com.qiniu.storage.model.DefaultPutRet
import com.qiniu.util.Auth
import lombok.RequiredArgsConstructor
import me.zhengjie.domain.QiniuConfig
import me.zhengjie.domain.QiniuContent
import me.zhengjie.exception.BadRequestException
import me.zhengjie.repository.QiNiuConfigRepository
import me.zhengjie.repository.QiniuContentRepository
import me.zhengjie.service.QiNiuService
import me.zhengjie.service.dto.QiniuQueryCriteria
import me.zhengjie.utils.FileUtil
import me.zhengjie.utils.PageUtil
import me.zhengjie.utils.QiNiuUtil.getKey
import me.zhengjie.utils.QiNiuUtil.getRegion
import me.zhengjie.utils.QueryHelp
import me.zhengjie.utils.ValidationUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
import javax.servlet.http.HttpServletResponse

/**
 * @author Zheng Jie
 * @date 2018-12-31
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = ["qiNiu"])
class QiNiuServiceImpl : QiNiuService {
    private val qiNiuConfigRepository: QiNiuConfigRepository? = null
    private val qiniuContentRepository: QiniuContentRepository? = null

    @Value("\${qiniu.max-size}")
    private val maxSize: Long? = null
    @Cacheable(key = "'config'")
    override fun find(): QiniuConfig {
        val qiniuConfig = qiNiuConfigRepository!!.findById(1L)
        return qiniuConfig.orElseGet { QiniuConfig() }
    }

    @CachePut(key = "'config'")
    @Transactional(rollbackFor = [Exception::class])
    override fun config(qiniuConfig: QiniuConfig): QiniuConfig {
        qiniuConfig.id = 1L
        val http = "http://"
        val https = "https://"
        if (!(qiniuConfig.host.lowercase(Locale.getDefault())
                .startsWith(http) || qiniuConfig.host.lowercase(Locale.getDefault()).startsWith(https))
        ) {
            throw BadRequestException("外链域名必须以http://或者https://开头")
        }
        return qiNiuConfigRepository!!.save(qiniuConfig)
    }

    override fun queryAll(criteria: QiniuQueryCriteria, pageable: Pageable?): Any? {
        return PageUtil.toPage(qiniuContentRepository!!.findAll({ root: Root<QiniuContent>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
            QueryHelp.getPredicate(
                root,
                criteria,
                criteriaBuilder
            )
        }, pageable!!))
    }

    override fun queryAll(criteria: QiniuQueryCriteria): List<QiniuContent?> {
        return qiniuContentRepository!!.findAll { root: Root<QiniuContent>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
            QueryHelp.getPredicate(
                root,
                criteria,
                criteriaBuilder
            )
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun upload(file: MultipartFile, qiniuConfig: QiniuConfig): QiniuContent {
        FileUtil.checkSize(maxSize!!, file.size)
        if (qiniuConfig.id == null) {
            throw BadRequestException("请先添加相应配置，再操作")
        }
        // 构造一个带指定Zone对象的配置类
        val cfg = Configuration(getRegion(qiniuConfig.zone))
        val uploadManager = UploadManager(cfg)
        val auth = Auth.create(qiniuConfig.accessKey, qiniuConfig.secretKey)
        val upToken = auth.uploadToken(qiniuConfig.bucket)
        return try {
            var key = file.originalFilename
            if (qiniuContentRepository!!.findByKey(key) != null) {
                key = getKey(key)
            }
            val response = uploadManager.put(file.bytes, key, upToken)
            //解析上传成功的结果
            val putRet = JSON.parseObject(response.bodyString(), DefaultPutRet::class.java)
            val content = qiniuContentRepository.findByKey(FileUtil.getFileNameNoEx(putRet.key))
            if (content == null) {
                //存入数据库
                val qiniuContent = QiniuContent()
                qiniuContent.suffix = FileUtil.getExtensionName(putRet.key)
                qiniuContent.bucket = qiniuConfig.bucket
                qiniuContent.type = qiniuConfig.type
                qiniuContent.key = FileUtil.getFileNameNoEx(putRet.key)
                qiniuContent.url = qiniuConfig.host + "/" + putRet.key
                qiniuContent.size = FileUtil.getSize(
                    (file.size.toString() + "").toInt().toLong()
                )
                return qiniuContentRepository.save(qiniuContent)
            }
            content
        } catch (e: Exception) {
            throw BadRequestException(e.message)
        }
    }

    override fun findByContentId(id: Long): QiniuContent {
        val qiniuContent = qiniuContentRepository!!.findById(id).orElseGet { QiniuContent() }
        ValidationUtil.isNull(qiniuContent.id, "QiniuContent", "id", id)
        return qiniuContent
    }

    override fun download(content: QiniuContent, config: QiniuConfig): String {
        val finalUrl: String
        val type = "公开"
        finalUrl = if (type == content.type) {
            content.url
        } else {
            val auth = Auth.create(config.accessKey, config.secretKey)
            // 1小时，可以自定义链接过期时间
            val expireInSeconds: Long = 3600
            auth.privateDownloadUrl(content.url, expireInSeconds)
        }
        return finalUrl
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun delete(content: QiniuContent, config: QiniuConfig) {
        //构造一个带指定Zone对象的配置类
        val cfg = Configuration(getRegion(config.zone))
        val auth = Auth.create(config.accessKey, config.secretKey)
        val bucketManager = BucketManager(auth, cfg)
        try {
            bucketManager.delete(content.bucket, content.key + "." + content.suffix)
            qiniuContentRepository!!.delete(content)
        } catch (ex: QiniuException) {
            qiniuContentRepository!!.delete(content)
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun synchronize(config: QiniuConfig) {
        if (config.id == null) {
            throw BadRequestException("请先添加相应配置，再操作")
        }
        //构造一个带指定Zone对象的配置类
        val cfg = Configuration(getRegion(config.zone))
        val auth = Auth.create(config.accessKey, config.secretKey)
        val bucketManager = BucketManager(auth, cfg)
        //文件名前缀
        val prefix = ""
        //每次迭代的长度限制，最大1000，推荐值 1000
        val limit = 1000
        //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        val delimiter = ""
        //列举空间文件列表
        val fileListIterator = bucketManager.createFileListIterator(config.bucket, prefix, limit, delimiter)
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            var qiniuContent: QiniuContent
            val items = fileListIterator.next()
            for (item in items) {
                if (qiniuContentRepository!!.findByKey(FileUtil.getFileNameNoEx(item.key)) == null) {
                    qiniuContent = QiniuContent()
                    qiniuContent.size = FileUtil.getSize(
                        (item.fsize.toString() + "").toInt().toLong()
                    )
                    qiniuContent.suffix = FileUtil.getExtensionName(item.key)
                    qiniuContent.key = FileUtil.getFileNameNoEx(item.key)
                    qiniuContent.type = config.type
                    qiniuContent.bucket = config.bucket
                    qiniuContent.url = config.host + "/" + item.key
                    qiniuContentRepository.save(qiniuContent)
                }
            }
        }
    }

    override fun deleteAll(ids: Array<Long>, config: QiniuConfig) {
        for (id in ids) {
            delete(findByContentId(id), config)
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun update(type: String?) {
        qiNiuConfigRepository!!.update(type)
    }

    @Throws(IOException::class)
    override fun downloadList(queryAll: List<QiniuContent>, response: HttpServletResponse?) {
        val list: MutableList<Map<String, Any>> = ArrayList()
        for (content in queryAll) {
            val map: MutableMap<String, Any> = LinkedHashMap()
            map["文件名"] = content.key
            map["文件类型"] = content.suffix
            map["空间名称"] = content.bucket
            map["文件大小"] = content.size
            map["空间类型"] = content.type
            map["创建日期"] = content.updateTime
            list.add(map)
        }
        FileUtil.downloadExcel(list, response)
    }
}