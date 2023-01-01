package im.zhaojun.zfile.controller.onedrive

import cn.hutool.core.util.StrUtil
import cn.hutool.http.HttpUtil
import com.alibaba.fastjson.JSONObject
import im.zhaojun.zfile.model.dto.SharePointInfoVO
import im.zhaojun.zfile.model.support.ResultBean
import im.zhaojun.zfile.model.support.ResultBean.Companion.error
import im.zhaojun.zfile.model.support.ResultBean.Companion.successData
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

/**
 * @author zhaojun
 * SharePoint 工具类
 */
@Controller
@RequestMapping("/sharepoint")
class SharePointHelperController {
    /**
     * 根据 AccessToken 获取域名前缀
     */
    @PostMapping("/getDomainPrefix")
    @ResponseBody
    fun getDomainPrefix(@RequestBody sharePointInfoVO: SharePointInfoVO): ResultBean {
        var host = ""

        // 判断是标准版还是世纪互联版
        if (sharePointInfoVO.type == "Standard") {
            host = "graph.microsoft.com"
        } else if (sharePointInfoVO.type == "China") {
            host = "microsoftgraph.chinacloudapi.cn"
        }

        // 请求 URL
        val requestUrl = StrUtil.format("https://{}/v1.0/sites/root", host)

        // 构建请求认证 Token 信息
        val tokenValue = java.lang.String.format("%s %s", "Bearer", sharePointInfoVO.accessToken)
        val headers = HashMap<String, String>()
        headers["Authorization"] = tokenValue

        // 请求接口
        val getRequest = HttpUtil.createGet(requestUrl)
        val execute = getRequest.addHeaders(headers).execute()
        val body = execute.body()
        if (execute.status != HttpStatus.OK.value()) {
            return error(body)
        }

        // 解析前缀
        val jsonObject = JSONObject.parseObject(body)
        val hostname = jsonObject.getJSONObject("siteCollection").getString("hostname")
        val domainPrefix = StrUtil.subBefore(hostname, ".sharepoint", false)
        return successData(domainPrefix)
    }

    @PostMapping("/getSiteId")
    @ResponseBody
    fun getSiteId(@RequestBody sharePointInfoVO: SharePointInfoVO?): ResultBean {

        // 判断必填参数
        if (sharePointInfoVO == null || sharePointInfoVO.accessToken == null || sharePointInfoVO.siteName == null) {
            return error("参数不全")
        }
        var host = ""

        // 判断是标准版还是世纪互联版
        if (sharePointInfoVO.type == "Standard") {
            host = "graph.microsoft.com"
            sharePointInfoVO.setDomainType("com")
        } else if (sharePointInfoVO.type== "China") {
            host = "microsoftgraph.chinacloudapi.cn"
            sharePointInfoVO.setDomainType("cn")
        } else {
            return error("参数不全")
        }

        // 构建请求认证 Token 信息
        val tokenValue = java.lang.String.format("%s %s", "Bearer", sharePointInfoVO.getAccessToken())
        val authorizationHeaders = HashMap<String, String>()
        authorizationHeaders["Authorization"] = tokenValue


        // 如果没有域名前缀, 则先获取
        if (sharePointInfoVO.domainPrefix == null || sharePointInfoVO.getDomainType() == null) {
            val requestUrl = StrUtil.format("https://{}/v1.0/sites/root", host)
            val getRequest = HttpUtil.createGet(requestUrl)
            val execute = getRequest.addHeaders(authorizationHeaders).execute()
            val body = execute.body()
            if (execute.status != HttpStatus.OK.value()) {
                return error(body)
            }
            val jsonObject = JSONObject.parseObject(body)
            val hostname = jsonObject.getJSONObject("siteCollection").getString("hostname")
            val domainPrefix = StrUtil.subBefore(hostname, ".sharepoint", false)
            sharePointInfoVO.setDomainPrefix(domainPrefix)
        }
        if (StrUtil.isEmpty(sharePointInfoVO.getSiteType())) {
            sharePointInfoVO.setSiteType("/sites/")
        }

        // 请求接口
        val requestUrl = StrUtil.format(
            "https://{}/v1.0/sites/{}.sharepoint.{}:/{}/{}", host,
            sharePointInfoVO.getDomainPrefix(),
            sharePointInfoVO.getDomainType(),
            sharePointInfoVO.getSiteType(),
            sharePointInfoVO.getSiteName()
        )
        val getRequest = HttpUtil.createGet(requestUrl)
        val execute = getRequest.addHeaders(authorizationHeaders).execute()
        val body = execute.body()

        // 解析数据
        if (execute.status != HttpStatus.OK.value()) {
            return error(body)
        }
        val jsonObject = JSONObject.parseObject(body)
        return successData(jsonObject.getString("id"))
    }
}