package cn.hellohao.util

import cn.hellohao.service.impl.ImgServiceImpl
import javax.servlet.http.HttpServletRequest


/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/11/6 17:35
 */
class GetIPS : Runnable {
    private var imgname: String? = null
    fun setImgname(imgname: String?) {
        this.imgname = imgname
    }

    override fun run() {
        val imgService: ImgServiceImpl =
            SpringContextHolder.Companion.getBean<ImgServiceImpl>(ImgServiceImpl::class.java)
    }

    companion object {
        fun runxc(imgnames: String?) {
            val getIPS = GetIPS()
            getIPS.setImgname(imgnames)
            val thread = Thread(getIPS)
            thread.start()
        }

        /*** * 用于测试跨域 * @author huangweii * 2015年5月29日  */
        @kotlin.jvm.JvmStatic
        fun getIpAddr(request: HttpServletRequest): String {
            var ip = request.getHeader("X-Forwarded-For")
            if (StringUtils.isNotEmpty(ip) && !"unKnown".equals(ip, ignoreCase = true)) {
                //多次反向代理后会有多个ip值，第一个ip才是真实ip
                val index = ip.indexOf(",")
                return if (index != -1) {
                    ip.substring(0, index)
                } else {
                    ip
                }
            }
            ip = request.getHeader("X-Real-IP")
            return if (StringUtils.isNotEmpty(ip) && !"unKnown".equals(ip, ignoreCase = true)) {
                ip
            } else request.remoteAddr
        }
    }
}