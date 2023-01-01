package cn.hellohao.controller;

import cn.hellohao.auth.filter.SubjectFilter;
import cn.hellohao.model.entity.SysUser;
import cn.hellohao.service.UserService;
import cn.hellohao.util.Base64Encryption;
import cn.hutool.core.util.HexUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author yanni
 * @date time 2022/5/3 23:21
 * @modified By:
 */
@Controller
@RequestMapping("/")
public class PageController {
@Resource
    UserService userService;
@Value("${webhost}")
String webhost;
    @GetMapping("/home")

    public String indexPage(){
        return  "home";
    }
    @GetMapping(value = "/user/retrieve" ) //new
    public String retrieve(Model model, String activation,String cip) {
        Integer ret = 0;
        try {
            SysUser u2 = new SysUser();
            u2.setUid(activation);
            SysUser sysUser = userService.getUsers(u2);
            sysUser.setIsok(1);
            String new_pass = HexUtil.decodeHexStr(cip);//解密密码
            sysUser.setPassword(Base64Encryption.encryptBASE64(new_pass.getBytes()));
            String uid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            sysUser.setUid(uid);
            if (sysUser != null) {
                Integer r = userService.changeUser(sysUser);
                model.addAttribute("title","成功");
                model.addAttribute("name","新密码:"+new_pass);//
                model.addAttribute("note","密码已被系统重置，请即使登录修改你的新密码");
            } else {
                model.addAttribute("title","抱歉");
                model.addAttribute("name","为获取到用户信息");
                model.addAttribute("note","操作失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("title","抱歉");
            model.addAttribute("name","系统操作过程中发生错误");
            model.addAttribute("note","操作失败");
        }
        model.addAttribute("webhost", webhost);
        return "msg";
    }

    @GetMapping(  "/user/activation" )
    public String activation(Model model, @RequestParam("activation")String activation, @RequestParam("username") String username) {

        SysUser u2 = new SysUser();
        u2.setUid(activation);
        SysUser sysUser = userService.getUsers(u2);
        model.addAttribute("webhost", webhost);
        if (sysUser != null && sysUser.getIsok() == 0) {
            userService.getUserByUid(activation);
            model.addAttribute("title","激活成功");
            model.addAttribute("name","Hi~"+username);
            model.addAttribute("note","您的账号已成功激活看");
            return "msg";
        } else {
            model.addAttribute("title","操作无效");
            model.addAttribute("name","该页面为无效页面");
            model.addAttribute("note","请返回首页");
            return "msg";
        }
    }
}
