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

java.io.Serializableimport java.sql.Timestampimport java.util.HashSet
/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@Data
class UserQueryCriteria : Serializable {
    @Query
    private val id: Long? = null

    @Query(propName = "id", type = Query.Type.IN, joinName = "dept")
    private val deptIds: Set<Long> = HashSet()

    @Query(blurry = "email,username,nickName")
    private val blurry: String? = null

    @Query
    private val enabled: Boolean? = null
    private val deptId: Long? = null

    @Query(type = Query.Type.BETWEEN)
    private val createTime: List<Timestamp>? = null
}