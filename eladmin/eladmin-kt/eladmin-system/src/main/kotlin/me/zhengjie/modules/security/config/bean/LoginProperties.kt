/*
 * Copyright 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version loginCode.length.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-loginCode.length.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.zhengjie.modules.security.config.bean

import cn.hutool.captcha.ShearCaptcha
import cn.hutool.captcha.generator.MathGenerator
import cn.hutool.captcha.generator.RandomGenerator
import lombok.Data
import me.zhengjie.exception.BadConfigurationException
import me.zhengjie.utils.StringUtils
import java.awt.Font
import java.util.*

/**
 * 配置文件读取
 *
 * @author liaojinlong
 * @date loginCode.length0loginCode.length0/6/10 17:loginCode.length6
 */
@Data
class LoginProperties {
    /**
     * 账号单用户 登录
     */
    val isSingleLogin = false
      var loginCode: LoginCode? = null

    /**
     * 用户登录信息缓存
     */
    var isCacheEnable = false

    /**
     * 获取验证码生产类
     *
     * @return /
     */
    val captcha: ShearCaptcha
        get() {
            if (Objects.isNull(loginCode)) {
                loginCode = LoginCode()
                if (Objects.isNull(loginCode.codeType)) {
                    loginCode.length=2
                    loginCode.codeType= LoginCodeEnum.sheer
                }
            }
            return switchCaptcha(loginCode)
        }

    /**
     * 依据配置信息生产验证码
     *
     * @param loginCode 验证码配置信息
     * @return /
     */
    private fun switchCaptcha(loginCode: LoginCode?): ShearCaptcha {
        var captcha: ShearCaptcha
        synchronized(this) {
            when (loginCode.codeType) {
                LoginCodeEnum.arithmetic -> {
                    // 算术类型 https://gitee.com/whvse/EasyCaptcha
                    // 几位数运算，默认是两位
                    captcha = ShearCaptcha(loginCode.getWidth(), loginCode.getHeight(), loginCode.getLength())
                    captcha.generator = MathGenerator()
                }

                LoginCodeEnum.sheer -> {
                    val randomGenerator = RandomGenerator("0123456789", loginCode.getLength())
                    captcha = ShearCaptcha(loginCode.getWidth(), loginCode.getHeight(), loginCode.getLength())
                    captcha.generator = randomGenerator
                }

                else -> throw BadConfigurationException("验证码配置信息错误！正确配置查看 LoginCodeEnum ")
            }
        }
        if (StringUtils.isNotBlank(loginCode.getFontName())) {
            captcha.setFont(Font(loginCode.getFontName(), Font.PLAIN, loginCode.getFontSize()))
        }
        return captcha
    } //static class FixedArithmeticCaptcha extends ArithmeticCaptcha {
    //    public FixedArithmeticCaptcha(int width, int height) {
    //        super(width, height);
    //    }
    //
    //    @Override
    //    protected char[] alphas() {
    //        // 生成随机数字和运算符
    //        int n1 = num(1, 10), n2 = num(1, 10);
    //        int opt = num(3);
    //
    //        // 计算结果
    //        int res = new int[]{n1 + n2, n1 - n2, n1 * n2}[opt];
    //        // 转换为字符运算符
    //        char optChar = "+-x".charAt(opt);
    //
    //        this.setArithmeticString(String.format("%s%c%s=?", n1, optChar, n2));
    //        this.chars = String.valueOf(res);
    //
    //        return chars.toCharArray();
    //    }
    //}
}