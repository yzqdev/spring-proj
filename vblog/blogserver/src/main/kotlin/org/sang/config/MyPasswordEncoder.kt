package org.sang.config

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.util.DigestUtils

/**
 * @作者 江南一点雨
 * @微信公众号 江南一点雨
 * @网站 http://www.itboyhub.com
 * @国际站 http://www.javaboy.org
 * @微信 a_java_boy
 * @GitHub https://github.com/lenve
 * @Gitee https://gitee.com/lenve
 */
@Component
class MyPasswordEncoder : PasswordEncoder {
    override fun encode(rawPassword: CharSequence): String {
        return DigestUtils.md5DigestAsHex(rawPassword.toString().toByteArray())
    }

    override fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean {
        return encodedPassword == DigestUtils.md5DigestAsHex(rawPassword.toString().toByteArray())
    }
}