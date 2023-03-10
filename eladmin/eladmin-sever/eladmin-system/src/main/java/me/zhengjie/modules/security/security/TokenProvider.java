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
package me.zhengjie.modules.security.security;
import java.util.Base64.Decoder;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;


import cn.hutool.jwt.Claims;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.security.config.bean.SecurityProperties;
import me.zhengjie.utils.RedisUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author /
 */
@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    private final SecurityProperties properties;
    private final RedisUtils redisUtils;
    public static final String AUTHORITIES_KEY = "user";

    private Algorithm algorithm;
    public TokenProvider(SecurityProperties properties, RedisUtils redisUtils) {
        this.properties = properties;
        this.redisUtils = redisUtils;
    }

    @Override
    public void afterPropertiesSet() {
        Decoder decoder=Base64.getMimeDecoder();
        byte[] keyBytes = decoder.decode(properties.getBase64Secret());
        algorithm = Algorithm.HMAC256(keyBytes);
    }

    /**
     * ??????Token ?????????????????????
     * Token ????????????????????????Redis ??????
     *
     * @param authentication /
     * @return /
     */
    public String createToken(Authentication authentication) {
        return JWT.create().withIssuer(IdUtil.simpleUUID())
                // ??????ID??????????????? Token ????????????

                .withClaim(AUTHORITIES_KEY, authentication.getName())
                .withSubject(authentication.getName())
                .sign(algorithm);
    }

    /**
     * ??????Token ??????????????????
     *
     * @param token /
     * @return /
     */
    Authentication getAuthentication(String token) {
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim>  claims =jwt.getClaims();
        User principal = new User(jwt.getSubject(), "******", new ArrayList<>());
        return new UsernamePasswordAuthenticationToken(principal, token, new ArrayList<>());
    }
    public   String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Map<String, Claim> result;
            result = jwt.getClaims();
            //??????!!!?????????toString??????????????????!!!?????????asString
            return result.get(TokenProvider.AUTHORITIES_KEY).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    /**
     * @param token ???????????????token
     */
    public void checkRenewal(String token) {
        // ??????????????????token,??????token???????????????
        long time = redisUtils.getExpire(properties.getOnlineKey() + token) * 1000;
        Date expireDate = DateUtil.offset(new Date(), DateField.MILLISECOND, (int) time);
        // ?????????????????????????????????????????????
        long differ = expireDate.getTime() - System.currentTimeMillis();
        // ?????????????????????????????????????????????
        if (differ <= properties.getDetect()) {
            long renew = time + properties.getRenew();
            redisUtils.expire(properties.getOnlineKey() + token, renew, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * ??????????????????token??????
     * @param request token???
     * @return
     */
    public String getToken(HttpServletRequest request) {
        final String requestHeader = request.getHeader(properties.getHeader());
        if (requestHeader != null && requestHeader.startsWith(properties.getTokenStartWith())) {
            return requestHeader.substring(7);
        }
        return null;
    }
}
