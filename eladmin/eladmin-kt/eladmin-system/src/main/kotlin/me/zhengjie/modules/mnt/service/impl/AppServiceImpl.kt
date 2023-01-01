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

import me.zhengjie.exception.BadRequestException
import me.zhengjie.modules.mnt.domain.App
import me.zhengjie.modules.mnt.repository.AppRepository
import me.zhengjie.modules.mnt.service.*
import me.zhengjie.modules.mnt.service.dto.AppDto
import me.zhengjie.modules.mnt.service.dto.AppQueryCriteria
import me.zhengjie.modules.mnt.service.mapstruct.AppMapper
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
class AppServiceImpl(private val appRepository: AppRepository, private val appMapper: AppMapper) : AppService {
    override fun queryAll(criteria: AppQueryCriteria?, pageable: Pageable?): Any {
        val page = appRepository.findAll(
            Specification { root: Root<App>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
                getPredicate(
                    root,
                    criteria,
                    criteriaBuilder!!
                )
            }, pageable
        )
        return toPage(page.map { entity: App -> appMapper.toDto(entity) })
    }

    override fun queryAll(criteria: AppQueryCriteria?): List<AppDto?>? {
        return appMapper.toDto(appRepository.findAll { root: Root<App>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
            getPredicate(
                root,
                criteria,
                criteriaBuilder!!
            )
        })
    }

    override fun findById(id: Long): AppDto? {
        val app = appRepository.findById(id).orElseGet { App() }!!
        isNull(app.id, "App", "id", id)
        return appMapper.toDto(app)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun create(resources: App) {
        verification(resources)
        appRepository.save(resources)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun update(resources: App) {
        verification(resources)
        val app = appRepository.findById(resources.id).orElseGet { App() }!!
        isNull(app.id, "App", "id", resources.id)
        app.copy(resources)
        appRepository.save(app)
    }

    private fun verification(resources: App) {
        val opt = "/opt"
        val home = "/home"
        if (!(resources.uploadPath.startsWith(opt) || resources.uploadPath.startsWith(home))) {
            throw BadRequestException("文件只能上传在opt目录或者home目录 ")
        }
        if (!(resources.deployPath.startsWith(opt) || resources.deployPath.startsWith(home))) {
            throw BadRequestException("文件只能部署在opt目录或者home目录 ")
        }
        if (!(resources.backupPath.startsWith(opt) || resources.backupPath.startsWith(home))) {
            throw BadRequestException("文件只能备份在opt目录或者home目录 ")
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun delete(ids: Set<Long>) {
        for (id in ids) {
            appRepository.deleteById(id)
        }
    }

    @Throws(IOException::class)
    override fun download(queryAll: List<AppDto?>?, response: HttpServletResponse?) {
        val list: MutableList<Map<String?, Any?>?> = ArrayList()
        for (appDto in queryAll!!) {
            val map: MutableMap<String?, Any?> = LinkedHashMap()
            map["应用名称"] = appDto.getName()
            map["端口"] = appDto.getPort()
            map["上传目录"] = appDto.getUploadPath()
            map["部署目录"] = appDto.getDeployPath()
            map["备份目录"] = appDto.getBackupPath()
            map["启动脚本"] = appDto.getStartScript()
            map["部署脚本"] = appDto.getDeployScript()
            map["创建日期"] = appDto.getCreateTime()
            list.add(map)
        }
        downloadExcel(list, response!!)
    }
}