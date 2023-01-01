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

import lombok.RequiredArgsConstructor
import me.zhengjie.config.FileProperties
import me.zhengjie.exception.BadRequestException
import me.zhengjie.exception.EntityExistException
import me.zhengjie.exception.EntityNotFoundException
import me.zhengjie.modules.security.service.OnlineUserService
import me.zhengjie.modules.security.service.UserCacheClean
import me.zhengjie.modules.system.domain.User
import me.zhengjie.modules.system.repository.UserRepository
import me.zhengjie.modules.system.service.UserService
import me.zhengjie.modules.system.service.dto.JobSmallDto
import me.zhengjie.modules.system.service.dto.RoleSmallDto
import me.zhengjie.modules.system.service.dto.UserDto
import me.zhengjie.modules.system.service.dto.UserQueryCriteria
import me.zhengjie.modules.system.service.mapstruct.UserMapper
import me.zhengjie.utils.*
import me.zhengjie.utils.FileUtil.checkSize
import me.zhengjie.utils.FileUtil.downloadExcel
import me.zhengjie.utils.FileUtil.getExtensionName
import me.zhengjie.utils.FileUtil.upload
import me.zhengjie.utils.QueryHelp.getPredicate
import me.zhengjie.utils.RedisUtils.del
import me.zhengjie.utils.ValidationUtil.isNull
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*
import java.util.stream.Collectors
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
import javax.servlet.http.HttpServletResponse
import javax.validation.constraints.NotBlank

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
import me.zhengjie.modules.system.domain.Dept
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
import me.zhengjie.modules.system.domain.DictDetail
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
 * @date 2018-11-23
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = ["user"])
class UserServiceImpl : UserService {
    private val userRepository: UserRepository? = null
    private val userMapper: UserMapper? = null
    private val properties: FileProperties? = null
    private val redisUtils: RedisUtils? = null
    private val userCacheClean: UserCacheClean? = null
    private val onlineUserService: OnlineUserService? = null
    override fun queryAll(criteria: UserQueryCriteria?, pageable: Pageable?): Any {
        val page = userRepository!!.findAll(
            { root: Root<User>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
                getPredicate(
                    root,
                    criteria,
                    criteriaBuilder!!
                )
            }, pageable!!
        )
        return PageUtil.toPage(page.map { entity: User -> userMapper!!.toDto(entity) })
    }

    override fun queryAll(criteria: UserQueryCriteria?): List<UserDto?>? {
        val users =
            userRepository!!.findAll { root: Root<User>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
                getPredicate(
                    root,
                    criteria,
                    criteriaBuilder!!
                )
            }
        return userMapper!!.toDto(users)
    }

