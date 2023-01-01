/*
Copyright [2020] [https://www.stylefeng.cn]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：

1.请不要删除和修改根目录下的LICENSE文件。
2.请不要删除和修改Guns源码头部的版权声明。
3.请保留源码和相关描述文件的项目出处，作者声明等。
4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns-separation
5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns-separation
6.若您的项目无法满足以上几点，可申请商业授权，获取Guns商业授权许可，请在官网购买授权，地址为 https://www.stylefeng.cn
 */
package cn.stylefeng.guns.sys.core.jwt;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.stylefeng.guns.core.context.constant.ConstantContextHolder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * JwtToken工具类
 *
 * @author xuyuxiang
 * @date 2020/3/12 17:39
 */
@Slf4j
public class JwtTokenUtil {

    /**
     * 生成token
     *
     * @author xuyuxiang
     * @date 2020/3/12 17:52
     */
    public static String generateToken(JwtPayLoad jwtPayLoad) {
        //私钥及加密算法
        Algorithm algorithm = Algorithm.HMAC512(ConstantContextHolder.getJwtSecret());
        DateTime expirationDate = DateUtil.offsetMillisecond(new Date(),
                Convert.toInt(ConstantContextHolder.getTokenExpireSec()) * 1000);
        return JWT.create()

                .withPayload(BeanUtil.beanToMap(jwtPayLoad))
                .withSubject(jwtPayLoad.getUserId().toString())
                .withIssuedAt(new Date())
                .withExpiresAt(expirationDate).sign(algorithm);

    }

    /**
     * 根据token获取Claims
     *
     * @author xuyuxiang
     * @date 2020/3/13 10:29
     */
    public static JwtPayLoad getClaimsFromToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        JwtPayLoad payLoad = new JwtPayLoad();

       Long userId = jwt.getClaim("userId").asLong();
        String uuid = jwt.getClaim("uuid").asString();
        payLoad.setUserId(userId);
        payLoad.setUuid( uuid);
        payLoad.setAccount( jwt.getClaim("account").asString());
        return payLoad;

    }



    /**
     * 校验token是否正确
     *
     * @author xuyuxiang
     * @date 2020/3/13 10:36
     */
    public static Boolean checkToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(ConstantContextHolder.getJwtSecret());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (JWTVerificationException jwtException) {
            return false;
        }
    }

    /**
     * 校验token是否失效
     *
     * @author xuyuxiang
     * @date 2020/3/13 10:30
     */
    public static Boolean isTokenExpired(String token) {
        try {


            Algorithm algorithm = Algorithm.HMAC512(ConstantContextHolder.getJwtSecret());
            DecodedJWT jwt = JWT.require(algorithm).build().verify(token);

            final Date expiration = jwt.getExpiresAt();
            log.info(String.valueOf(expiration));

            return expiration.before(new Date());
        } catch (TokenExpiredException expiredJwtException) {
            return true;
        }
    }
}
