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
package me.zhengjie.modules.system.rest

import cn.hutool.core.collection.CollectionUtilimport

io.swagger.v3.oas.annotations.Operationimport io.swagger.v3.oas.annotations.tags.Tagimport lombok.RequiredArgsConstructorimport me.zhengjie.annotation .Logimport me.zhengjie.base.BaseEntityimport me.zhengjie.config.RsaPropertiesimport me.zhengjie.exception.BadRequestExceptionimport me.zhengjie.modules.system.domain.Userimport me.zhengjie.modules.system.domain.vo.UserPassVoimport me.zhengjie.modules.system.service.*import me.zhengjie.modules.system.service.dto.RoleSmallDtoimport

me.zhengjie.modules.system.service.dto.UserQueryCriteriaimport me.zhengjie.utils.*import me.zhengjie.utils.PageUtil.toPageimport

me.zhengjie.utils.RsaUtils.decryptByPrivateKeyimport me.zhengjie.utils.enums.CodeEnumimport org.springframework.data .domain.Pageableimport org.springframework.http.HttpStatusimport org.springframework.http.ResponseEntityimport org.springframework.security.access.prepost.PreAuthorizeimport org.springframework.security.crypto.password.PasswordEncoderimport org.springframework.util.CollectionUtilsimport org.springframework.util.ObjectUtilsimport org.springframework.validation.annotation .Validatedimport org.springframework.web.bind.annotation .*import org.springframework.web.multipart.MultipartFileimport

java.io.IOExceptionimport java.util.*import java.util.stream.Collectorsimport

javax.servlet.http.HttpServletResponse
/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@Tag(name = "?????????????????????")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
class UserController {
    private val passwordEncoder: PasswordEncoder? = null
    private val userService: UserService? = null
    private val dataService: DataService? = null
    private val deptService: DeptService? = null
    private val roleService: RoleService? = null
    private val verificationCodeService: VerifyService? = null

    @Operation(summary = "??????????????????")
    @GetMapping(value = ["/download"])
    @PreAuthorize("@el.check('user:list')")
    @Throws(
        IOException::class
    )
    fun download(response: HttpServletResponse?, criteria: UserQueryCriteria?) {
        userService!!.download(userService.queryAll(criteria), response)
    }

    @Operation(summary = "????????????")
    @GetMapping
    @PreAuthorize("@el.check('user:list')")
    fun query(criteria: UserQueryCriteria, pageable: Pageable?): ResponseEntity<Any> {
        if (!ObjectUtils.isEmpty(criteria.deptId)) {
            criteria.deptIds.add(criteria.deptId)
            // ??????????????????????????????
            val data = deptService!!.findByPid(criteria.deptId)
            // ?????????????????????ID?????????????????????
            criteria.deptIds.addAll(deptService.getDeptChildren(data))
        }
        // ????????????
        val dataScopes = dataService!!.getDeptIds(userService!!.findByName(SecurityUtils.getCurrentUsername()))
        // criteria.getDeptIds() ????????????????????????????????????????????????
        if (!CollectionUtils.isEmpty(criteria.deptIds) && !CollectionUtils.isEmpty(dataScopes)) {
            // ?????????
            criteria.deptIds.retainAll(dataScopes)
            if (!CollectionUtil.isEmpty(criteria.deptIds)) {
                return ResponseEntity(userService.queryAll(criteria, pageable), HttpStatus.OK)
            }
        } else {
            // ???????????????
            criteria.deptIds.addAll(dataScopes)
            return ResponseEntity(userService.queryAll(criteria, pageable), HttpStatus.OK)
        }
        return ResponseEntity(toPage(null, 0), HttpStatus.OK)
    }

