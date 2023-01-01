package cn.hellohao.auth.token;

import cn.hellohao.model.entity.SysUser;
import cn.hutool.core.lang.Console;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2021/6/11 18:50
 */
public class JWTUtil {

    private static String EXPIRE_TIME = "";
    private static String SECRET = "www.hellohao.cn";

    public static String createToken(SysUser sysUser){
        Calendar calendar = Calendar.getInstance();
        //单位秒，604800 为7天
        calendar.add(Calendar.SECOND,604800 );
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        Console.error(sysUser.toString());
        return JWT.create()
                .withClaim("email", sysUser.getEmail())
                .withClaim("username", sysUser.getUsername())
                .withClaim("uid", sysUser.getUid())
                .withClaim("password", sysUser.getPassword())
                .withExpiresAt(calendar.getTime())
                .sign(algorithm);
    }

    public static UserClaim checkToken(String token){
        //验证对象
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        UserClaim jsonObject=new UserClaim();
        if(null==token){
            jsonObject.setCheck(false);
            return jsonObject;
        }
        try {

            DecodedJWT verify = jwtVerifier.verify(token);
            Date expiresAt = verify.getExpiresAt();
            jsonObject.setCheck(true);
            jsonObject.setEmail(verify.getClaim("email").asString());
            jsonObject.setPassword(verify.getClaim("password").asString());
            jsonObject.setUid(verify.getClaim("uid").asString());
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            Console.log("token认证已过期，请重新登录获取");
            jsonObject.setCheck(false);
        }catch (Exception e){
            e.printStackTrace();
            Console.log("token无效");
            jsonObject.setCheck(false);
        }
        return jsonObject;
    }



}
