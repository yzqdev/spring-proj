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
package me.zhengjie.modules.system.domain;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
* @author Zheng Jie
* @date 2019-03-25
*/
@Entity
@Getter
@Setter
@Table(name="sys_dept")
public class Dept extends BaseEntity implements Serializable {

    @Id
    @Column(name = "dept_id")
    @NotNull(groups = Update.class)
    @Schema(name = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JSONField(serialize = false)
    @ManyToMany(mappedBy = "depts")
    @Schema(name = "角色")
    private Set<Role> roles;

    @Schema(name = "排序")
    private Integer deptSort;

    @NotBlank
    @Schema(name = "部门名称")
    private String name;

    @NotNull
    @Schema(name = "是否启用")
    private Boolean enabled;

    @Schema(name = "上级部门")
    private Long pid;

    @Schema(name = "子节点数目", hidden = true)
    private Integer subCount = 0;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Dept dept = (Dept) o;
        return Objects.equals(id, dept.id) &&
                Objects.equals(name, dept.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}