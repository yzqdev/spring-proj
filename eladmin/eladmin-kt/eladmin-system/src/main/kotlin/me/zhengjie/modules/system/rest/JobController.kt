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
package me.zhengjie.modules.system.restimport

import io.swagger.v3.oas.annotations.Operationimport

io.swagger.v3.oas.annotations.tags.Tagimport lombok.RequiredArgsConstructorimport me.zhengjie.annotation .Logimport me.zhengjie.base.BaseEntityimport me.zhengjie.exception.BadRequestExceptionimport me.zhengjie.modules.system.domain.Jobimport me.zhengjie.modules.system.rest.JobControllerimport me.zhengjie.modules.system.service.JobServiceimport me.zhengjie.modules.system.service.dto.*import org.springframework.data.domain.Pageableimport

org.springframework.http.HttpStatusimport org.springframework.http.ResponseEntityimport org.springframework.security.access.prepost.PreAuthorizeimport org.springframework.validation.annotation .Validatedimport org.springframework.web.bind.annotation .*import java.io.IOExceptionimport

javax.servlet.http.HttpServletResponse me.zhengjie.utils.PageUtil.toPage
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
import lombok.RequiredArgsConstructor
import me.zhengjie.modules.system.service.JobService
import org.springframework.security.access.prepost.PreAuthorize
import javax.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import me.zhengjie.exception.BadRequestException
import me.zhengjie.modules.system.rest.JobController
import me.zhengjie.modules.system.service.DeptService
import me.zhengjie.modules.system.domain.Dept
import me.zhengjie.modules.system.rest.DeptController
import cn.hutool.core.collection.CollectionUtil
import me.zhengjie.modules.system.service.DictService
import me.zhengjie.modules.system.rest.DictController
import me.zhengjie.modules.system.service.MenuService
import me.zhengjie.modules.system.service.mapstruct.MenuMapper
import me.zhengjie.modules.system.rest.MenuController
import me.zhengjie.modules.system.service.RoleService
import me.zhengjie.modules.system.rest.RoleController
import org.springframework.security.crypto.password.PasswordEncoder
import me.zhengjie.modules.system.service.UserService
import me.zhengjie.modules.system.service.DataService
import me.zhengjie.modules.system.service.VerifyService
import me.zhengjie.modules.system.domain.vo.UserPassVo
import me.zhengjie.utils.RsaUtils
import me.zhengjie.config.RsaProperties
import org.springframework.web.multipart.MultipartFile
import me.zhengjie.utils.enums.CodeEnum
import me.zhengjie.annotation.rest.AnonymousGetMapping
import me.zhengjie.modules.system.rest.LimitController
import me.zhengjie.domain.vo.EmailVo
import me.zhengjie.utils.enums.CodeBiEnum
import me.zhengjie.modules.system.service.MonitorService
import me.zhengjie.modules.system.service.DictDetailService
import org.springframework.data.web.PageableDefault
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
import me.zhengjie.annotation.DataPermission
import org.springframework.cache.annotation.CacheConfig
import me.zhengjie.modules.system.repository.JobRepository
import me.zhengjie.modules.system.service.mapstruct.JobMapper
import me.zhengjie.utils.RedisUtils
import me.zhengjie.modules.system.repository.UserRepository
import javax.persistence.criteria.CriteriaBuilder
import me.zhengjie.utils.QueryHelp
import me.zhengjie.exception.EntityExistException
import org.springframework.cache.annotation.CacheEvict
import me.zhengjie.modules.system.repository.DeptRepository
import me.zhengjie.modules.system.service.mapstruct.DeptMapper
import me.zhengjie.modules.system.repository.RoleRepository
import me.zhengjie.modules.system.repository.DictRepository
import me.zhengjie.modules.system.service.mapstruct.DictMapper
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
import me.zhengjie.utils.ElAdminConstant
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
 * @date 2019-03-29
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "?????????????????????")
@RequestMapping("/api/job")
class JobController {
    private val jobService: JobService? = null

    @Operation(summary = "??????????????????")
    @GetMapping(value = ["/download"])
    @PreAuthorize("@el.check('job:list')")
    @Throws(
        IOException::class
    )
    fun download(response: HttpServletResponse?, criteria: JobQueryCriteria?) {
        jobService!!.download(jobService.queryAll(criteria), response)
    }

    @Operation(summary = "????????????")
    @GetMapping
    @PreAuthorize("@el.check('job:list','user:list')")
    fun query(criteria: JobQueryCriteria?, pageable: Pageable?): ResponseEntity<Any> {
        return ResponseEntity(jobService!!.queryAll(criteria, pageable), HttpStatus.OK)
    }

    @Log("????????????")
    @Operation(summary = "????????????")
    @PostMapping
    @PreAuthorize("@el.check('job:add')")
    fun create(@Validated @RequestBody resources: Job): ResponseEntity<Any> {
        if (resources.id != null) {
            throw BadRequestException("A new " + JobController.Companion.ENTITY_NAME + " cannot already have an ID")
        }
        jobService!!.create(resources)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @Log("????????????")
    @Operation(summary = "????????????")
    @PutMapping
    @PreAuthorize("@el.check('job:edit')")
    fun update(
        @Validated(
            BaseEntity.Update::class
        ) @RequestBody resources: Job?
    ): ResponseEntity<Any> {
        jobService!!.update(resources)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @Log("????????????")
    @Operation(summary = "????????????")
    @DeleteMapping
    @PreAuthorize("@el.check('job:del')")
    fun delete(@RequestBody ids: Set<Long?>?): ResponseEntity<Any> {
        // ???????????????????????????
        jobService!!.verification(ids)
        jobService.delete(ids)
        return ResponseEntity(HttpStatus.OK)
    }

    companion object {
        private const val ENTITY_NAME = "job"
    }
}