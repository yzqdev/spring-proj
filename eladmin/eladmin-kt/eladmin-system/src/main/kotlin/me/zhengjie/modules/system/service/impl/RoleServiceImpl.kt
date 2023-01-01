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
package me.zhengjie.modules.system.service.implimport

import cn.hutool.core.collection.CollectionUtil
import lombok.RequiredArgsConstructor
import me.zhengjie.exception.BadRequestException
import me.zhengjie.exception.EntityExistException
import me.zhengjie.modules.security.service.UserCacheClean
import me.zhengjie.modules.system.domain.*
import me.zhengjie.modules.system.repository.RoleRepository
import me.zhengjie.modules.system.repository.UserRepository
import me.zhengjie.modules.system.service.RoleService
import me.zhengjie.modules.system.service.dto.RoleDto
import me.zhengjie.modules.system.service.dto.RoleQueryCriteria
import me.zhengjie.modules.system.service.dto.RoleSmallDto
import me.zhengjie.modules.system.service.dto.UserDto
import me.zhengjie.modules.system.service.mapstruct.RoleMapper
import me.zhengjie.modules.system.service.mapstruct.RoleSmallMapper
import me.zhengjie.utils.*
import me.zhengjie.utils.FileUtil.downloadExcel
import me.zhengjie.utils.QueryHelp.getPredicate
import me.zhengjie.utils.ValidationUtil.isNull
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import java.util.*
import java.util.function.Consumer
import java.util.function.Predicate
import java.util.stream.Collectors
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
import javax.servlet.http.HttpServletResponse

