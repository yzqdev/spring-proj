package cn.hellohao.controller

import cn.hellohao.model.entity.Msg
import cn.hellohao.service.impl.ClientService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-07-18 17:22
 */
@RestController
class ClientController(private val clientService: ClientService) {


    @PostMapping(value = ["/uploadbymail"])
    @ResponseBody
    fun uploadbymail(
        request: HttpServletRequest?,
        @RequestParam("file") file: MultipartFile?,
        mail: String?,
        pass: String?
    ): Msg {
        return clientService!!.uploadImg(request, file, mail, pass)
    }
}