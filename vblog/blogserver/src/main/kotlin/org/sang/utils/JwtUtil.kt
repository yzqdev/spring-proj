package org.sang.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.Claim
import org.sang.config.SecurityConstant
import java.util.*

/**
 * @author: Milogenius
 * @create: 2019-07-08 10:24
 * @description:
 */
object JwtUtil {
    /**
     * 过期时间为一天
     * TODO 正式上线更换为15分钟
     */
    private const val EXPIRE_TIME = (24 * 60 * 60 * 1000).toLong()

    /**
     * token私钥
     */
    private const val TOKEN_SECRET = "joijsdfjlsjfljfljl5135313135"

    /**
     * 生成签名,15分钟后过期
     *
     * @param username
     * @param userId
     * @return
     */
    fun sign(username: String?, auth: String?): String {
        //过期时间
        val date = Date(System.currentTimeMillis() + EXPIRE_TIME)
        //私钥及加密算法
        val algorithm = Algorithm.HMAC256(TOKEN_SECRET)
        //设置头信息
        val header = HashMap<String, Any>(2)
        header["typ"] = "JWT"
        header["alg"] = "HS256"
        //附带username和userID生成签名
        return JWT.create().withClaim("username", username)
            .withClaim("authorities", auth).withExpiresAt(date).sign(algorithm)
    }

    fun getUserAuth(token: String?): String? {
        return try {
            val jwt = JWT.decode(token)
            val result: Map<String?, Claim>
            println(jwt.claims)
            result = jwt.claims
            result[SecurityConstant.AUTHORITIES].toString()
        } catch (e: JWTDecodeException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 根据token获取用户名
     *
     * @param token
     * @return
     */
    fun getUserName(token: String?): String? {
        return try {
            val jwt = JWT.decode(token)
            val result: Map<String, Claim>
            println(jwt.claims)
            result = jwt.claims
            result["username"].toString()
        } catch (e: JWTDecodeException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 验证token
     *
     * @param token
     * @return
     */
    fun verifyToken(token: String?): Boolean {
        return try {
            val algorithm = Algorithm.HMAC256(TOKEN_SECRET)
            val verifier = JWT.require(algorithm).build()
            val jwt = verifier.verify(token)
            true
        } catch (e: IllegalArgumentException) {
            false
        } catch (e: JWTVerificationException) {
            false
        }
    }
}