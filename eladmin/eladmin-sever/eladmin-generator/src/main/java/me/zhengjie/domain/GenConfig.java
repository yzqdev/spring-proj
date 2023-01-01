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
package me.zhengjie.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 代码生成配置
 * @author Zheng Jie
 * @date 2019-01-03
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "code_gen_config")
public class GenConfig implements Serializable {

    public GenConfig(String tableName) {
        this.tableName = tableName;
    }

    @Id
    @Column(name = "config_id")
    @Schema(name = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Schema(name = "表名")
    private String tableName;

    @Schema(name = "接口名称")
    private String apiAlias;

    @NotBlank
    @Schema(name = "包路径")
    private String pack;

    @NotBlank
    @Schema(name = "模块名")
    private String moduleName;

    @NotBlank
    @Schema(name = "前端文件路径")
    private String path;

    @Schema(name = "前端文件路径")
    private String apiPath;

    @Schema(name = "作者")
    private String author;

    @Schema(name = "表前缀")
    private String prefix;

    @Schema(name = "是否覆盖")
    private Boolean cover = false;
}
