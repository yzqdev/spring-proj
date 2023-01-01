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

import me.zhengjie.modules.mnt.domain.Deploy
import me.zhengjie.modules.mnt.domain.DeployHistory
import me.zhengjie.modules.mnt.service.dto.DeployDto
import me.zhengjie.modules.mnt.service.dto.DeployQueryCriteria
import org.springframework.data.domain.Pageable
import java.io.IOException
import javax.servlet.http.HttpServletResponse

/**
 * @author zhanghouying
 * @date 2019-08-24
 */
interface DeployService {
    /**
     * 分页查询
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    fun queryAll(criteria: DeployQueryCriteria?, pageable: Pageable?): Any

    /**
     * 查询全部数据
     * @param criteria 条件
     * @return /
     */
    fun queryAll(criteria: DeployQueryCriteria?): List<DeployDto?>?

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    fun findById(id: Long): DeployDto?

    /**
     * 创建
     * @param resources /
     */
    fun create(resources: Deploy)

    /**
     * 编辑
     * @param resources /
     */
    fun update(resources: Deploy)

    /**
     * 删除
     * @param ids /
     */
    fun delete(ids: Set<Long>)

    /**
     * 部署文件到服务器
     * @param fileSavePath 文件路径
     * @param appId 应用ID
     */
    fun deploy(fileSavePath: String, appId: Long)

    /**
     * 查询部署状态
     * @param resources /
     * @return /
     */
    fun serverStatus(resources: Deploy): String

    /**
     * 启动服务
     * @param resources /
     * @return /
     */
    fun startServer(resources: Deploy): String

    /**
     * 停止服务
     * @param resources /
     * @return /
     */
    fun stopServer(resources: Deploy): String

    /**
     * 停止服务
     * @param resources /
     * @return /
     */
    fun serverReduction(resources: DeployHistory): String

    /**
     * 导出数据
     * @param queryAll /
     * @param response /
     * @throws IOException /
     */
    @Throws(IOException::class)
    fun download(queryAll: List<DeployDto?>?, response: HttpServletResponse?)
}