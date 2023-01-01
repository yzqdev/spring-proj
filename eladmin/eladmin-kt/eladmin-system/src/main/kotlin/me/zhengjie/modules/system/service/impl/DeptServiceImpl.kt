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

import cn.hutool.core.collection.CollectionUtil
import cn.hutool.core.util.ObjectUtil
import lombok.RequiredArgsConstructor
import me.zhengjie.exception.BadRequestException
import me.zhengjie.modules.system.domain.Dept
import me.zhengjie.modules.system.domain.User
import me.zhengjie.modules.system.repository.DeptRepository
import me.zhengjie.modules.system.repository.RoleRepository
import me.zhengjie.modules.system.repository.UserRepository
import me.zhengjie.modules.system.service.DeptService
import me.zhengjie.modules.system.service.dto.DeptDto
import me.zhengjie.modules.system.service.dto.DeptQueryCriteria
import me.zhengjie.modules.system.service.mapstruct.DeptMapper
import me.zhengjie.utils.*
import me.zhengjie.utils.FileUtil.downloadExcel
import me.zhengjie.utils.QueryHelp.getAllFields
import me.zhengjie.utils.QueryHelp.getPredicate
import me.zhengjie.utils.ValidationUtil.isNull
import me.zhengjie.utils.enums.DataScopeEnum
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import java.util.function.Consumer
import java.util.stream.Collectors
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
import javax.servlet.http.HttpServletResponse

