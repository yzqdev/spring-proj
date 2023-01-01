package cn.hellohao.controller;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import cn.hellohao.auth.filter.SubjectFilter;
import cn.hellohao.auth.token.JWTUtil;
import cn.hellohao.auth.token.UserClaim;
import cn.hellohao.config.SysName;
import cn.hellohao.model.dto.UserFind;
import cn.hellohao.model.entity.*;
import cn.hellohao.model.dto.UserLoginDto;
import cn.hellohao.service.*;
import cn.hellohao.util.*;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.HexUtil;
import cn.hutool.log.StaticLog;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private EmailConfigService emailConfigService;
    @Resource
    private ConfigService configService;
    @Resource
    private UploadConfigService uploadConfigService;
    @Resource
    private SysConfigService sysConfigService;
  @Value("${webhost}")
  String webhost;
    @Resource
    IRedisService iRedisService;

    @PostMapping("/register")
    @ResponseBody
    public Msg Register(@RequestBody UserLoginDto userLoginDto) {//Validated
        Msg msg = new Msg();
        HttpServletRequest request=RequestHelper.getRequest();

        String username = userLoginDto.getUsername();
        String email = userLoginDto.getEmail();
        String password = Base64Encryption.encryptBASE64(userLoginDto.getPassword().getBytes());
        String userIP = GetIPS.getIpAddr(request);
        String verifyCodeForRegister = userLoginDto.getVerifyCode();
        Object redis_verifyCodeForRegister = iRedisService.getValue(userIP+"_hellohao_verifyCodeForRegister");
        if(!SetText.checkEmail(email)){
            msg.setCode("110403");
            msg.setInfo("邮箱格式不正确");
            return msg;
        }
        String regex = "^\\w+$";
        if(username.length()>20 || !username.matches (regex)){
            msg.setCode("110403");
            msg.setInfo("用户名不得超过20位字符");
            return msg;
        }
        if(null==redis_verifyCodeForRegister){
            msg.setCode("4035");
            msg.setInfo("验证码已失效，请重新弄获取。");
            return msg;
        }else if(null==verifyCodeForRegister){
            msg.setCode("4036");
            msg.setInfo("验证码不能为空。");
            return msg;
        }
        if((redis_verifyCodeForRegister.toString().toLowerCase()).compareTo((verifyCodeForRegister.toLowerCase()))==0){
            SysUser sysUser = new SysUser();
            UploadConfig updateConfig = uploadConfigService.getUpdateConfig();
            EmailConfig emailConfig = emailConfigService.getOne(new LambdaQueryWrapper<EmailConfig>().eq(EmailConfig::getId,"1"));
            Integer countusername = userService.countusername(username);
            Integer countmail = userService.countmail(email);
            SysConfig sysConfig = sysConfigService.getstate();
            if(sysConfig.getRegister()!=1){
                msg.setCode("110403");
                msg.setInfo("本站已暂时关闭用户注册功能");
                return msg;
            }
            if(countusername == 1 || !SysName.CheckSysName(username)){
                msg.setCode("110406");
                msg.setInfo("此用户名已存在");
                return msg;
            }
            if(countmail == 1){
                msg.setCode("110407");
                msg.setInfo("此邮箱已被注册");
                return msg;
            }
            String uid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            sysUser.setLevel(1);
            sysUser.setUid(uid);
            sysUser.setBirthday(LocalDateTime.now());
            sysUser.setMemory(updateConfig.getUserStorage());
            sysUser.setGroupId("1");
            sysUser.setEmail(email);
            sysUser.setUsername(username);
            sysUser.setPassword(password);
            sysUser.setCreateTime( LocalDateTime.now());
            sysUser.setUpdateTime( LocalDateTime.now());
            Config config = configService.getSourceType();
            Integer type = 0;
            Console.log("email=>{}",emailConfig);
            if(emailConfig!=null&&emailConfig.getEnable() ){
                sysUser.setIsok(0);
                Thread thread = new Thread(() -> {
                    Integer a = NewSendEmail.sendEmail(emailConfig, sysUser.getUsername(), uid, sysUser.getEmail(),config);
                });
                thread.start();
                msg.setInfo("注册成功,请注意查收邮箱尽快激活账户");
            }else{
                sysUser.setIsok(1);
                msg.setInfo("注册成功,快去登陆吧");
            }
            userService.register(sysUser);
        }else{
            msg.setCode("110408");
            msg.setInfo("验证码不正确");//失效也要处理。
        }
        return msg;
    }

    @PostMapping("/login")
    @ResponseBody
    public Msg login(@RequestBody UserLoginDto userLoginDto) {
        Msg msg = new Msg();

        String email = userLoginDto.getEmail();
        String password = Base64Encryption.encryptBASE64(userLoginDto.getPassword().getBytes());
        String verifyCode = userLoginDto.getVerifyCode();
        String userIP = GetIPS.getIpAddr(RequestHelper.getRequest());
        Object redis_VerifyCode = iRedisService.getValue(userIP+"_hellohao_verifyCode");
        if(null==redis_VerifyCode){
            msg.setCode("4035");
            msg.setInfo("验证码已失效，请重新弄获取。");
            return msg;
        }else if(null==verifyCode){
            msg.setCode("4036");
            msg.setInfo("验证码不能为空。");
            return msg;
        }
        if((redis_VerifyCode.toString().toLowerCase()).compareTo((verifyCode.toLowerCase()))==0){
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken tokenOBJ = new UsernamePasswordToken(email,password);
            tokenOBJ.setRememberMe(true);
            try {
                subject.login(tokenOBJ);
                //一小时
                SecurityUtils.getSubject().getSession().setTimeout(3600000);
                JSONObject jsonObject = new JSONObject();
                SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
                if(sysUser.getIsok()==0){
                    msg.setInfo("你的账号暂未激活");
                    msg.setCode("110403");
                    return msg;
                }
                if(sysUser.getIsok()<0){
                    msg.setInfo("你的账户已被冻结");
                    msg.setCode("110403");
                    return msg;
                }
                StaticLog.warn(sysUser.toString());
                String token = JWTUtil.createToken(sysUser);

                UserClaim u=JWTUtil.checkToken(token);
                Console.error(u);
                Subject su = SecurityUtils.getSubject();
                System.out.println("当前用户角色：admin:"+su.hasRole("admin"));
                msg.setInfo("登录成功");
                jsonObject.put("token",token);
                jsonObject.put("RoleLevel", sysUser.getLevel()==2?"admin":"sysUser");
                jsonObject.put("userName", sysUser.getUsername());
                msg.setData(jsonObject);
                return msg;
            } catch (UnknownAccountException e) {
                //此异常说明用户名不存在
                msg.setCode("4000");
                msg.setInfo("登录邮箱不存在");
                System.err.println("邮箱不存在");
                e.printStackTrace();
                return msg;
            }catch (IncorrectCredentialsException e) {
                msg.setCode("4000");
                msg.setInfo("登录密码错误");
                System.err.println("密码不存在");
                e.printStackTrace();
                return msg;
            }catch (Exception e) {
                msg.setCode("5000");
                msg.setInfo("登录失败");
                System.err.println("登录失败");
                e.printStackTrace();
                return msg;
            }
        }else{
            msg.setCode("40034");
            msg.setInfo("验证码不正确");//失效也要处理。
        }
        return msg;
    }




    @PostMapping(value = "/logout")//new
    @ResponseBody
    public Msg exit(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        Msg msg = new Msg();
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        msg.setInfo("退出成功");
        Print.Normal("用户账号退出成功");
        return msg;
    }

    @PostMapping("/retrievePass") //new
    @ResponseBody
    public Msg retrievePass(HttpServletRequest request, @RequestBody UserFind userFind) {
        Msg msg = new Msg();
        try {

            String email =userFind.getEmail();
            String retrieveCode = userFind.getRetrieveCode();
            String userIP = GetIPS.getIpAddr(request);
            Object redis_verifyCodeForEmailRetrieve = iRedisService.getValue(userIP+"_hellohao_verifyCodeForEmailRetrieve");

            EmailConfig emailConfig = emailConfigService.getEmail();
            if(null==redis_verifyCodeForEmailRetrieve){
                msg.setCode("4035");
                msg.setInfo("验证码已失效，请重新弄获取。");
                return msg;
            }else if(null==retrieveCode){
                msg.setCode("4036");
                msg.setInfo("验证码不能为空。");
                return msg;
            }
            if((redis_verifyCodeForEmailRetrieve.toString().toLowerCase()).compareTo((retrieveCode.toLowerCase()))!=0){
                msg.setCode("40034");
                msg.setInfo("验证码不正确");
                return msg;
            }

            Integer ret = userService.countmail(email);
            if(ret>0){
                if(emailConfig.getEnable() ){
                    SysUser u2 = new SysUser();
                    u2.setEmail(email);
                    SysUser sysUser = userService.getUsers(u2);
                    if(sysUser.getIsok()==-1){
                        msg.setCode("110110");
                        msg.setInfo("当前用户已被冻结，禁止操作");
                        return msg;
                    }
                    Config config = configService.getSourceType();
                    Thread thread = new Thread(() -> {
                        Integer a = NewSendEmail.sendEmailFindPass(emailConfig, sysUser.getUsername(), sysUser.getUid(), sysUser.getEmail(),config);//SendEmail.sendEmailT(message, sysUser.getUsername(), sysUser.getUid(), sysUser.getEmail(),emailConfig,config);
                    });
                    thread.start();
                    msg.setInfo("重置密码的验证链接已发送至该邮箱，请前往邮箱验证并重置密码。【若长时间未收到邮件，请检查垃圾箱】");
                }else{
                    msg.setCode("400");
                    msg.setInfo("本站暂未开启邮箱服务，请联系管理员");
                }
            }else{
                msg.setCode("110404");
                msg.setInfo("未找到邮箱所在的用户");
            }
        }catch (Exception e){
            e.printStackTrace();
            msg.setCode("110500");
            msg.setInfo("系统发生错误");
        }
        return msg;
    }




}
