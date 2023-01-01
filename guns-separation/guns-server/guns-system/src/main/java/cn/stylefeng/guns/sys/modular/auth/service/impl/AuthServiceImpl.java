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
package cn.stylefeng.guns.sys.modular.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailException;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.guns.core.consts.CommonConstant;
import cn.stylefeng.guns.core.context.constant.ConstantContextHolder;
import cn.stylefeng.guns.core.context.requestno.RequestNoContext;
import cn.stylefeng.guns.core.dbs.CurrentDataSourceContext;
import cn.stylefeng.guns.core.enums.CommonStatusEnum;
import cn.stylefeng.guns.core.exception.AuthException;
import cn.stylefeng.guns.core.exception.ServiceException;
import cn.stylefeng.guns.core.exception.enums.AuthExceptionEnum;
import cn.stylefeng.guns.core.exception.enums.ServerExceptionEnum;
import cn.stylefeng.guns.core.pojo.login.SysLoginUser;
import cn.stylefeng.guns.core.pojo.response.SuccessResponseData;
import cn.stylefeng.guns.core.tenant.context.TenantCodeHolder;
import cn.stylefeng.guns.core.tenant.context.TenantDbNameHolder;
import cn.stylefeng.guns.core.tenant.entity.TenantInfo;
import cn.stylefeng.guns.core.tenant.exception.TenantException;
import cn.stylefeng.guns.core.tenant.exception.enums.TenantExceptionEnum;
import cn.stylefeng.guns.core.tenant.service.TenantInfoService;
import cn.stylefeng.guns.core.util.HttpServletUtil;
import cn.stylefeng.guns.core.util.IpAddressUtil;
import cn.stylefeng.guns.sys.core.cache.UserCache;
import cn.stylefeng.guns.sys.core.enums.LogSuccessStatusEnum;
import cn.stylefeng.guns.sys.core.jwt.JwtPayLoad;
import cn.stylefeng.guns.sys.core.jwt.JwtTokenUtil;
import cn.stylefeng.guns.sys.core.log.LogManager;
import cn.stylefeng.guns.sys.core.redis.RedisUtils;
import cn.stylefeng.guns.sys.modular.auth.factory.LoginUserFactory;
import cn.stylefeng.guns.sys.modular.auth.service.AuthService;
import cn.stylefeng.guns.sys.modular.email.enums.SysEmailExceptionEnum;
import cn.stylefeng.guns.sys.modular.user.entity.SysUser;
import cn.stylefeng.guns.sys.modular.user.mapper.SysUserMapper;
import cn.stylefeng.guns.sys.modular.user.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 认证相关service实现类
 *
 * @author xuyuxiang
 * @date 2020/3/11 16:58
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService, UserDetailsService {

    private final JavaMailSender javaMailSender;

    private final SysUserService sysUserService;

    private final SysUserMapper sysUserMapper;
    @Value("${online-key}")
    String onlineKey;

    private final RedisUtils redisUtils;

    public AuthServiceImpl(SysUserService sysUserService, SysUserMapper sysUserMapper, RedisUtils redisUtils, JavaMailSender javaMailSender) {
        this.sysUserService = sysUserService;
        this.sysUserMapper = sysUserMapper;
        this.redisUtils = redisUtils;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public String login(String account, String password) {

        if (ObjectUtil.hasEmpty(account, password)) {
            LogManager.me().executeLoginLog(account, LogSuccessStatusEnum.FAIL.getCode(), AuthExceptionEnum.ACCOUNT_PWD_EMPTY.getMessage());
            throw new AuthException(AuthExceptionEnum.ACCOUNT_PWD_EMPTY);
        }

        SysUser sysUser = sysUserService.getUserByCount(account);

        //用户不存在，账号或密码错误
        if (ObjectUtil.isEmpty(sysUser)) {
            LogManager.me().executeLoginLog(account, LogSuccessStatusEnum.FAIL.getCode(), AuthExceptionEnum.ACCOUNT_PWD_ERROR.getMessage());
            throw new AuthException(AuthExceptionEnum.ACCOUNT_PWD_ERROR);
        }

        String passwordBcrypt = sysUser.getPassword();

        //验证账号密码是否正确
        if (ObjectUtil.isEmpty(passwordBcrypt) || !BCrypt.checkpw(password, passwordBcrypt)) {
            LogManager.me().executeLoginLog(sysUser.getAccount(), LogSuccessStatusEnum.FAIL.getCode(), AuthExceptionEnum.ACCOUNT_PWD_ERROR.getMessage());
            throw new AuthException(AuthExceptionEnum.ACCOUNT_PWD_ERROR);
        }

        return this.doLogin(sysUser);
    }

    /**
     * 注册
     *
     * @param username 用户名
     * @param email    电子邮件
     * @param password 密码
     * @return {@link SysUser}
     */
    @Override
    public SysUser register(String username, String email, String password) {
        if (ObjectUtil.hasEmpty(username, email, password)) {
            throw new AuthException(AuthExceptionEnum.NO_LOGIN_USER);
        } else {
            SysUser sysUser = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getAccount, username));
            if (sysUser != null) {
                throw new AuthException(AuthExceptionEnum.ACCOUNT_EXIST);
            }


            SysUser newUser = SysUser.builder().account(username).email(email).password(BCrypt.hashpw(password, BCrypt.gensalt())).name("默认名称").sex(1).adminType(2).status(1).build();
            sysUserMapper.insert(newUser);
            sendEmail(email, username);
            return newUser;
        }
    }


    /**
     * 从请求得到令牌
     *
     * @param request 请求
     * @return {@link String}
     */
    @Override
    public String getTokenFromRequest(HttpServletRequest request) {
        String authToken = request.getHeader(CommonConstant.AUTHORIZATION);
        if (ObjectUtil.isEmpty(authToken)) {
            return null;
        } else {
            //token不是以Bearer打头，则响应回格式不正确
            if (!authToken.startsWith(CommonConstant.TOKEN_TYPE_BEARER)) {
                throw new AuthException(AuthExceptionEnum.NOT_VALID_TOKEN_TYPE);
            }
            try {
                authToken = authToken.substring(CommonConstant.TOKEN_TYPE_BEARER.length() + 1);
            } catch (StringIndexOutOfBoundsException e) {
                throw new AuthException(AuthExceptionEnum.NOT_VALID_TOKEN_TYPE);
            }
        }

        return authToken;
    }

    @Override
    public SysLoginUser getLoginUserByToken(String token) {

        //校验token，错误则抛异常
        this.checkToken(token);

        //根据token获取JwtPayLoad部分
        JwtPayLoad jwtPayLoad = JwtTokenUtil.getClaimsFromToken(token);

        //从内存缓存中获取登录用户
        //Object cacheObject = userCache.get(jwtPayLoad.getUuid());
        //从redis缓存中获取登录用户
        LinkedHashMap<String, Object> cacheObject = (LinkedHashMap) redisUtils.get(onlineKey + jwtPayLoad.getUuid());
        //用户不存在则表示登录已过期
        if (ObjectUtil.isEmpty(cacheObject)) {
            throw new AuthException(AuthExceptionEnum.LOGIN_EXPIRED);
        }

        //转换成登录用户
        SysLoginUser sysLoginUser = BeanUtil.toBean(cacheObject, SysLoginUser.class);

        //用户存在, 无痛刷新缓存，在登录过期前活动的用户自动刷新缓存时间
        this.cacheLoginUser(jwtPayLoad, sysLoginUser);

        //返回用户
        return sysLoginUser;
    }

    @Override
    public void logout() {

        HttpServletRequest request = HttpServletUtil.getRequest();

        if (ObjectUtil.isNotNull(request)) {

            //获取token
            String token = this.getTokenFromRequest(request);

            //如果token为空直接返回
            if (ObjectUtil.isEmpty(token)) {
                return;
            }

            //校验token，错误则抛异常，待确定
            this.checkToken(token);

            //根据token获取JwtPayLoad部分
            JwtPayLoad jwtPayLoad = JwtTokenUtil.getClaimsFromToken(token);

            //获取缓存的key
            String loginUserCacheKey = jwtPayLoad.getUuid();
            this.clearUser(loginUserCacheKey, jwtPayLoad.getAccount());

        } else {
            throw new ServiceException(ServerExceptionEnum.REQUEST_EMPTY);
        }
    }

    @Override
    public void setSpringSecurityContextAuthentication(SysLoginUser sysLoginUser) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        sysLoginUser,
                        null,
                        sysLoginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public void checkToken(String token) {
        //校验token是否正确
        Boolean tokenCorrect = JwtTokenUtil.checkToken(token);
        if (Boolean.FALSE.equals(tokenCorrect)) {
            throw new AuthException(AuthExceptionEnum.REQUEST_TOKEN_ERROR);
        }

        //校验token是否失效
        Boolean tokenExpired = JwtTokenUtil.isTokenExpired(token);
        if (Boolean.TRUE.equals(tokenExpired)) {
            throw new AuthException(AuthExceptionEnum.LOGIN_EXPIRED);
        }
    }

    @Override
    public void cacheTenantInfo(String tenantCode) {
        if (StrUtil.isBlank(tenantCode)) {
            return;
        }

        // 从spring容器中获取service，如果没开多租户功能，没引入相关包，这里会报错
        TenantInfoService tenantInfoService = null;
        try {
            tenantInfoService = SpringUtil.getBean(TenantInfoService.class);
        } catch (Exception e) {
            throw new TenantException(TenantExceptionEnum.TENANT_MODULE_NOT_ENABLE_ERROR);
        }

        // 获取租户信息
        TenantInfo tenantInfo = tenantInfoService.getByCode(tenantCode);
        if (tenantInfo != null) {
            String dbName = tenantInfo.getDbName();

            // 租户编码的临时存放
            TenantCodeHolder.put(tenantCode);

            // 租户的数据库名称临时缓存
            TenantDbNameHolder.put(dbName);

            // 数据源信息临时缓存
            CurrentDataSourceContext.setDataSourceType(dbName);
        } else {
            throw new TenantException(TenantExceptionEnum.CNAT_FIND_TENANT_ERROR);
        }
    }

    @Override
    public SysLoginUser loadUserByUsername(String account) throws UsernameNotFoundException {
        SysLoginUser sysLoginUser = new SysLoginUser();
        SysUser user = sysUserService.getUserByCount(account);
        BeanUtil.copyProperties(user, sysLoginUser);
        return sysLoginUser;
    }

    @Value("${spring.mail.username}")
    private String from;


    /**
     * 发送电子邮件
     *
     * @param email 电子邮件
     */
    public void sendEmail(String email, String username) {

        String[] to = {email};
        if (ObjectUtil.isEmpty(to)) {
            throw new ServiceException(SysEmailExceptionEnum.EMAIL_TO_EMPTY);
        }

        String domain = ConstantContextHolder.getSysConfig("domain", String.class, true);
        PebbleEngine engine = new PebbleEngine.Builder().build();
        ClassPathResource classPathResource = new ClassPathResource("emailTemplate/emailRegister.html");
        PebbleTemplate compiledTemplate = engine.getTemplate(classPathResource.getPath());
        Map<String, Object> context = new HashMap<>();
        context.put("username", username);
        context.put("webname", "guns");
        Long uid = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getAccount, username)).getId();
        context.put("url", domain + "/active?uid=" + uid + "&username=" + username);

        try {
            Writer writer = new StringWriter();
            compiledTemplate.evaluate(writer, context);
            String content = writer.toString();
            if (ObjectUtil.isEmpty(content)) {
                throw new ServiceException(SysEmailExceptionEnum.EMAIL_CONTENT_EMPTY);
            }

            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("主题：验证邮件！");
            helper.setText(content, true);

            javaMailSender.send(message);

            new SuccessResponseData("发送成功!");

        } catch (MailException | MessagingException | IOException e) {
            log.error(">>> 邮件发送异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
            throw new ServiceException(SysEmailExceptionEnum.EMAIL_SEND_ERROR);
        }

    }

    /**
     * 根据key清空登陆信息
     *
     * @author xuyuxiang
     * @date 2020/6/19 12:28
     */
    private void clearUser(String loginUserKey, String account) {
        //获取缓存的用户
        //Object cacheObject = userCache.get(loginUserKey);
        // 获取redis缓存的用户
        Object cacheObject = redisUtils.get(onlineKey + loginUserKey);

        //如果缓存的用户存在，清除会话，否则表示该会话信息已失效，不执行任何操作
        if (ObjectUtil.isNotEmpty(cacheObject)) {
            //清除登录会话
            //userCache.remove(loginUserKey);
            //清除redis登录会话
            redisUtils.del(onlineKey + loginUserKey);
            //创建退出登录日志
            LogManager.me().executeExitLog(account);
        }
    }

    /**
     * 执行登录方法
     *
     * @author xuyuxiang
     * @date 2020/3/12 10:43
     */
    private String doLogin(SysUser sysUser) {

        Integer sysUserStatus = sysUser.getStatus();

        //验证账号是否被冻结
        if (CommonStatusEnum.DISABLE.getCode().equals(sysUserStatus)) {
            LogManager.me().executeLoginLog(sysUser.getAccount(), LogSuccessStatusEnum.FAIL.getCode(), AuthExceptionEnum.ACCOUNT_FREEZE_ERROR.getMessage());
            throw new AuthException(AuthExceptionEnum.ACCOUNT_FREEZE_ERROR);
        }

        //构造SysLoginUser
        SysLoginUser sysLoginUser = this.genSysLoginUser(sysUser);

        //构造jwtPayLoad
        JwtPayLoad jwtPayLoad = new JwtPayLoad(sysUser.getId(), sysUser.getAccount());

        //生成token
        String token = JwtTokenUtil.generateToken(jwtPayLoad);
        log.info("登陆中");
        log.info(String.valueOf(jwtPayLoad));
        log.info(String.valueOf(sysLoginUser));
        //缓存token与登录用户信息对应, 默认2个小时
        this.cacheLoginUser(jwtPayLoad, sysLoginUser);

        //设置最后登录ip和时间
        sysUser.setLastLoginIp(IpAddressUtil.getIp(HttpServletUtil.getRequest()));
        sysUser.setLastLoginTime(LocalDateTime.now());

        //更新用户登录信息
        sysUserService.updateById(sysUser);

        //登录成功，记录登录日志
        LogManager.me().executeLoginLog(sysUser.getAccount(), LogSuccessStatusEnum.SUCCESS.getCode(), null);

        //登录成功，设置SpringSecurityContext上下文，方便获取用户
        this.setSpringSecurityContextAuthentication(sysLoginUser);

        //如果开启限制单用户登陆，则踢掉原来的用户
        Boolean enableSingleLogin = ConstantContextHolder.getEnableSingleLogin();
        if (Boolean.TRUE.equals(enableSingleLogin)) {

            //获取所有的登陆用户
            //Map<String, SysLoginUser> allLoginUsers = userCache.getAllKeyValues();
            //for (Map.Entry<String, SysLoginUser> loginedUserEntry : allLoginUsers.entrySet()) {
            //
            //    String loginedUserKey = loginedUserEntry.getKey();
            //    SysLoginUser loginedUser = loginedUserEntry.getValue();
            //
            //    //如果账号名称相同，并且redis缓存key和刚刚生成的用户的uuid不一样，则清除以前登录的
            //    if (loginedUser.getName().equals(sysUser.getName())
            //            && !loginedUserKey.equals(jwtPayLoad.getUuid())) {
            //        this.clearUser(loginedUserKey, loginedUser.getAccount());
            //    }
            //}
            //------------------------------------------------使用redis
            List<String> keys = redisUtils.scan(onlineKey + "*");
            for (String value : keys) {
                SysLoginUser loginUser = (SysLoginUser) redisUtils.get(value);
                if (loginUser.getName().equals(sysUser.getName()) && !value.equals(jwtPayLoad.getUuid())) {
                    this.clearUser(value, loginUser.getAccount());
                }
            }

        }

        //返回token
        return token;
    }

    /**
     * 构造登录用户信息
     *
     * @author xuyuxiang
     * @date 2020/3/12 17:32
     */
    @Override
    public SysLoginUser genSysLoginUser(SysUser sysUser) {
        SysLoginUser sysLoginUser = new SysLoginUser();
        BeanUtil.copyProperties(sysUser, sysLoginUser);
        LoginUserFactory.fillLoginUserInfo(sysLoginUser);
        return sysLoginUser;
    }

    /**
     * 缓存token与登录用户信息对应, 默认24个小时
     *
     * @author xuyuxiang
     * @date 2020/3/13 14:51
     */
    private void cacheLoginUser(JwtPayLoad jwtPayLoad, SysLoginUser sysLoginUser) {
        String redisLoginUserKey = jwtPayLoad.getUuid();
        //内存缓存
        //userCache.put(redisLoginUserKey, sysLoginUser);
        //下面是redis
        redisUtils.set(onlineKey + redisLoginUserKey, sysLoginUser, ConstantContextHolder.getSessionTokenExpireSec());
    }
}
