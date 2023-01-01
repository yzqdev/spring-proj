/*
 * Copyright 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.zhengjie.modules.security.config.bean

import lombok.Data

/**
 * 登录验证码配置信息
 *
 * @author liaojinlong
 * @date 2020/6/10 18:53
 */
@Data
class LoginCode {
    /**
     * 验证码配置
     */
    var codeType: LoginCodeEnum? = null

    /**
     * 验证码有效期 分钟
     */
    var expiration = 2L

    /**
     * 验证码内容长度
     */
    var length = 2

    /**
     * 验证码宽度
     */
    var width = 111

    /**
     * 验证码高度
     */
    var height = 36

    /**
     * 验证码字体
     */
    var fontName: String? = null

    /**
     * 字体大小
     */
      var fontSize = 25
}