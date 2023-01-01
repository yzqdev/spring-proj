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
package me.zhengjie.domain

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Data
import org.hibernate.annotations.UpdateTimestamp
import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.*

/**
 * 上传成功后，存储结果
 * @author Zheng Jie
 * @date 2018-12-31
 */
@Data
@Entity
@Table(name = "tool_qiniu_content")
class QiniuContent : Serializable {
    @Id
    @Column(name = "content_id")
    @Schema(name = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    @Column(name = "name")
    @Schema(name = "文件名")
    private val key: String? = null

    @Schema(name = "空间名")
    private val bucket: String? = null

    @Schema(name = "大小")
    private val size: String? = null

    @Schema(name = "文件地址")
    private val url: String? = null

    @Schema(name = "文件类型")
    private val suffix: String? = null

    @Schema(name = "空间类型：公开/私有")
    private val type = "公开"

    @UpdateTimestamp
    @Schema(name = "创建或更新时间")
    @Column(name = "update_time")
    private val updateTime: Timestamp? = null
}