    @Cacheable(key = "'id:' + #p0")
    @Transactional(rollbackFor = [Exception::class])
    override fun findById(id: Long): UserDto {
        val user = userRepository!!.findById(id).orElseGet { User() }
        isNull(user.id, "User", "id", id)
        return userMapper!!.toDto(user)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun create(resources: User) {
        if (userRepository!!.findByUsername(resources.username) != null) {
            throw EntityExistException(User::class.java, "username", resources.username)
        }
        if (userRepository.findByEmail(resources.email) != null) {
            throw EntityExistException(User::class.java, "email", resources.email)
        }
        if (userRepository.findByPhone(resources.phone) != null) {
            throw EntityExistException(User::class.java, "phone", resources.phone)
        }
        userRepository.save(resources)
    }

    @Transactional(rollbackFor = [Exception::class])
    @Throws(
        Exception::class
    )
    override fun update(resources: User) {
        val user = userRepository!!.findById(resources.id).orElseGet { User() }
        isNull(user.id, "User", "id", resources.id)
        val user1 = userRepository.findByUsername(resources.username)
        val user2 = userRepository.findByEmail(resources.email)
        val user3 = userRepository.findByPhone(resources.phone)
        if (user1 != null && user.id != user1.id) {
            throw EntityExistException(User::class.java, "username", resources.username)
        }
        if (user2 != null && user.id != user2.id) {
            throw EntityExistException(User::class.java, "email", resources.email)
        }
        if (user3 != null && user.id != user3.id) {
            throw EntityExistException(User::class.java, "phone", resources.phone)
        }
        // 如果用户的角色改变
        if (resources.roles != user.roles) {
            redisUtils!!.del(CacheKey.DATA_USER + resources.id)
            redisUtils.del(CacheKey.MENU_USER + resources.id)
            redisUtils.del(CacheKey.ROLE_AUTH + resources.id)
        }
        // 如果用户被禁用，则清除用户登录信息
        if (!resources.enabled) {
            onlineUserService!!.kickOutForUsername(resources.username)
        }
        user.username = resources.username
        user.email = resources.email
        user.enabled = resources.enabled
        user.roles = resources.roles
        user.dept = resources.dept
        user.jobs = resources.jobs
        user.phone = resources.phone
        user.nickName = resources.nickName
        user.gender = resources.gender
        userRepository.save(user)
        // 清除缓存
        delCaches(user.id, user.username)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun updateCenter(resources: User) {
        val user = userRepository!!.findById(resources.id).orElseGet { User() }
        val user1 = userRepository.findByPhone(resources.phone)
        if (user1 != null && user.id != user1.id) {
            throw EntityExistException(User::class.java, "phone", resources.phone)
        }
        user.nickName = resources.nickName
        user.phone = resources.phone
        user.gender = resources.gender
        userRepository.save(user)
        // 清理缓存
        delCaches(user.id, user.username)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun delete(ids: Set<Long>) {
        for (id in ids) {
            // 清理缓存
            val user = findById(id)
            delCaches(user.id, user.username)
        }
        userRepository!!.deleteAllByIdIn(ids)
    }

    override fun findByName(userName: String?): UserDto? {
        val user = userRepository!!.findByUsername(userName)
        return if (user == null) {
            throw EntityNotFoundException(User::class.java, "name", userName!!)
        } else {
            userMapper!!.toDto(user)
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun updatePass(username: String?, pass: String?) {
        userRepository!!.updatePass(username, pass, Date())
        flushCache(username)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun updateAvatar(multipartFile: MultipartFile): Map<String?, String?> {
        // 文件大小验证
        checkSize(properties.getAvatarMaxSize(), multipartFile.size)
        // 验证文件上传的格式
        val image = "gif jpg png jpeg"
        val fileType = getExtensionName(multipartFile.originalFilename)
        if (fileType != null && !image.contains(fileType)) {
            throw BadRequestException("文件格式错误！, 仅支持 $image 格式")
        }
        val user = userRepository!!.findByUsername(SecurityUtils.getCurrentUsername())
        val oldPath = user.avatarPath
        val file = upload(multipartFile, properties!!.path.getAvatar())
        user.avatarPath = Objects.requireNonNull(file).path
        user.avatarName = file!!.name
        userRepository.save(user)
        if (StringUtils.isNotBlank(oldPath)) {
            FileUtil.del(oldPath)
        }
        val username: @NotBlank String? = user.username
        flushCache(username)
        return object : HashMap<String?, String?>(1) {
            init {
                put("avatar", file.name)
            }
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun updateEmail(username: String?, email: String?) {
        userRepository!!.updateEmail(username, email)
        flushCache(username)
    }

    @Throws(IOException::class)
    override fun download(queryAll: List<UserDto>, response: HttpServletResponse?) {
        val list: MutableList<Map<String?, Any?>?> = ArrayList()
        for (userDTO in queryAll) {
            val roles = userDTO.roles.stream().map { obj: RoleSmallDto -> obj.name }.collect(Collectors.toList())
            val map: MutableMap<String?, Any?> = LinkedHashMap()
            map["用户名"] = userDTO.username
            map["角色"] = roles
            map["部门"] = userDTO.dept.name
            map["岗位"] = userDTO.jobs.stream().map { obj: JobSmallDto -> obj.name }.collect(Collectors.toList())
            map["邮箱"] = userDTO.email
            map["状态"] = if (userDTO.enabled) "启用" else "禁用"
            map["手机号码"] = userDTO.phone
            map["修改密码的时间"] = userDTO.pwdResetTime
            map["创建日期"] = userDTO.getCreateTime()
            list.add(map)
        }
        downloadExcel(list, response!!)
    }

    /**
     * 清理缓存
     *
     * @param id /
     */
    fun delCaches(id: Long, username: String?) {
        redisUtils!!.del(CacheKey.USER_ID + id)
        flushCache(username)
    }

    /**
     * 清理 登陆时 用户缓存信息
     *
     * @param username /
     */
    private fun flushCache(username: String?) {
        userCacheClean!!.cleanUserCache(username)
    }
}