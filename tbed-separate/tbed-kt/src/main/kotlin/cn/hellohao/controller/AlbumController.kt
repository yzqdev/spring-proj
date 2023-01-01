package cn.hellohao.controller

import cn.hellohao.model.dto.AlbumDto
import cn.hellohao.model.entity.*
import cn.hellohao.model.vo.PageResultBean
import cn.hellohao.service.AlbumService
import cn.hellohao.service.ImgAndAlbumService
import cn.hellohao.service.UserService
import cn.hellohao.serviceimport.UserService
import com.alibaba.fastjson.JSONObject
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import lombok.RequiredArgsConstructor
import org.apache.shiro.SecurityUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/12/17 11:25
 */
@Controller

class AlbumController(private val albumService: AlbumService,
                      private val imgAndAlbumService: ImgAndAlbumService,
                      private val userService: UserService,) {


    @PostMapping("/admin/getGalleryList") //new
    @ResponseBody
    fun getGalleryList(@RequestBody albumDto: AlbumDto): Map<String, Any> {
        val subject = SecurityUtils.getSubject()
        var sysUser: SysUser = subject.principal as SysUser
        sysUser = userService.getUsers(sysUser)
        val map: MutableMap<String, Any> = HashMap()
        val pageNum: Int? = albumDto.pageNum
        val pageSize: Int? = albumDto.pageSize
        val albumtitle: String? = albumDto.albumTitle
        if (subject.hasRole("admin")) {
        } else {
            albumDto.userId=sysUser.id
        }
        val page: Page<Album> = Page<Album>(pageNum.toLong(), pageSize.toLong())
        try {
            val albums: Page<Album> = albumService.selectAlbumURLList(page, albumDto)
            map["code"] = 200
            map["info"] = ""
            map["count"] = albums.total
            map["data"] = albums.records
        } catch (e: Exception) {
            e.printStackTrace()
            map["code"] = 500
            map["info"] = "获取数据异常"
        }
        return map
    }

    @PostMapping("/admin/deleGallery") //new 删除画廊
    @ResponseBody
    fun deleGallery(@RequestBody albumkeyList: Array<String?>): Msg {
        val msg = Msg()
        val subject = SecurityUtils.getSubject()
        val sysUser: SysUser = subject.principal as SysUser
        try {
            for (s in albumkeyList) {
                if (subject.hasRole("admin")) {
                    albumService.deleteAlbum(s)
                } else {
                    val album = AlbumDto()
                    album.albumKey=s
                    val alb: Album = albumService.selectAlbum(album)
                    if (alb.userId.equals(sysUser.id)) {
                        albumService.deleteAlbum(s)
                    }
                }
            }
            msg.info = "画廊已成功删除"
        } catch (e: Exception) {
            msg.code = "500"
            msg.info = "画廊删除失败"
            e.printStackTrace()
        }
        return msg
    }

    /**
     * 得到专辑img列表
     *
     * @param data 数据
     * @return [Msg]
     */
    @PostMapping("/getAlbumImgList")
    @ResponseBody
    fun getAlbumImgList(@RequestBody data: Array<String?>?): Msg {
        val msg = Msg()
        val json: Page<Images> = albumService.getAlbumList(data)
        msg.data = json.getRecords()
        return msg
    }

    @PostMapping("/SaveForAlbum") //new
    @ResponseBody
    fun SaveForAlbum(@RequestBody albumDto: AlbumDto): Msg {
        //data = StringEscapeUtils.unescapeHtml4(data);
        val msg = Msg()
        try {
            val albumtitle: String = albumDto.albumTitle?:""
            var password: String = albumDto.password
            val albumList: List<Images> = albumDto.albumList
            if (null != password) {
                password = password.replace(" ", "")
                if (password.replace(" ", "") == "" || password.length < 3) {
                    msg.code = "110403"
                    msg.info = "密码长度不得小于三位有效字符"
                    return msg
                }
            }
            if (albumList.isEmpty()) {
                msg.code = "110404"
                msg.info = "标题和图片参数不能为空"
                return msg
            }
            val subject = SecurityUtils.getSubject()
            val u: SysUser = subject.principal as SysUser
            val uuid = "TOALBUM" + UUID.randomUUID().toString().replace("-", "").lowercase(Locale.getDefault())
                .substring(0, 5) + "N"
            val df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val album = Album().apply {
                createTime= LocalDateTime.now()
                updateTime= LocalDateTime.now()
                this.password=password
                albumKey=uuid

            }
            album.albumTitle=albumtitle

            if (u == null) {
                album.userId="0"
            } else {
                album.userId=u.id

            }
            albumService.addAlbum(album)
            for (i in albumList.indices) {
                val img: Images = albumList[i]
                val imgAndAlbum = ImgAndAlbum().apply {
                    albumKey=uuid
                    notes=img.notes
                    imgName=img.imgName
                }


                albumService.addAlbumForImgAndAlbumMapper(imgAndAlbum)
            }
            val json = JSONObject()
            json["url"] = uuid
            json["title"] = albumtitle
            json["password"] = password
            msg.code = "200"
            msg.info = "成功创建画廊链接"
            msg.data = json
        } catch (e: Exception) {
            e.printStackTrace()
            msg.code = "500"
            msg.info = "创建画廊链接失败"
        }
        return msg
    }

    @PostMapping("/checkPass") //new
    @ResponseBody
    fun checkPass(@RequestBody jsonObject: Map<String?, String?>): Msg {
        val msg = Msg()
        val json = JSONObject()
        try {
            val key = jsonObject["key"]
            val album = AlbumDto()
            album.albumKey=key
            val a: Album? = albumService.selectAlbum(album)
            json["album"] = a
            json["exist"] = true
            if (a.password != null && !a.password.equals("")) {
                json["passType"] = true
            } else {
                json["passType"] = false
            }
            msg.data = json
        } catch (e: Exception) {
            e.printStackTrace()
            msg.code = "110500"
            msg.info = "操作失败"
            msg.data = json
        }
        return msg
    }

    @PostMapping("/getAlbumList") //new
    @ResponseBody
    fun getAlbumList(@RequestBody albumDto: AlbumDto): Msg {
        val msg = Msg()
        val json = JSONObject()
        val pageNum: Int = albumDto.pageNum
        val pageSize: Int = albumDto.pageSize
        val albumkey: String = albumDto.albumKey
        var password: String = albumDto.password
        if (null != password) {
            password = password.replace(" ", "")
        }
        val a: Album = albumService.selectAlbum(albumDto)
        if (a == null) {
            msg.code = "110404"
            msg.info = "画廊地址不存在"
        } else {
            val page: Page<Images> = Page<Images>(pageNum.toLong(), pageSize.toLong())
            if (a.password == null || a.password.replace(" ", "").equals("")) {
                val imagesList: Page<Images> = imgAndAlbumService.selectImgForAlbumkey(page, albumkey)
                val pageResultBean: PageResultBean<Images> =
                    PageResultBean<Images>(imagesList.total, imagesList.records)
                json["imagesList"] = pageResultBean
            } else {
                if (a.password.equals(password)) {
                    val imagesList: Page<Images> = imgAndAlbumService.selectImgForAlbumkey(page, albumkey)
                    val pageResultBean: PageResultBean<Images> =
                        PageResultBean<Images>(imagesList.total, imagesList.records)
                    json["imagesList"] = pageResultBean
                } else {
                    msg.code = "110403"
                    msg.info = "画廊密码错误"
                }
            }
            json["titlename"] = a.albumTitle
            msg.data = json
        }
        return msg
    }
}