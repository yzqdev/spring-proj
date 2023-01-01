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
package me.zhengjie.modules.system.domain

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Getter
import lombok.Setter
import me.zhengjie.base.BaseEntity
import java.io.Serializable
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

/**
 * @author Zheng Jie
 * @date 2018-11-22
 */
@Entity
@Getter
@Setter
@Table(name = "sys_user")
class User : BaseEntity(), Serializable {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "ID", hidden = true)
    private val id: @NotNull(groups = [Update::class]) Long? = null

    @ManyToMany
    @Schema(name = "用户角色")
    @JoinTable(
        name = "sys_users_roles",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "role_id")]
    )
    private val roles: Set<Role>? = null

    @ManyToMany
    @Schema(name = "用户岗位")
    @JoinTable(
        name = "sys_users_jobs",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "job_id", referencedColumnName = "job_id")]
    )
    private val jobs: Set<Job>? = null

    @OneToOne
    @JoinColumn(name = "dept_id")
    @Schema(name = "用户部门")
    private val dept: Dept? = null

    @Column(unique = true)
    @Schema(name = "用户名称")
    private val username: @NotBlank String? = null

    @Schema(name = "用户昵称")
    private val nickName: @NotBlank String? = null

    @Schema(name = "邮箱")
    private val email: @Email @NotBlank String? = null

    @Schema(name = "电话号码")
    private val phone: @NotBlank String? = null

    @Schema(name = "用户性别")
    private val gender: String? = null

    @Schema(name = "头像真实名称", hidden = true)
    private val avatarName: String? = null

    @Schema(name = "头像存储的路径", hidden = true)
    private val avatarPath: String? = null

    @Schema(name = "密码")
    private val password: String? = null

    @Schema(name = "是否启用")
    private val enabled: @NotNull Boolean? = null

    @Schema(name = "是否为admin账号", hidden = true)
    private val isAdmin = false

    @Column(name = "pwd_reset_time")
    @Schema(name = "最后修改密码的时间", hidden = true)
    private val pwdResetTime: Date? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val user = o as User
        return id == user.id && username == user.username
    }

    override fun hashCode(): Int {
        return Objects.hash(id, username)
    }
}