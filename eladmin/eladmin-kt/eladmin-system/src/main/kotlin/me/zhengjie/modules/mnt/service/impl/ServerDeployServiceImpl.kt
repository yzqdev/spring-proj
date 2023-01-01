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

import lombok.RequiredArgsConstructor
import me.zhengjie.modules.mnt.domain.ServerDeploy
import me.zhengjie.modules.mnt.repository.ServerDeployRepository
import me.zhengjie.modules.mnt.service.ServerDeployService
import me.zhengjie.modules.mnt.service.dto.ServerDeployDto
import me.zhengjie.modules.mnt.service.dto.ServerDeployQueryCriteria
import me.zhengjie.modules.mnt.service.mapstruct.ServerDeployMapper
import me.zhengjie.modules.mnt.util.ExecuteShellUtil
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
class ServerDeployServiceImpl : ServerDeployService {
    private val serverDeployRepository: ServerDeployRepository? = null
    private val serverDeployMapper: ServerDeployMapper? = null
    override fun queryAll(criteria: ServerDeployQueryCriteria?, pageable: Pageable?): Any {
        val page = serverDeployRepository.findAll(
            Specification { root: Root<ServerDeploy>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
                getPredicate(
                    root,
                    criteria,
                    criteriaBuilder!!
                )
            }, pageable
        )
        return toPage(page.map { entity: ServerDeploy -> serverDeployMapper!!.toDto(entity) })
    }

    override fun queryAll(criteria: ServerDeployQueryCriteria?): List<ServerDeployDto?>? {
        return serverDeployMapper.toDto(serverDeployRepository.findAll(Specification { root: Root<ServerDeploy>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
            getPredicate(
                root,
                criteria,
                criteriaBuilder!!
            )
        }))
    }

    override fun findById(id: Long): ServerDeployDto? {
        val server = serverDeployRepository!!.findById(id).orElseGet { ServerDeploy() }!!
        isNull(server.id, "ServerDeploy", "id", id)
        return serverDeployMapper!!.toDto(server)
    }

    override fun findByIp(ip: String?): ServerDeployDto? {
        val deploy = serverDeployRepository!!.findByIp(ip)
        return serverDeployMapper!!.toDto(deploy)
    }

    override fun testConnect(resources: ServerDeploy): Boolean {
        var executeShellUtil: ExecuteShellUtil? = null
        return try {
            executeShellUtil = ExecuteShellUtil(resources.ip, resources.account, resources.password, resources.port)
            executeShellUtil.execute("ls") == 0
        } catch (e: Exception) {
            false
        } finally {
            executeShellUtil?.close()
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun create(resources: ServerDeploy) {
        serverDeployRepository!!.save(resources)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun update(resources: ServerDeploy) {
        val serverDeploy = serverDeployRepository!!.findById(resources.id).orElseGet { ServerDeploy() }!!
        isNull(serverDeploy.id, "ServerDeploy", "id", resources.id)
        serverDeploy.copy(resources)
        serverDeployRepository.save(serverDeploy)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun delete(ids: Set<Long>) {
        for (id in ids) {
            serverDeployRepository!!.deleteById(id)
        }
    }

    @Throws(IOException::class)
    override fun download(queryAll: List<ServerDeployDto?>?, response: HttpServletResponse?) {
        val list: MutableList<Map<String?, Any?>?> = ArrayList()
        for (deployDto in queryAll!!) {
            val map: MutableMap<String?, Any?> = LinkedHashMap()
            map["服务器名称"] = deployDto.getName()
            map["服务器IP"] = deployDto.getIp()
            map["端口"] = deployDto.getPort()
            map["账号"] = deployDto.getAccount()
            map["创建日期"] = deployDto.getCreateTime()
            list.add(map)
        }
        downloadExcel(list, response!!)
    }
}