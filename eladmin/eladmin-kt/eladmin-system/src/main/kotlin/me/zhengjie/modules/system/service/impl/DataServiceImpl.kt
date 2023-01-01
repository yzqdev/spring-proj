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
package me.zhengjie.modules.system.service.impl

import lombok.RequiredArgsConstructor
import me.zhengjie.modules.system.service.DataService
import me.zhengjie.modules.system.service.DeptService
import me.zhengjie.modules.system.service.RoleService
import me.zhengjie.modules.system.service.dto.RoleSmallDto
import me.zhengjie.modules.system.service.dto.UserDto
import me.zhengjie.utils.enums.DataScopeEnum
import me.zhengjie.utils.enums.DataScopeEnum.Companion.find
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author Zheng Jie
 * @website https://el-admin.vip
 * @description 数据权限服务实现
 * @date 2020-05-07
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = ["data"])
class DataServiceImpl : DataService {
    private val roleService: RoleService? = null
    private val deptService: DeptService? = null

    /**
     * 用户角色改变时需清理缓存
     * @param user /
     * @return /
     */
    @Cacheable(key = "'user:' + #p0.id")
    override fun getDeptIds(user: UserDto): List<Long> {
        // 用于存储部门id
        val deptIds: MutableSet<Long> = HashSet()
        // 查询用户角色
        val roleSet = roleService!!.findByUsersId(user.id)
        // 获取对应的部门ID
        for (role in roleSet) {
            val dataScopeEnum = find(role.dataScope)
            when (Objects.requireNonNull(dataScopeEnum)) {
                DataScopeEnum.THIS_LEVEL -> deptIds.add(user.dept.id)
                DataScopeEnum.CUSTOMIZE -> deptIds.addAll(getCustomize(deptIds, role))
                else -> return ArrayList(deptIds)
            }
        }
        return ArrayList(deptIds)
    }

    /**
     * 获取自定义的数据权限
     * @param deptIds 部门ID
     * @param role 角色
     * @return 数据权限ID
     */
    fun getCustomize(deptIds: MutableSet<Long>, role: RoleSmallDto): Set<Long> {
        val depts = deptService!!.findByRoleId(role.id)
        for (dept in depts) {
            deptIds.add(dept.id)
            val deptChildren = deptService.findByPid(dept.id)
            if (deptChildren != null && deptChildren.size != 0) {
                deptIds.addAll(deptService.getDeptChildren(deptChildren))
            }
        }
        return deptIds
    }
}