me.zhengjie.utils.PageUtil.toPage
import me.zhengjie.base.BaseMapper.toEntity
import me.zhengjie.base.BaseEntity.toString
import me.zhengjie.utils.RsaUtils.decryptByPrivateKey
import me.zhengjie.utils.enums.CodeBiEnum.Companion.find
import me.zhengjie.utils.QueryHelp.getPredicate
import me.zhengjie.base.BaseMapper.toDto
import me.zhengjie.utils.ValidationUtil.isNull
import me.zhengjie.utils.RedisUtils.delByKeys
import me.zhengjie.utils.FileUtil.downloadExcel
import me.zhengjie.utils.enums.DataScopeEnum.Companion.find
import me.zhengjie.utils.QueryHelp.getAllFields
import me.zhengjie.utils.RedisUtils.del
import me.zhengjie.modules.security.service.UserCacheClean.cleanUserCache
import me.zhengjie.modules.security.service.OnlineUserService.kickOutForUsername
import me.zhengjie.utils.FileUtil.checkSize
import me.zhengjie.utils.FileUtil.getExtensionName
import me.zhengjie.utils.FileUtil.upload
import me.zhengjie.config.FileProperties.path
import me.zhengjie.utils.RedisUtils.get
import me.zhengjie.utils.RedisUtils.set
import me.zhengjie.utils.FileUtil.getSize
import me.zhengjie.utils.StringUtils.localIp
import org.springframework.web.bind.annotation.RestController
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.RequestMapping
import me.zhengjie.modules.system.service.JobService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.security.access.prepost.PreAuthorize
import javax.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.validation.annotation.Validated
import me.zhengjie.exception.BadRequestException
import me.zhengjie.modules.system.rest.JobController
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import me.zhengjie.modules.system.service.DeptService
import me.zhengjie.modules.system.service.dto.DeptQueryCriteria
import me.zhengjie.modules.system.service.dto.DeptDto
import me.zhengjie.modules.system.rest.DeptController
import cn.hutool.core.collection.CollectionUtil
import me.zhengjie.modules.system.service.DictService
import me.zhengjie.modules.system.service.dto.DictQueryCriteria
import me.zhengjie.modules.system.rest.DictController
import me.zhengjie.modules.system.service.MenuService
import me.zhengjie.modules.system.service.mapstruct.MenuMapper
import me.zhengjie.modules.system.service.dto.MenuQueryCriteria
import me.zhengjie.modules.system.service.dto.MenuDto
import org.springframework.web.bind.annotation.RequestParam
import me.zhengjie.modules.system.rest.MenuController
import me.zhengjie.modules.system.service.RoleService
import org.springframework.web.bind.annotation.PathVariable
import me.zhengjie.modules.system.service.dto.RoleQueryCriteria
import me.zhengjie.modules.system.rest.RoleController
import me.zhengjie.modules.system.service.dto.RoleDto
import me.zhengjie.modules.system.service.dto.RoleSmallDto
import org.springframework.security.crypto.password.PasswordEncoder
import me.zhengjie.modules.system.service.UserService
import me.zhengjie.modules.system.service.DataService
import me.zhengjie.modules.system.service.VerifyService
import me.zhengjie.modules.system.service.dto.UserQueryCriteria
import me.zhengjie.modules.system.domain.vo.UserPassVo
import me.zhengjie.config.RsaProperties
import me.zhengjie.modules.system.service.dto.UserDto
import org.springframework.web.multipart.MultipartFile
import me.zhengjie.utils.enums.CodeEnum
import me.zhengjie.annotation.rest.AnonymousGetMapping
import me.zhengjie.modules.system.rest.LimitController
import me.zhengjie.domain.vo.EmailVo
import me.zhengjie.utils.enums.CodeBiEnum
import me.zhengjie.modules.system.service.MonitorService
import me.zhengjie.modules.system.service.DictDetailService
import me.zhengjie.modules.system.service.dto.DictDetailQueryCriteria
import org.springframework.data.web.PageableDefault
import me.zhengjie.modules.system.service.dto.DictDetailDto
import me.zhengjie.modules.system.rest.DictDetailController
import me.zhengjie.modules.system.domain.vo.MenuMetaVo
import me.zhengjie.modules.system.domain.vo.MenuVo
import lombok.AllArgsConstructor
import me.zhengjie.base.BaseEntity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import com.alibaba.fastjson.annotation.JSONField
import javax.persistence.ManyToMany
import javax.persistence.JoinTable
import javax.persistence.JoinColumn
import me.zhengjie.utils.enums.DataScopeEnum
import javax.persistence.FetchType
import lombok.NoArgsConstructor
import me.zhengjie.base.BaseDTO
import me.zhengjie.modules.system.service.dto.JobSmallDto
import me.zhengjie.modules.system.service.dto.DeptSmallDto
import me.zhengjie.modules.system.service.dto.DictSmallDto
import me.zhengjie.annotation.DataPermission
import org.springframework.cache.annotation.CacheConfig
import me.zhengjie.modules.system.repository.JobRepository
import me.zhengjie.modules.system.service.mapstruct.JobMapper
import me.zhengjie.modules.system.repository.UserRepository
import javax.persistence.criteria.CriteriaBuilder
import me.zhengjie.modules.system.service.dto.JobDto
import me.zhengjie.exception.EntityExistException
import org.springframework.cache.annotation.CacheEvict
import me.zhengjie.modules.system.repository.DeptRepository
import me.zhengjie.modules.system.service.mapstruct.DeptMapper
import me.zhengjie.modules.system.repository.RoleRepository
import me.zhengjie.modules.system.repository.DictRepository
import me.zhengjie.modules.system.service.mapstruct.DictMapper
import me.zhengjie.modules.system.service.dto.DictDto
import me.zhengjie.modules.system.repository.MenuRepository
import me.zhengjie.modules.system.service.mapstruct.RoleMapper
import me.zhengjie.modules.system.service.mapstruct.RoleSmallMapper
import me.zhengjie.modules.security.service.UserCacheClean
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import me.zhengjie.modules.system.service.mapstruct.UserMapper
import me.zhengjie.config.FileProperties
import me.zhengjie.modules.security.service.OnlineUserService
import cn.hutool.extra.template.TemplateUtil
import cn.hutool.extra.template.TemplateConfig
import cn.hutool.core.util.RandomUtil
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OSFileStore
import oshi.hardware.GlobalMemory
import oshi.hardware.VirtualMemory
import oshi.hardware.CentralProcessor
import cn.hutool.core.date.BetweenFormatter
import me.zhengjie.modules.system.repository.DictDetailRepository
import me.zhengjie.modules.system.service.mapstruct.DictDetailMapper
import org.mapstruct.ReportingPolicy
import me.zhengjie.base.BaseMapper
import me.zhengjie.modules.system.service.mapstruct.DictSmallMapper
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying

