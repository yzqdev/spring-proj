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
package me.zhengjie.modules.system.service.dto

lombok.Dataimport me.zhengjie.annotation .Query
import me.zhengjie.modules.system.repository.DictDetailRepository
import me.zhengjie.modules.system.service.mapstruct.DictDetailMapper
import org.mapstruct.ReportingPolicy
import me.zhengjie.base.BaseMapper
import me.zhengjie.modules.system.service.mapstruct.DictSmallMapper
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifyingimport

java.sql.Timestamp
/**
 * @author Zheng Jie
 * 公共查询类
 */
@Data
class MenuQueryCriteria {
    @Query(blurry = "title,component,permission")
    private val blurry: String? = null

    @Query(type = Query.Type.BETWEEN)
    private val createTime: List<Timestamp>? = null

    @Query(type = Query.Type.IS_NULL, propName = "pid")
    private val pidIsNull: Boolean? = null

    @Query
    private val pid: Long? = null
}