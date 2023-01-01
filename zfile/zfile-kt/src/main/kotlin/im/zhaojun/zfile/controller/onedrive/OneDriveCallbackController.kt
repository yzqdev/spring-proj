package im.zhaojun.zfile.controller.onedrive

import im.zhaojun.zfile.model.support.OneDriveToken
import im.zhaojun.zfile.service.impl.OneDriveChinaServiceImpl
import im.zhaojun.zfile.service.impl.OneDriveServiceImpl
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.annotation.Resource

/**
 * @author zhaojun
 */
@Controller
@RequestMapping(value = ["/onedrive", "/onedirve"])
class OneDriveCallbackController(private val oneDriveServiceImpl: OneDriveServiceImpl, private val oneDriveChinaServiceImpl: OneDriveChinaServiceImpl) {

    @GetMapping("/callback")
    fun oneDriveCallback(code: String?, model: Model): String {
        val oneDriveToken: OneDriveToken = oneDriveServiceImpl.getToken(code)
        model.addAttribute("accessToken", oneDriveToken.getAccessToken())
        model.addAttribute("refreshToken", oneDriveToken.getRefreshToken())
        return "callback"
    }

    @GetMapping("/authorize")
    fun authorize(): String {
        return "redirect:https://login.microsoftonline.com/common/oauth2/v2.0/authorize?client_id=" + oneDriveServiceImpl!!.getClientId() +
                "&response_type=code&redirect_uri=" + oneDriveServiceImpl.getRedirectUri() +
                "&scope=" + oneDriveServiceImpl.getScope()
    }

    @GetMapping("/china-callback")
    fun oneDriveChinaCallback(code: String?, model: Model): String {
        val oneDriveToken: OneDriveToken = oneDriveChinaServiceImpl.getToken(code)
        model.addAttribute("accessToken", oneDriveToken.getAccessToken())
        model.addAttribute("refreshToken", oneDriveToken.getRefreshToken())
        return "callback"
    }

    @GetMapping("/china-authorize")
    fun authorizeChina(): String {
        return "redirect:https://login.chinacloudapi.cn/common/oauth2/v2.0/authorize?client_id=" + oneDriveChinaServiceImpl!!.getClientId() +
                "&response_type=code&redirect_uri=" + oneDriveChinaServiceImpl.getRedirectUri() +
                "&scope=" + oneDriveChinaServiceImpl.getScope()
    }
}