/**
 * @author Zheng Jie
 * @date 2018-12-03
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = ["role"])
class RoleServiceImpl : RoleService {
    private val roleRepository: RoleRepository? = null
    private val roleMapper: RoleMapper? = null
    private val roleSmallMapper: RoleSmallMapper? = null
    private val redisUtils: RedisUtils? = null
    private val userRepository: UserRepository? = null
    private val userCacheClean: UserCacheClean? = null
    override fun queryAll(): List<RoleDto?>? {
        val sort = Sort.by(Sort.Direction.ASC, "level")
        return roleMapper!!.toDto(roleRepository!!.findAll(sort))
    }

    override fun queryAll(criteria: RoleQueryCriteria?): List<RoleDto?>? {
        return roleMapper!!.toDto(roleRepository!!.findAll { root: Root<Role>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
            getPredicate(
                root,
                criteria,
                criteriaBuilder!!
            )
        })
    }

    override fun queryAll(criteria: RoleQueryCriteria?, pageable: Pageable?): Any {
        val page = roleRepository!!.findAll(
            { root: Root<Role>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
                getPredicate(
                    root,
                    criteria,
                    criteriaBuilder!!
                )
            }, pageable!!
        )
        return PageUtil.toPage(page.map { entity: Role -> roleMapper!!.toDto(entity) })
    }

    @Cacheable(key = "'id:' + #p0")
    @Transactional(rollbackFor = [Exception::class])
    override fun findById(id: Long): RoleDto {
        val role = roleRepository!!.findById(id).orElseGet { Role() }
        isNull(role.id, "Role", "id", id)
        return roleMapper!!.toDto(role)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun create(resources: Role) {
        if (roleRepository!!.findByName(resources.name) != null) {
            throw EntityExistException(Role::class.java, "username", resources.name)
        }
        roleRepository.save(resources)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun update(resources: Role) {
        val role = roleRepository!!.findById(resources.id).orElseGet { Role() }
        isNull(role.id, "Role", "id", resources.id)
        val role1 = roleRepository.findByName(resources.name)
        if (role1 != null && role1.id != role.id) {
            throw EntityExistException(Role::class.java, "username", resources.name)
        }
        role.name = resources.name
        role.description = resources.description
        role.dataScope = resources.dataScope
        role.depts = resources.depts
        role.level = resources.level
        roleRepository.save(role)
        // 更新相关缓存
        delCaches(role.id, null)
    }

    override fun updateMenu(resources: Role, roleDTO: RoleDto) {
        val role = roleMapper!!.toEntity(roleDTO)
        val users = userRepository!!.findByRoleId(role.id)
        // 更新菜单
        role.menus = resources.menus
        delCaches(resources.id, users)
        roleRepository!!.save(role)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun untiedMenu(menuId: Long?) {
        // 更新菜单
        roleRepository!!.untiedMenu(menuId)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun delete(ids: Set<Long>) {
        for (id in ids) {
            // 更新相关缓存
            delCaches(id, null)
        }
        roleRepository!!.deleteAllByIdIn(ids)
    }

    override fun findByUsersId(id: Long?): List<RoleSmallDto?>? {
        return roleSmallMapper!!.toDto(ArrayList(roleRepository!!.findByUserId(id)))
    }

    override fun findByRoles(roles: Set<Role>): Int {
        if (roles.size == 0) {
            return Int.MAX_VALUE
        }
        val roleDtos: MutableSet<RoleDto> = HashSet()
        for (role in roles) {
            roleDtos.add(findById(role.id))
        }
        return Collections.min(roleDtos.stream().map { obj: RoleDto -> obj.level }.collect(Collectors.toList()))
    }

    @Cacheable(key = "'auth:' + #p0.id")
    override fun mapToGrantedAuthorities(user: UserDto): List<GrantedAuthority> {
        var permissions: MutableSet<String?> = HashSet()
        // 如果是管理员直接返回
        if (user.isAdmin) {
            permissions.add("admin")
            return permissions.stream().map { role: String? -> SimpleGrantedAuthority(role) }
                .collect(Collectors.toList())
        }
        val roles = roleRepository!!.findByUserId(user.id)
        permissions = roles.stream().flatMap { role: Role -> role.menus.stream() }
            .filter(Predicate { menu: Menu -> StringUtils.isNotBlank(menu.permission) })
            .map { obj: Menu -> obj.permission }.collect(Collectors.toSet())
        return permissions.stream().map { role: String? -> SimpleGrantedAuthority(role) }
            .collect(Collectors.toList())
    }

    @Throws(IOException::class)
    override fun download(roles: List<RoleDto>, response: HttpServletResponse?) {
        val list: MutableList<Map<String?, Any?>?> = ArrayList()
        for (role in roles) {
            val map: MutableMap<String?, Any?> = LinkedHashMap()
            map["角色名称"] = role.name
            map["角色级别"] = role.level
            map["描述"] = role.description
            map["创建日期"] = role.getCreateTime()
            list.add(map)
        }
        downloadExcel(list, response!!)
    }

    override fun verification(ids: Set<Long?>?) {
        if (userRepository!!.countByRoles(ids) > 0) {
            throw BadRequestException("所选角色存在用户关联，请解除关联再试！")
        }
    }

    override fun findInMenuId(menuIds: List<Long?>?): List<Role?>? {
        return roleRepository!!.findInMenuId(menuIds)
    }

    /**
     * 清理缓存
     * @param id /
     */
    fun delCaches(id: Long, users: List<User?>?) {
        var users = users
        users = if (CollectionUtil.isEmpty(users)) userRepository!!.findByRoleId(id) else users
        if (CollectionUtil.isNotEmpty(users)) {
            users!!.forEach(Consumer { item: User? -> userCacheClean!!.cleanUserCache(item.getUsername()) })
            val userIds = users.stream().map { obj: User? -> obj.getId() }
                .collect(Collectors.toSet())
            redisUtils!!.delByKeys(CacheKey.DATA_USER, userIds)
            redisUtils.delByKeys(CacheKey.MENU_USER, userIds)
            redisUtils.delByKeys(CacheKey.ROLE_AUTH, userIds)
        }
        redisUtils!!.del(CacheKey.ROLE_ID + id)
    }
}