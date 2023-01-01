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
package me.zhengjie.modules.system.service.impl

import cn.hutool.core.collection.CollectionUtil
import lombok.RequiredArgsConstructor
import me.zhengjie.modules.system.domain.Dict
import me.zhengjie.modules.system.repository.DictRepository
import me.zhengjie.modules.system.service.DictService
import me.zhengjie.modules.system.service.dto.DictDto
import me.zhengjie.modules.system.service.dto.DictQueryCriteria
import me.zhengjie.modules.system.service.mapstruct.DictMapper
import me.zhengjie.utils.*
import me.zhengjie.utils.FileUtil.downloadExcel
import me.zhengjie.utils.PageUtil.toPage
import me.zhengjie.utils.QueryHelp.getPredicate
import me.zhengjie.utils.ValidationUtil.isNull
import org.springframework.cache.annotation.CacheConfig
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
import javax.servlet.http.HttpServletResponse

/**
 * @author Zheng Jie
 * @date 2019-04-10
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = ["dict"])
class DictServiceImpl : DictService {
    private val dictRepository: DictRepository? = null
    private val dictMapper: DictMapper? = null
    private val redisUtils: RedisUtils? = null
    override fun queryAll(dict: DictQueryCriteria?, pageable: Pageable?): Map<String?, Any?> {
        val page = dictRepository!!.findAll(
            { root: Root<Dict>?, query: CriteriaQuery<*>?, cb: CriteriaBuilder? -> getPredicate(root, dict, cb!!) },
            pageable!!
        )
        return toPage(page.map { entity: Dict -> dictMapper!!.toDto(entity) })
    }

    override fun queryAll(dict: DictQueryCriteria?): List<DictDto?>? {
        val list = dictRepository!!.findAll { root: Root<Dict>?, query: CriteriaQuery<*>?, cb: CriteriaBuilder? ->
            getPredicate(
                root,
                dict,
                cb!!
            )
        }
        return dictMapper!!.toDto(list)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun create(resources: Dict) {
        dictRepository!!.save(resources)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun update(resources: Dict) {
        // 清理缓存
        delCaches(resources)
        val dict = dictRepository!!.findById(resources.id).orElseGet { Dict() }
        isNull(dict.id, "Dict", "id", resources.id)
        dict.name = resources.name
        dict.description = resources.description
        dictRepository.save(dict)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun delete(ids: Set<Long?>?) {
        // 清理缓存
        val dicts = dictRepository!!.findByIdIn(ids)
        for (dict in dicts) {
            delCaches(dict)
        }
        dictRepository.deleteByIdIn(ids)
    }

    @Throws(IOException::class)
    override fun download(dictDtos: List<DictDto>, response: HttpServletResponse?) {
        val list: MutableList<Map<String?, Any?>?> = ArrayList()
        for (dictDTO in dictDtos) {
            if (CollectionUtil.isNotEmpty(dictDTO.dictDetails)) {
                for (dictDetail in dictDTO.dictDetails) {
                    val map: MutableMap<String?, Any?> = LinkedHashMap()
                    map["字典名称"] = dictDTO.name
                    map["字典描述"] = dictDTO.description
                    map["字典标签"] = dictDetail.label
                    map["字典值"] = dictDetail.value
                    map["创建日期"] = dictDetail.getCreateTime()
                    list.add(map)
                }
            } else {
                val map: MutableMap<String?, Any?> = LinkedHashMap()
                map["字典名称"] = dictDTO.name
                map["字典描述"] = dictDTO.description
                map["字典标签"] = null
                map["字典值"] = null
                map["创建日期"] = dictDTO.getCreateTime()
                list.add(map)
            }
        }
        downloadExcel(list, response!!)
    }

    fun delCaches(dict: Dict) {
        redisUtils!!.del(CacheKey.DICT_NAME + dict.name)
    }
}