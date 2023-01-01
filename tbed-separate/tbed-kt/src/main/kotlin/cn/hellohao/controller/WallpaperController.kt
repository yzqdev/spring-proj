package cn.hellohao.controller

import cn.hellohao.service.MobilePaperService
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class WallpaperController {
    @Value("\${webName}")
    private val webName: String? = null

    @Value("\${weblinks}")
    private val weblinks: String? = null

    @Autowired
    private val wallpaperService: WallpaperService? = null

    @Autowired
    private val mobilePaperService: MobilePaperService? = null

    //Hellohao编写修改
    @RequestMapping("/")
    fun index(model: Model): String {
        model.addAttribute("webName", webName)
        model.addAttribute("weblinks", weblinks)
        return "index-1"
    }

    //首页最新更新
    //    @RequestMapping(value="/GetWallpapers",method = RequestMethod.POST)
    //    @ResponseBody
    //    public String GetWallpaper(Integer start, Integer count,Integer category) {
    //        String wallpaper = "";
    //        wallpaper = wallpaperService.GetWallpaper(start, count,category);
    //        return wallpaper;
    //    }
    @GetMapping(value = ["/GetWallpapers"])
    @ResponseBody
    fun GetWallpaper(start: Int?, count: Int?, category: Int?): CommonResult<*> {
        val wallpaper: String = wallpaperService.getWallpaper(start, count, category)
        val jsonArray = JSONArray()
        val res = HashMap<String, Any>()
        val imgjo = JSONObject.parseObject(wallpaper)
        //可以使用parseObject(params，Class<T> clazz)直接转换成需要的Bean
        val imgjson = JSONArray.parseArray(imgjo.getString("data"))
        for (i in imgjson.indices) {
            val job = imgjson.getJSONObject(i)
            val jsonObject = JSONObject()
            jsonObject["imgUrl"] = job["url"]
            jsonObject["imgTag"] = job["utag"]
            jsonArray.add(i, jsonObject)
        }
        res["urls"] = jsonArray
        res["img"] = imgjson
        return CommonResult.success(res)
    }

    //获取所有分类
    @RequestMapping(value = ["/GetCategory"], method = [RequestMethod.GET])
    @ResponseBody
    fun GetCategory(): String {
        return wallpaperService.getWallpaperCategory()
    }

    @RequestMapping(value = ["/GetMobilepapers"], method = [RequestMethod.GET])
    @ResponseBody
    fun GetMobilepaper(start: Int?, count: Int?, category: String?): String {
        val mobilpaper: String = mobilePaperService.GetMobilepaper(start, count, category)
        val jsonObject = JSONObject()
        val jsonArray = JSONArray()
        val imgjo = JSONObject.parseObject(mobilpaper)
        println(imgjo.toJSONString())
        //可以使用parseObject(params，Class<T> clazz)直接转换成需要的Bean
        val imgjsonObj = JSONObject.parseObject(imgjo.getString("res"))
        println(imgjsonObj)
        val imgjson = JSONArray.parseArray(imgjsonObj.getString("vertical"))
        for (i in imgjson.indices) {
            val job = imgjson.getJSONObject(i) // 遍历 jsonarray 数组，把每一个对象转成 json 对象
            jsonObject["ImgUrl"] = job["preview"]
            jsonObject["ImgTag"] = job["tag"]
            jsonArray.add(i, jsonObject)
        }
        println()
        return jsonArray.toString()
    }

    //获取所有分类
    @RequestMapping(value = ["/GetMobileCategory"], method = [RequestMethod.GET])
    @ResponseBody
    fun GetMobileCategory(): String {
        return mobilePaperService.GetMobilepaperCategory()
    }
}