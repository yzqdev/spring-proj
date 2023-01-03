package org.javaboy.vhr.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.RequestBody

/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 * @时间 2020-03-01 13:07
 */
@RestController
class HrInfoController {
    @Autowired
    var hrService: HrService? = null

    @Value("\${fastdfs.nginx.host}")
    var nginxHost: String? = null
    @GetMapping("/hr/info")
    fun getCurrentHr(authentication: Authentication): Hr {
        return authentication.getPrincipal() as Hr
    }

    @PutMapping("/hr/info")
    fun updateHr(@RequestBody hr: Hr?, authentication: Authentication): RespBean {
        if (hrService.updateHr(hr) == 1) {
            SecurityContextHolder.getContext().setAuthentication(
                UsernamePasswordAuthenticationToken(
                    hr,
                    authentication.getCredentials(),
                    authentication.getAuthorities()
                )
            )
            return RespBean.ok("更新成功!")
        }
        return RespBean.error("更新失败!")
    }

    @PutMapping("/hr/pass")
    fun updateHrPasswd(@RequestBody info: Map<String?, Any?>): RespBean {
        val oldpass = info["oldpass"] as String?
        val pass = info["pass"] as String?
        val hrid = info["hrid"] as Int?
        return if (hrService.updateHrPasswd(oldpass, pass, hrid)) {
            RespBean.ok("更新成功!")
        } else RespBean.error("更新失败!")
    }

    @PostMapping("/hr/userface")
    fun updateHrUserface(file: MultipartFile?, id: Int?, authentication: Authentication): RespBean {
        val fileId: String = FastDFSUtils.upload(file)
        val url = nginxHost + fileId
        if (hrService.updateUserface(url, id) == 1) {
            val hr: Hr = authentication.getPrincipal() as Hr
            hr.userface = url
            SecurityContextHolder.getContext().setAuthentication(
                UsernamePasswordAuthenticationToken(
                    hr,
                    authentication.getCredentials(),
                    authentication.getAuthorities()
                )
            )
            return RespBean.ok("更新成功!", url)
        }
        return RespBean.error("更新失败!")
    }
}