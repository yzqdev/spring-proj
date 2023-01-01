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
package me.zhengjie.modules.mnt.service.dto

import lombok.Getter
import lombok.Setter
import me.zhengjie.base.BaseDTO
import java.io.Serializable

/**
 * @author zhanghouying
 * @date 2019-08-24
 */
@Getter
@Setter
class AppDto : BaseDTO(), Serializable {
    /**
     * 应用编号
     */
    private val id: Long? = null

    /**
     * 应用名称
     */
    private val name: String? = null

    /**
     * 端口
     */
    private val port: Int? = null

    /**
     * 上传目录
     */
    private val uploadPath: String? = null

    /**
     * 部署目录
     */
    private val deployPath: String? = null

    /**
     * 备份目录
     */
    private val backupPath: String? = null

    /**
     * 启动脚本
     */
    private val startScript: String? = null

    /**
     * 部署脚本
     */
    private val deployScript: String? = null
}