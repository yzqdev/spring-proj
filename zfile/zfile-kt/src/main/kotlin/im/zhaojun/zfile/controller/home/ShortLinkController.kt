package im.zhaojun.zfile.controller.home

import cn.hutool.core.util.RandomUtil
import cn.hutool.core.util.URLUtil
import im.zhaojun.zfile.model.constant.ZFileConstant
import im.zhaojun.zfile.model.dto.ShortLinkDto
import im.zhaojun.zfile.model.dto.SystemConfigDTO
import im.zhaojun.zfile.model.entity.ShortLinkConfig
import im.zhaojun.zfile.model.support.ResultBean
import im.zhaojun.zfile.service.SystemConfigService
import im.zhaojun.zfile.serviceimport.ShortLinkConfigService
import im.zhaojun.zfile.util.StringUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import javax.annotation.Resource

/**
 * 短链 Controller
 * @author zhao
 */
@Controller
class ShortLinkController(private val systemConfigService: SystemConfigService, private val shortLinkConfigService: ShortLinkConfigService) {



    @PostMapping("/api/short-link")
    @ResponseBody
    fun shortLink(driveId: String?, path: String?): ResultBean {
        val systemConfig: SystemConfigDTO = systemConfigService.systemConfig()
        val domain: String = systemConfig.domain!!
        // 拼接直链地址.
        val fullPath = StringUtils.concatUrl(StringUtils.DELIMITER_STR, ZFileConstant.DIRECT_LINK_PREFIX, driveId, path)
        var shortLinkConfig: ShortLinkConfig? = shortLinkConfigService.findByUrl(fullPath)
        if (shortLinkConfig == null) {
            var randomKey: String
            do {
                // 获取短链
                randomKey = RandomUtil.randomString(6)
                shortLinkConfig = shortLinkConfigService.findByKey(randomKey)
            } while (shortLinkConfig != null)
            shortLinkConfig = ShortLinkConfig().apply {
                key=randomKey
                url=fullPath
            }

            shortLinkConfigService.save(shortLinkConfig)
        }
        val shortUrl = StringUtils.removeDuplicateSeparator(domain + "/s/" + shortLinkConfig.key)
        return ResultBean.successData(shortUrl)
    }

    @GetMapping("/s/{key}")
    fun parseShortKey(@PathVariable key: String?): String {
        val shortLinkConfig: ShortLinkConfig = shortLinkConfigService.findByKey(key)
            ?: throw RuntimeException("此直链不存在或已失效.")
        val systemConfig: SystemConfigDTO = systemConfigService.systemConfig()
        val domain: String = systemConfig.domain!!
        val url = URLUtil.encode(StringUtils.removeDuplicateSeparator(domain + shortLinkConfig.url))
        return "redirect:$url"
    }

    @GetMapping("admin/api/short-link/key")
    @ResponseBody
    fun updateShortKey(id: Int?, newKey: String?): ResultBean {
        val newShortLinkConfig: ShortLinkConfig = shortLinkConfigService.findByKey(newKey)
        if (newShortLinkConfig != null) {
            throw RuntimeException("您输入的 Key 已存在，请重新输入")
        }
        val shortLinkConfig: ShortLinkConfig = shortLinkConfigService.findById(id)
            ?: throw RuntimeException("此直链不存在或已失效.")
        shortLinkConfig.key=newKey
        shortLinkConfigService.save(shortLinkConfig)
        return ResultBean.success()
    }

    /**
     * 批量删除直链
     */
    @PostMapping("/admin/api/short-link")
    @ResponseBody
    fun batchDelete(@RequestBody shortLinkDto: ShortLinkDto): ResultBean {
        for (id in shortLinkDto.ids) {
            shortLinkConfigService.deleteById(id)
        }
        return ResultBean.success()
    }
}