/**
 * @author Zheng Jie
 * @date 2019-03-25
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = ["dept"])
class DeptServiceImpl : DeptService {
    private val deptRepository: DeptRepository? = null
    private val deptMapper: DeptMapper? = null
    private val userRepository: UserRepository? = null
    private val redisUtils: RedisUtils? = null
    private val roleRepository: RoleRepository? = null
    @Throws(Exception::class)
    override fun queryAll(criteria: DeptQueryCriteria, isQuery: Boolean): List<DeptDto>? {
        val sort = Sort.by(Sort.Direction.ASC, "deptSort")
        val dataScopeType = SecurityUtils.getDataScopeType()
        if (java.lang.Boolean.TRUE == isQuery) {
            if (dataScopeType == DataScopeEnum.ALL.getValue()) {
                criteria.pidIsNull = true
            }
            val fields = getAllFields(criteria.javaClass, ArrayList())
            val fieldNames: MutableList<String> = ArrayList()
            fieldNames.add("pidIsNull")
            fieldNames.add("enabled")
            for (field in fields) {
                //设置对象的访问权限，保证对private的属性的访问
                field.isAccessible = true
                val `val` = field[criteria]
                if (fieldNames.contains(field.name)) {
                    continue
                }
                if (ObjectUtil.isNotNull(`val`)) {
                    criteria.pidIsNull = null
                    break
                }
            }
        }
        val list =
            deptMapper!!.toDto(deptRepository!!.findAll({ root: Root<Dept>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
                getPredicate(
                    root,
                    criteria,
                    criteriaBuilder!!
                )
            }, sort))
        // 如果为空，就代表为自定义权限或者本级权限，就需要去重，不理解可以注释掉，看查询结果
        return if (StringUtils.isBlank(dataScopeType)) {
            deduplication(list)
        } else list
    }

    @Cacheable(key = "'id:' + #p0")
    override fun findById(id: Long): DeptDto {
        val dept = deptRepository!!.findById(id).orElseGet { Dept() }
        isNull(dept.id, "Dept", "id", id)
        return deptMapper!!.toDto(dept)
    }

    override fun findByPid(pid: Long): List<Dept?>? {
        return deptRepository!!.findByPid(pid)
    }

    override fun findByRoleId(id: Long?): Set<Dept?>? {
        return deptRepository!!.findByRoleId(id)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun create(resources: Dept) {
        deptRepository!!.save(resources)
        // 计算子节点数目
        resources.subCount = 0
        // 清理缓存
        updateSubCnt(resources.pid)
        // 清理自定义角色权限的datascope缓存
        delCaches(resources.pid)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun update(resources: Dept) {
        // 旧的部门
        val oldPid = findById(resources.id).pid
        val newPid = resources.pid
        if (resources.pid != null && resources.id == resources.pid) {
            throw BadRequestException("上级不能为自己")
        }
        val dept = deptRepository!!.findById(resources.id).orElseGet { Dept() }
        isNull(dept.id, "Dept", "id", resources.id)
        resources.id = dept.id
        deptRepository.save(resources)
        // 更新父节点中子节点数目
        updateSubCnt(oldPid)
        updateSubCnt(newPid)
        // 清理缓存
        delCaches(resources.id)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun delete(deptDtos: Set<DeptDto>) {
        for (deptDto in deptDtos) {
            // 清理缓存
            delCaches(deptDto.id)
            deptRepository!!.deleteById(deptDto.id)
            updateSubCnt(deptDto.pid)
        }
    }

    @Throws(IOException::class)
    override fun download(deptDtos: List<DeptDto>, response: HttpServletResponse?) {
        val list: MutableList<Map<String?, Any?>?> = ArrayList()
        for (deptDTO in deptDtos) {
            val map: MutableMap<String?, Any?> = LinkedHashMap()
            map["部门名称"] = deptDTO.name
            map["部门状态"] = if (deptDTO.enabled) "启用" else "停用"
            map["创建日期"] = deptDTO.getCreateTime()
            list.add(map)
        }
        downloadExcel(list, response!!)
    }

    override fun getDeleteDepts(menuList: List<Dept>, deptDtos: MutableSet<DeptDto?>): Set<DeptDto?> {
        for (dept in menuList) {
            deptDtos.add(deptMapper!!.toDto(dept))
            val depts = deptRepository!!.findByPid(dept.id)
            if (depts != null && depts.size != 0) {
                getDeleteDepts(depts, deptDtos)
            }
        }
        return deptDtos
    }

    override fun getDeptChildren(deptList: List<Dept?>): List<Long> {
        val list: MutableList<Long> = ArrayList()
        deptList.forEach(
            Consumer { dept: Dept? ->
                if (dept != null && dept.enabled) {
                    val depts = deptRepository!!.findByPid(dept.id)
                    if (depts.size != 0) {
                        list.addAll(getDeptChildren(depts))
                    }
                    list.add(dept.id)
                }
            }
        )
        return list
    }

    override fun getSuperior(deptDto: DeptDto, depts: MutableList<Dept?>): List<DeptDto>? {
        if (deptDto.pid == null) {
            depts.addAll(deptRepository!!.findByPidIsNull())
            return deptMapper!!.toDto(depts)
        }
        depts.addAll(deptRepository!!.findByPid(deptDto.pid))
        return getSuperior(findById(deptDto.pid), depts)
    }

    override fun buildTree(deptDtos: List<DeptDto>): Any {
        var trees: MutableSet<DeptDto?> = LinkedHashSet()
        val depts: MutableSet<DeptDto?> = LinkedHashSet()
        val deptNames = deptDtos.stream().map { obj: DeptDto -> obj.name }.collect(Collectors.toList())
        var isChild: Boolean
        for (deptDTO in deptDtos) {
            isChild = false
            if (deptDTO.pid == null) {
                trees.add(deptDTO)
            }
            for (it in deptDtos) {
                if (it.pid != null && deptDTO.id == it.pid) {
                    isChild = true
                    if (deptDTO.children == null) {
                        deptDTO.children = ArrayList()
                    }
                    deptDTO.children.add(it)
                }
            }
            if (isChild) {
                depts.add(deptDTO)
            } else if (deptDTO.pid != null && !deptNames.contains(findById(deptDTO.pid).name)) {
                depts.add(deptDTO)
            }
        }
        if (CollectionUtil.isEmpty(trees)) {
            trees = depts
        }
        val map: MutableMap<String, Any> = HashMap(2)
        map["totalElements"] = deptDtos.size
        map["content"] = if (CollectionUtil.isEmpty(trees)) deptDtos else trees
        return map
    }

    override fun verification(deptDtos: Set<DeptDto>) {
        val deptIds = deptDtos.stream().map { obj: DeptDto -> obj.id }.collect(Collectors.toSet())
        if (userRepository!!.countByDepts(deptIds) > 0) {
            throw BadRequestException("所选部门存在用户关联，请解除后再试！")
        }
        if (roleRepository!!.countByDepts(deptIds) > 0) {
            throw BadRequestException("所选部门存在角色关联，请解除后再试！")
        }
    }

    private fun updateSubCnt(deptId: Long?) {
        if (deptId != null) {
            val count = deptRepository!!.countByPid(deptId)
            deptRepository.updateSubCntById(count, deptId)
        }
    }

    private fun deduplication(list: List<DeptDto>?): List<DeptDto> {
        val deptDtos: MutableList<DeptDto> = ArrayList()
        for (deptDto in list!!) {
            var flag = true
            for (dto in list) {
                if (dto.id == deptDto.pid) {
                    flag = false
                    break
                }
            }
            if (flag) {
                deptDtos.add(deptDto)
            }
        }
        return deptDtos
    }

    /**
     * 清理缓存
     * @param id /
     */
    fun delCaches(id: Long) {
        val users = userRepository!!.findByRoleDeptId(id)
        // 删除数据权限
        redisUtils!!.delByKeys(CacheKey.DATA_USER, users.stream().map { obj: User -> obj.id }
            .collect(Collectors.toSet()))
        redisUtils.del(CacheKey.DEPT_ID + id)
    }
}