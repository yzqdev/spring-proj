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
import me.zhengjie.modules.mnt.domain.DeployHistory
import me.zhengjie.modules.mnt.repository.DeployHistoryRepository
import me.zhengjie.modules.mnt.service.DeployHistoryService
import me.zhengjie.modules.mnt.service.dto.DeployHistoryDto
import me.zhengjie.modules.mnt.service.dto.DeployHistoryQueryCriteria
import me.zhengjie.modules.mnt.service.mapstruct.DeployHistoryMapper
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
@Service
@RequiredArgsConstructor
class DeployHistoryServiceImpl : DeployHistoryService {
    private val deployhistoryRepository: DeployHistoryRepository? = null
    private val deployhistoryMapper: DeployHistoryMapper? = null
    override fun queryAll(criteria: DeployHistoryQueryCriteria?, pageable: Pageable?): Any {
        val page =
            deployhistoryRepository.findAll(Specification { root: Root<DeployHistory>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
                getPredicate(
                    root,
                    criteria,
                    criteriaBuilder!!
                )
            }, pageable)
        return toPage(page.map { entity: DeployHistory -> deployhistoryMapper!!.toDto(entity) })
    }

    override fun queryAll(criteria: DeployHistoryQueryCriteria?): List<DeployHistoryDto?>? {
        return deployhistoryMapper.toDto(deployhistoryRepository.findAll(Specification { root: Root<DeployHistory>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
            getPredicate(
                root,
                criteria,
                criteriaBuilder!!
            )
        }))
    }

    override fun findById(id: String): DeployHistoryDto? {
        val deployhistory = deployhistoryRepository!!.findById(id).orElseGet { DeployHistory() }!!
        isNull(deployhistory.id, "DeployHistory", "id", id)
        return deployhistoryMapper!!.toDto(deployhistory)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun create(resources: DeployHistory) {
        resources.id = IdUtil.simpleUUID()
        deployhistoryRepository!!.save(resources)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun delete(ids: Set<String>) {
        for (id in ids) {
            deployhistoryRepository!!.deleteById(id)
        }
    }

    @Throws(IOException::class)
    override fun download(queryAll: List<DeployHistoryDto?>?, response: HttpServletResponse?) {
        val list: MutableList<Map<String?, Any?>?> = ArrayList()
        for (deployHistoryDto in queryAll!!) {
            val map: MutableMap<String?, Any?> = LinkedHashMap()
            map["部署编号"] = deployHistoryDto.getDeployId()
            map["应用名称"] = deployHistoryDto.getAppName()
            map["部署IP"] = deployHistoryDto.getIp()
            map["部署时间"] = deployHistoryDto.getDeployDate()
            map["部署人员"] = deployHistoryDto.getDeployUser()
            list.add(map)
        }
        downloadExcel(list, response!!)
    }
}