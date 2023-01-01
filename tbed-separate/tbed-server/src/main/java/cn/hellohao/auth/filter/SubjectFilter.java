package cn.hellohao.auth.filter;

import cn.hellohao.auth.token.JWTUtil;
import cn.hellohao.auth.token.UserClaim;
import cn.hellohao.model.entity.SysUser;
import cn.hellohao.service.UserService;
import cn.hellohao.service.impl.UserServiceImpl;
import cn.hellohao.util.SpringContextHolder;
import cn.hutool.core.lang.Console;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2021/6/16 17:43
 */
@Slf4j
public class SubjectFilter extends BasicHttpAuthenticationFilter {

    public static String[] WEBHOST = null;
    private String CODE = "000";

    /**
     * 是访问允许
     *
     * @param request     请求
     * @param response    响应
     * @param mappedValue 映射值
     * @return boolean
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        UserService userService = SpringContextHolder.getBean(UserService.class);
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String serviceName = httpServletRequest.getServletPath();
        String Users_Origin = httpServletRequest.getHeader("usersOrigin");

        //验证前端域名
        if (httpServletRequest.getMethod().equals("POST") && !serviceName.contains("/api") && !serviceName.contains("/verifyCode")) {
            try {

                //todo 这里禁止其他网站使用我们的api
                //for (String item:  WEBHOST) {
                //    if(Users_Origin.compareTo(SecureUtil.md5(item))!=0){
                //        System.out.println("前端域名校验未通过");
                //        System.out.println("request-MD5:"+Users_Origin);
                //        System.out.println("配置文件-MD5:"+SecureUtil.md5(item));
                //        System.out.println("配置Host:"+item);
                //        this.CODE = "406";
                //        return false;
                //    }
                //}


            } catch (Exception e) {
                e.printStackTrace();
                this.CODE = "500";
                return false;
            }
        }
        Console.log("service-name=>{}",serviceName);
        Console.print("http=>{}",httpServletRequest.getParameter("username"));
if (serviceName.contains("/user/activation")){
    return  true;
}
        String token = httpServletRequest.getHeader("Authorization");
        Console.error("从filter获取token {}", token);
        UserClaim jsonObject = JWTUtil.checkToken(token);
        Console.error(jsonObject.toString());
        if (Boolean.FALSE.equals(jsonObject.getCheck())) {
            if (!serviceName.contains("admin")) {
                return true;
            } else {
                this.CODE = "403";
                return false;
            }
        } else {
            Subject subject = SecurityUtils.getSubject();
            SysUser sysUser = (SysUser) subject.getPrincipal();
            if (sysUser == null) {
                UsernamePasswordToken tokenOBJ = new UsernamePasswordToken(jsonObject.getEmail(), jsonObject.getPassword());
                tokenOBJ.setRememberMe(true);
                try {
                    subject.login(tokenOBJ);
                    //一小时
                    SecurityUtils.getSubject().getSession().setTimeout(3600000);
                } catch (Exception e) {
                    log.info("拦截器，登录失败");
                    this.CODE = "403";
                    return false;
                }
            } else {
                if (null != sysUser) {
                    try {
                        if (null != sysUser.getId()) {
                            if (userService.getUsers(sysUser).getIsok() < 1) {
                                subject.logout();
                                this.CODE = "403";
                                return false;
                            }
                        }
                    } catch (Exception e) {
                        Console.log("拦截器判断用户isOK的时候报错了");
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    /**
     * 在访问被拒绝
     *
     * @param request     请求
     * @param response    响应
     * @param mappedValue 映射值
     * @return boolean
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) {
        String info = "未知错误";
        try {
            switch (this.CODE) {
                case "406" -> info = "前端域名配置不正确";
                case "403" -> info = "当前用户无权访问该请求";
                case "402" -> info = "当前web请求不合规";
                default -> info = "default";
            }
            Console.log("拦截器False-" + info);
            response.setContentType("application/json;charset=UTF-8");
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", this.CODE);
            jsonObject.put("info", info);
            response.getWriter().write(jsonObject.toJSONString());
        } catch (Exception e) {
            Console.log("返回token验证失败403请求，报异常了");
        }

        return false;
    }

}