    @Log("????????????")
    @Operation(summary = "????????????")
    @PostMapping
    @PreAuthorize("@el.check('user:add')")
    fun create(@Validated @RequestBody resources: User): ResponseEntity<Any> {
        checkLevel(resources)
        // ???????????? 123456
        resources.password = passwordEncoder!!.encode("123456")
        userService!!.create(resources)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @Log("????????????")
    @Operation(summary = "????????????")
    @PutMapping
    @PreAuthorize("@el.check('user:edit')")
    @Throws(
        Exception::class
    )
    fun update(@Validated(BaseEntity.Update::class) @RequestBody resources: User): ResponseEntity<Any> {
        checkLevel(resources)
        println(resources.toString())
        userService!!.update(resources)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @Log("???????????????????????????")
    @Operation(summary = "???????????????????????????")
    @PutMapping(value = ["center"])
    fun center(
        @Validated(
            BaseEntity.Update::class
        ) @RequestBody resources: User
    ): ResponseEntity<Any> {
        if (resources.id != SecurityUtils.getCurrentUserId()) {
            throw BadRequestException("????????????????????????")
        }
        userService!!.updateCenter(resources)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @Log("????????????")
    @Operation(summary = "????????????")
    @DeleteMapping
    @PreAuthorize("@el.check('user:del')")
    fun delete(@RequestBody ids: Set<Long?>): ResponseEntity<Any> {
        for (id in ids) {
            val currentLevel = Collections.min(
                roleService!!.findByUsersId(SecurityUtils.getCurrentUserId()).stream()
                    .map { obj: RoleSmallDto -> obj.level }
                    .collect(Collectors.toList()))
            val optLevel = Collections.min(
                roleService.findByUsersId(id).stream().map { obj: RoleSmallDto -> obj.level }
                    .collect(Collectors.toList())
            )
            if (currentLevel > optLevel) {
                throw BadRequestException("????????????????????????????????????" + userService!!.findById(id!!).username)
            }
        }
        userService!!.delete(ids)
        return ResponseEntity(HttpStatus.OK)
    }

    @Operation(summary = "????????????")
    @PostMapping(value = ["/updatePass"])
    @Throws(
        Exception::class
    )
    fun updatePass(@RequestBody passVo: UserPassVo): ResponseEntity<Any> {
        val oldPass = decryptByPrivateKey(RsaProperties.privateKey, passVo.oldPass)
        val newPass = decryptByPrivateKey(RsaProperties.privateKey, passVo.newPass)
        val user = userService!!.findByName(SecurityUtils.getCurrentUsername())
        if (!passwordEncoder!!.matches(oldPass, user.password)) {
            throw BadRequestException("??????????????????????????????")
        }
        if (passwordEncoder.matches(newPass, user.password)) {
            throw BadRequestException("?????????????????????????????????")
        }
        userService.updatePass(user.username, passwordEncoder.encode(newPass))
        return ResponseEntity(HttpStatus.OK)
    }

    @Operation(summary = "????????????")
    @PostMapping(value = ["/updateAvatar"])
    fun updateAvatar(@RequestParam avatar: MultipartFile?): ResponseEntity<Any> {
        return ResponseEntity(userService!!.updateAvatar(avatar), HttpStatus.OK)
    }

    @Log("????????????")
    @Operation(summary = "????????????")
    @PostMapping(value = ["/updateEmail/{code}"])
    @Throws(
        Exception::class
    )
    fun updateEmail(@PathVariable code: String?, @RequestBody user: User): ResponseEntity<Any> {
        val password = decryptByPrivateKey(RsaProperties.privateKey, user.password)
        val userDto = userService!!.findByName(SecurityUtils.getCurrentUsername())
        if (!passwordEncoder!!.matches(password, userDto.password)) {
            throw BadRequestException("????????????")
        }
        verificationCodeService!!.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + user.email, code)
        userService.updateEmail(userDto.username, user.email)
        return ResponseEntity(HttpStatus.OK)
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????????????????
     * @param resources /
     */
    private fun checkLevel(resources: User) {
        val currentLevel = Collections.min(
            roleService!!.findByUsersId(SecurityUtils.getCurrentUserId()).stream()
                .map { obj: RoleSmallDto -> obj.level }
                .toList())
        val optLevel = roleService.findByRoles(resources.roles)
        if (currentLevel > optLevel) {
            throw BadRequestException("??????????????????")
        }
    }
}