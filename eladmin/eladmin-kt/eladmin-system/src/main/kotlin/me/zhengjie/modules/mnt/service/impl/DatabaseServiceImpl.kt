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
package me.zhengjie.modules.mnt.service.impl

import cn.hutool.core.util.IdUtil
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import me.zhengjie.modules.mnt.domain.Database
import me.zhengjie.modules.mnt.repository.DatabaseRepository
import me.zhengjie.modules.mnt.service.DatabaseService
import me.zhengjie.modules.mnt.service.dto.DatabaseDto
import me.zhengjie.modules.mnt.service.dto.DatabaseQueryCriteria
import me.zhengjie.modules.mnt.service.mapstruct.DatabaseMapper
import me.zhengjie.modules.mnt.util.SqlUtils
import me.zhengjie.utils.FileUtil.downloadExcel
import me.zhengjie.utils.PageUtil.toPage
import me.zhengjie.utils.QueryHelp.getPredicate
import me.zhengjie.utils.ValidationUtil.isNull
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
import javax.servlet.http.HttpServletResponse

/**
 * @author zhanghouying
 * @date 2019-08-24
 */
@Slf4j
@Service
@RequiredArgsConstructor
class DatabaseServiceImpl : DatabaseService {
    private val databaseRepository: DatabaseRepository? = null
    private val databaseMapper: DatabaseMapper? = null
    override fun queryAll(criteria: DatabaseQueryCriteria?, pageable: Pageable?): Any {
        val page = databaseRepository.findAll(
            Specification { root: Root<Database>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
                getPredicate(
                    root,
                    criteria,
                    criteriaBuilder!!
                )
            }, pageable
        )
        return toPage(page.map { entity: Database -> databaseMapper!!.toDto(entity) })
    }

    override fun queryAll(criteria: DatabaseQueryCriteria?): List<DatabaseDto?>? {
        return databaseMapper.toDto(databaseRepository.findAll(Specification { root: Root<Database>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
            getPredicate(
                root,
                criteria,
                criteriaBuilder!!
            )
        }))
    }

    override fun findById(id: String): DatabaseDto? {
        val database = databaseRepository!!.findById(id).orElseGet { Database() }!!
        isNull(database.id, "Database", "id", id)
        return databaseMapper!!.toDto(database)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun create(resources: Database) {
        resources.id = IdUtil.simpleUUID()
        databaseRepository!!.save(resources)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun update(resources: Database) {
        val database = databaseRepository!!.findById(resources.id).orElseGet { Database() }!!
        isNull(database.id, "Database", "id", resources.id)
        database.copy(resources)
        databaseRepository.save(database)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun delete(ids: Set<String>) {
        for (id in ids) {
            databaseRepository!!.deleteById(id)
        }
    }

    override fun testConnection(resources: Database): Boolean {
        return try {
            SqlUtils.testConnection(resources.jdbcUrl, resources.userName, resources.pwd)
        } catch (e: Exception) {
            DatabaseServiceImpl.log.error(e.message)
            false
        }
    }

    @Throws(IOException::class)
    override fun download(queryAll: List<DatabaseDto?>?, response: HttpServletResponse?) {
        val list: MutableList<Map<String?, Any?>?> = ArrayList()
        for (databaseDto in queryAll!!) {
            val map: MutableMap<String?, Any?> = LinkedHashMap()
            map["数据库名称"] = databaseDto.getName()
            map["数据库连接地址"] = databaseDto.getJdbcUrl()
            map["用户名"] = databaseDto.getUserName()
            map["创建日期"] = databaseDto.getCreateTime()
            list.add(map)
        }
        downloadExcel(list, response!!)
    }
}