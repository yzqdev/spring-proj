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
package me.zhengjie.modules.mnt.service

import me.zhengjie.modules.mnt.domain.ServerDeploy
import me.zhengjie.modules.mnt.service.dto.ServerDeployDto
import me.zhengjie.modules.mnt.service.dto.ServerDeployQueryCriteria
import org.springframework.data.domain.Pageable
import java.io.IOException
import javax.servlet.http.HttpServletResponse

/**
 * @author zhanghouying
 * @date 2019-08-24
 */
interface ServerDeployService {
    /**
     * 分页查询
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    fun queryAll(criteria: ServerDeployQueryCriteria?, pageable: Pageable?): Any

    /**
     * 查询全部数据
     * @param criteria 条件
     * @return /
     */
    fun queryAll(criteria: ServerDeployQueryCriteria?): List<ServerDeployDto?>?

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    fun findById(id: Long): ServerDeployDto?

    /**
     * 创建
     * @param resources /
     */
    fun create(resources: ServerDeploy)

    /**
     * 编辑
     * @param resources /
     */
    fun update(resources: ServerDeploy)

    /**
     * 删除
     * @param ids /
     */
    fun delete(ids: Set<Long>)

    /**
     * 根据IP查询
     * @param ip /
     * @return /
     */
    fun findByIp(ip: String?): ServerDeployDto?

    /**
     * 测试登录服务器
     * @param resources /
     * @return /
     */
    fun testConnect(resources: ServerDeploy): Boolean

    /**
     * 导出数据
     * @param queryAll /
     * @param response /
     * @throws IOException /
     */
    @Throws(IOException::class)
    fun download(queryAll: List<ServerDeployDto?>?, response: HttpServletResponse?)
}