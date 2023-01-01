package cn.stylefeng.guns.sys.modular.user.controller;

import cn.stylefeng.guns.sys.modular.user.entity.SysUser;
import cn.stylefeng.guns.sys.modular.user.service.SysUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * @author yanni
 * @date time 2021/11/30 16:47
 * @modified By:
 */
@Controller
public class ActiveController {
    @Resource
    SysUserService sysUserService;
    @GetMapping("/active")
    public String active(Model model,Long uid){

        SysUser user = sysUserService.getById(uid);
        if (user != null) {

            user.setStatus(0);

            //model.addAttribute("webhost",SubjectFilter.WEBHOST);
            if (sysUserService.updateById(user) )  {

                model.addAttribute("title","激活成功");
                model.addAttribute("name","Hi~"+user.getAccount());
                model.addAttribute("note","您的账号已成功激活看");
                return "msg";
            } else {
                model.addAttribute("title","操作无效");
                model.addAttribute("name","该页面为无效页面");
                model.addAttribute("note","请返回首页");
                return "msg";
            }
        }


        return null;
    }
}
