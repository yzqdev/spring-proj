package cn.hellohao.controller;

import cn.hellohao.model.entity.*;
import cn.hellohao.model.dto.AlbumDto;
import cn.hellohao.model.vo.PageResultBean;
import cn.hellohao.service.AlbumService;
import cn.hellohao.service.ImgAndAlbumService;
import cn.hellohao.service.UserService;
import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/12/17 11:25
 */
@Controller
@RequiredArgsConstructor
public class AlbumController {

   private final AlbumService albumService;
    private final  ImgAndAlbumService imgAndAlbumService;
    private final    UserService userService;

    @PostMapping("/admin/getGalleryList") //new
    @ResponseBody
    public Map<String, Object> getGalleryList (@RequestBody AlbumDto albumDto){
        Subject subject = SecurityUtils.getSubject();
        SysUser sysUser = (SysUser) subject.getPrincipal();
        sysUser =  userService.getUsers(sysUser);
        Map<String, Object> map = new HashMap<String, Object>();

        Integer pageNum =albumDto.getPageNum();
        Integer pageSize = albumDto.getPageSize();
        String albumtitle = albumDto.getAlbumTitle();
        if(subject.hasRole("admin")){
        }else{
            albumDto.setUserId(String.valueOf(sysUser.getId()));
        }
        Page<Album> page=new Page<>(pageNum,pageSize);

        try {
         Page<Album>   albums = albumService.selectAlbumURLList(page,albumDto);

            map.put("code", 200);
            map.put("info", "");
            map.put("count", albums.getTotal());
            map.put("data", albums.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 500);
            map.put("info", "获取数据异常");
        }
        return map;

    }

    @PostMapping("/admin/deleGallery") //new 删除画廊
    @ResponseBody
    public Msg deleGallery (@RequestBody String[] albumkeyList) {
        Msg msg = new Msg();
        Subject subject = SecurityUtils.getSubject();
        SysUser sysUser = (SysUser) subject.getPrincipal();
        try {


            for (String s : albumkeyList) {
                if (subject.hasRole("admin")) {
                    albumService.deleteAlbum(s);
                } else {
                    AlbumDto album = new AlbumDto();
                    album.setAlbumKey(s);
                    final Album alb = albumService.selectAlbum(album);
                    if (alb.getUserId().equals(sysUser.getId())) {
                        albumService.deleteAlbum(s);
                    }
                }
            }
            msg.setInfo("画廊已成功删除");
        }catch (Exception e){
            msg.setCode("500");
            msg.setInfo("画廊删除失败");
            e.printStackTrace();
        }
        return msg;

    }

    /**
     * 得到专辑img列表
     *
     * @param data 数据
     * @return {@link Msg}
     */
    @PostMapping("/getAlbumImgList")
    @ResponseBody
    public Msg getAlbumImgList(@RequestBody String[] data) {
        Msg msg = new Msg();


       Page<Images> json = albumService.getAlbumList(data);
        msg.setData(json.getRecords());
        return msg;
    }

    @PostMapping("/SaveForAlbum")//new
    @ResponseBody
    public Msg SaveForAlbum(@RequestBody AlbumDto albumDto){
        //data = StringEscapeUtils.unescapeHtml4(data);
        Msg msg = new Msg();
        try {

            String albumtitle = albumDto.getAlbumTitle();
            String password = albumDto.getPassword();
           List<Images> albumList=albumDto.getAlbumList();
            if(null!=password){
                password = password.replace(" ", "");
                if(password.replace(" ", "").equals("") || password.length()<3){
                    msg.setCode("110403");
                    msg.setInfo("密码长度不得小于三位有效字符");
                    return msg;
                }
            }
            if(albumtitle==null || albumList.size()==0){
                msg.setCode("110404");
                msg.setInfo("标题和图片参数不能为空");
                return msg;
            }
            Subject subject = SecurityUtils.getSubject();
            SysUser u = (SysUser) subject.getPrincipal();
            String uuid = "TOALBUM"+ UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5)+"N";
            DateTimeFormatter df=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Album album = new Album();
            album.setAlbumTitle(albumtitle);
            album.setCreateTime(LocalDateTime.now());
            album.setUpdateTime(LocalDateTime.now());
            album.setPassword(password);
            album.setAlbumKey(uuid);
            if(u==null){
                album.setUserId("0");
            }else{
                album.setUserId(u.getId());
            }
            albumService.addAlbum(album);
            for (int i = 0; i < albumList.size(); i++) {
                Images img =albumList.get(i);
                ImgAndAlbum imgAndAlbum = new ImgAndAlbum();
                imgAndAlbum.setImgName(img.getImgName());
                imgAndAlbum.setAlbumKey(uuid);
                imgAndAlbum.setNotes(img.getNotes());
                albumService.addAlbumForImgAndAlbumMapper(imgAndAlbum);
            }
            final JSONObject json = new JSONObject();
            json.put("url",uuid);
            json.put("title",albumtitle);
            json.put("password",password);
            msg.setCode("200");
            msg.setInfo("成功创建画廊链接");
            msg.setData(json);
        }catch (Exception e){
            e.printStackTrace();
            msg.setCode("500");
            msg.setInfo("创建画廊链接失败");
        }
        return msg;
    }


    @PostMapping("/checkPass")//new
    @ResponseBody
    public Msg checkPass(@RequestBody Map<String,String> jsonObject) {
        Msg msg = new Msg();
        JSONObject json = new JSONObject();

        try {

            String key = jsonObject.get("key");
            AlbumDto album=new AlbumDto();
            album.setAlbumKey(key);
            Album a =  albumService.selectAlbum(album);
            if(a!=null){
                json.put("album",a);
                json.put("exist",true);
                if(a.getPassword()!=null && !a.getPassword().equals("")){
                    json.put("passType",true);
                }else{
                    json.put("passType",false);
                }
                msg.setData(json);
            }else{
                json.put("exist",false);
                msg.setData(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode("110500");
            msg.setInfo("操作失败");
            msg.setData(json);
        }
        return msg;
    }

    @PostMapping("/getAlbumList") //new
    @ResponseBody
    public Msg getAlbumList(@RequestBody AlbumDto albumDto){
        Msg msg = new Msg();
        JSONObject json = new JSONObject();
        Integer pageNum = albumDto.getPageNum();
        Integer pageSize = albumDto.getPageSize();
        String albumkey = albumDto.getAlbumKey();
        String password = albumDto.getPassword();
        if(null!=password){
            password = password.replace(" ", "");
        }


        Album a =  albumService.selectAlbum(albumDto);
        if(a==null){
            msg.setCode("110404");
            msg.setInfo("画廊地址不存在");
        }else{
            Page<Images> page =new Page<>(pageNum,pageSize);
            if(a.getPassword()==null || (a.getPassword().replace(" ", "")).equals("")){
                Page<Images> imagesList = imgAndAlbumService.selectImgForAlbumkey(page,albumkey);

                PageResultBean<Images> pageResultBean = new PageResultBean<>(imagesList.getTotal(), imagesList.getRecords());
                json.put("imagesList",pageResultBean);
            }else{
                if(a.getPassword().equals(password)){
                    Page<Images> imagesList =  imgAndAlbumService.selectImgForAlbumkey(page,albumkey);

                    PageResultBean<Images> pageResultBean = new PageResultBean<>(imagesList.getTotal(), imagesList.getRecords());
                    json.put("imagesList",pageResultBean);
                }else{
                    msg.setCode("110403");
                    msg.setInfo("画廊密码错误");
                }
            }
            json.put("titlename",a.getAlbumTitle());
            msg.setData(json);
        }
        return msg;
    }



}
