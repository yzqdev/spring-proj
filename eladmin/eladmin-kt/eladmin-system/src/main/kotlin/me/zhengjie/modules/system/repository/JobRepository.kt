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
package me.zhengjie.modules.system.repositoryimport

import me.zhengjie.modules.system.domain.Job
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor


/**
 * @author Zheng Jie
 * @date 2019-03-29
 */
interface JobRepository : JpaRepository<Job?, Long?>, JpaSpecificationExecutor<Job?> {
    /**
     * 根据名称查询
     * @param name 名称
     * @return /
     */
    fun findByName(name: String?): Job?

    /**
     * 根据Id删除
     * @param ids /
     */
    fun deleteAllByIdIn(ids: Set<Long?>?)
}