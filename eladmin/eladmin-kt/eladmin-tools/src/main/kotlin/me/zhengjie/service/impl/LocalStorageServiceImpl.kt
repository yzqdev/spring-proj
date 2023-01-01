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

import cn.hutool.core.util.ObjectUtil
import lombok.RequiredArgsConstructor
import me.zhengjie.config.FileProperties
import me.zhengjie.domain.LocalStorage
import me.zhengjie.exception.BadRequestException
import me.zhengjie.repository.LocalStorageRepository
import me.zhengjie.service.LocalStorageService
import me.zhengjie.service.dto.LocalStorageDto
import me.zhengjie.service.dto.LocalStorageQueryCriteria
import me.zhengjie.service.mapstruct.LocalStorageMapper
import me.zhengjie.utils.*
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
import javax.servlet.http.HttpServletResponse

/**
 * @author Zheng Jie
 * @date 2019-09-05
 */
@Service
@RequiredArgsConstructor
class LocalStorageServiceImpl : LocalStorageService {
    private val localStorageRepository: LocalStorageRepository? = null
    private val localStorageMapper: LocalStorageMapper? = null
    private val properties: FileProperties? = null
    override fun queryAll(criteria: LocalStorageQueryCriteria, pageable: Pageable?): Any? {
        val page =
            localStorageRepository!!.findAll({ root: Root<LocalStorage>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
                QueryHelp.getPredicate(
                    root,
                    criteria,
                    criteriaBuilder
                )
            }, pageable!!)
        return PageUtil.toPage(page.map { entity: LocalStorage -> localStorageMapper!!.toDto(entity) })
    }

    override fun queryAll(criteria: LocalStorageQueryCriteria): List<LocalStorageDto?>? {
        return localStorageMapper!!.toDto(localStorageRepository!!.findAll { root: Root<LocalStorage>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
            QueryHelp.getPredicate(
                root,
                criteria,
                criteriaBuilder
            )
        })
    }

    override fun findById(id: Long): LocalStorageDto? {
        val localStorage = localStorageRepository!!.findById(id).orElseGet { LocalStorage() }
        ValidationUtil.isNull(localStorage.id, "LocalStorage", "id", id)
        return localStorageMapper!!.toDto(localStorage)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun create(name: String?, multipartFile: MultipartFile): LocalStorage {
        var name = name
        FileUtil.checkSize(properties!!.maxSize, multipartFile.size)
        val suffix = FileUtil.getExtensionName(multipartFile.originalFilename)
        val type = FileUtil.getFileType(suffix)
        val file = FileUtil.upload(multipartFile, properties.path.path + type + File.separator)
        if (ObjectUtil.isNull(file)) {
            throw BadRequestException("上传失败")
        }
        return try {
            name = if (StringUtils.isBlank(name)) FileUtil.getFileNameNoEx(multipartFile.originalFilename) else name
            val localStorage = LocalStorage(
                file.name,
                name,
                suffix,
                file.path,
                type,
                FileUtil.getSize(multipartFile.size)
            )
            localStorageRepository!!.save(localStorage)
        } catch (e: Exception) {
            FileUtil.del(file)
            throw e
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun update(resources: LocalStorage) {
        val localStorage = localStorageRepository!!.findById(resources.id).orElseGet { LocalStorage() }
        ValidationUtil.isNull(localStorage.id, "LocalStorage", "id", resources.id)
        localStorage.copy(resources)
        localStorageRepository.save(localStorage)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun deleteAll(ids: Array<Long>) {
        for (id in ids) {
            val storage = localStorageRepository!!.findById(id).orElseGet { LocalStorage() }
            FileUtil.del(storage.path)
            localStorageRepository.delete(storage)
        }
    }

    @Throws(IOException::class)
    override fun download(queryAll: List<LocalStorageDto>, response: HttpServletResponse?) {
        val list: MutableList<Map<String, Any>> = ArrayList()
        for (localStorageDTO in queryAll) {
            val map: MutableMap<String, Any> = LinkedHashMap()
            map["文件名"] = localStorageDTO.realName
            map["备注名"] = localStorageDTO.name
            map["文件类型"] = localStorageDTO.type
            map["文件大小"] = localStorageDTO.size
            map["创建者"] = localStorageDTO.createBy
            map["创建日期"] = localStorageDTO.createTime
            list.add(map)
        }
        FileUtil.downloadExcel(list, response)
    }
}