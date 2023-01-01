package im.zhaojun.zfile.controller.admin

import im.zhaojun.zfile.model.support.ResultBean
import im.zhaojun.zfile.serviceimport.ShortLinkConfigService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import javax.annotation.Resource

/**
 * 直链管理 Controller
 *
 * @author zhaojun
 */
@Controller
@RequestMapping("/admin")
class ShortLinkManagerController {
    @Resource
    private val shortLinkConfigService: ShortLinkConfigService? = null
    @GetMapping("/link/list")
    @ResponseBody
    fun list(
        key: String?,
        url: String?,
        dateFrom: String?,
        dateTo: String?,
        page: Int?,
        limit: Int?,
        @RequestParam(required = false, defaultValue = "createDate") orderBy: String?,
        @RequestParam(required = false, defaultValue = "desc") orderDirection: String?
    ): ResultBean {
        return ResultBean.success(
            shortLinkConfigService.find(
                key,
                url,
                dateFrom,
                dateTo,
                page,
                limit,
                orderBy,
                orderDirection
            )
        )
    }

    @DeleteMapping("/link/delete/{id}")
    @ResponseBody
    fun deleteById(@PathVariable id: Int?): ResultBean {
        shortLinkConfigService.deleteById(id)
        return ResultBean.success()
    }
}