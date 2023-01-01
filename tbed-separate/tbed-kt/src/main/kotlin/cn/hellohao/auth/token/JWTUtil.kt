package cn.hellohao.auth.token

import cn.hellohao.model.entity.SysUser
import cn.hutool.core.lang.Console
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.TokenExpiredException
import java.util.*

/**
 * @author Hellohao
 * @version 1.0
 * @date 2021/6/11 18:50
 */
object JWTUtil {
    private const val EXPIRE_TIME = ""
    private const val SECRET = "www.hellohao.cn"
    fun createToken(sysUser: SysUser): String {
        val calendar = Calendar.getInstance()
        //单位秒，604800 为7天
        calendar.add(Calendar.SECOND, 604800)
        val algorithm = Algorithm.HMAC256(SECRET)
        Console.error(sysUser.toString())
        return JWT.create()
            .withClaim("email", sysUser.email)
            .withClaim("username", sysUser.username)
            .withClaim("uid", sysUser.uid)
            .withClaim("password", sysUser.password)
            .withExpiresAt(calendar.time)
            .sign(algorithm)
    }

    fun checkToken(token: String?): UserClaim {
        //验证对象
        val jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build()
        val jsonObject = UserClaim()
        if (null == token) {
            jsonObject.check = false
            return jsonObject
        }
        try {
            val verify = jwtVerifier.verify(token)
            val expiresAt = verify.expiresAt
            jsonObject.check = true
            jsonObject.email = verify.getClaim("email").asString()
            jsonObject.password = verify.getClaim("password").asString()
            jsonObject.uid = verify.getClaim("uid").asString()
        } catch (e: TokenExpiredException) {
            e.printStackTrace()
            Console.log("token认证已过期，请重新登录获取")
            jsonObject.check = false
        } catch (e: Exception) {
            e.printStackTrace()
            Console.log("token无效")
            jsonObject.check = false
        }
        return jsonObject
    }
}