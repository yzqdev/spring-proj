package cn.hellohao.controller;

import cn.hellohao.config.SysName;
import cn.hellohao.model.entity.*;
import cn.hellohao.model.dto.HomeImgDto;
import cn.hellohao.model.dto.ImgSearchDto;
import cn.hellohao.model.vo.ImageVo;
import cn.hellohao.model.vo.PageResultBean;
import cn.hellohao.service.*;
import cn.hellohao.service.impl.*;
import cn.hellohao.util.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-07-17 14:22
 */
@Controller
@RequestMapping("/admin")
@Tag(name = "管理员")
public class AdminController {

    @Resource
    private ImgService imgService;
    @Resource
    private KeysService keysService;
    @Resource
    private UserService  userService;
    @Resource
    private ImgreviewService imgreviewService;
    @Resource
    private ImgTempService imgTempService;
    @Resource
    private UploadConfigService uploadConfigService;
    @Resource
    private CodeService codeService;
    @Resource
    private ImgAndAlbumService imgAndAlbumService;
    @Resource
    private AlbumService albumService;


    @Resource
    private NOSImageupload nosImageupload;
    @Resource
    private OSSImageupload ossImageupload;
    @Resource
    private COSImageupload cosImageupload;
    @Resource
    private KODOImageupload kodoImageupload;
    @Resource
    private USSImageupload ussImageupload;
    @Resource
    private UFileImageupload uFileImageupload;
    @Resource
    private FTPImageupload ftpImageupload;


    /**
     * 概述数据
     *
     * @return {@link Msg}
     */
    @PostMapping(value = "/overviewData") //new
    @ResponseBody
    public Msg overviewData() {
        Msg msg = new Msg();
        Subject subject = SecurityUtils.getSubject();
        SysUser sysUser = (SysUser) subject.getPrincipal();
        sysUser = userService.getUsers(sysUser);
        JSONObject jsonObject = new JSONObject();
        UploadConfig uploadConfig = uploadConfigService.getUpdateConfig();
        //查询非法个数
        Imgreview imgreview = imgreviewService.selectByPrimaryKey("1");
        //查询有没有启动鉴别功能
        Imgreview isImgreviewOK = imgreviewService.selectByusing(1);
        String ok = "false";
        jsonObject.put("myImgTotal", imgService.countimg(sysUser.getId())); //我的图片数
        jsonObject.put("myAlbumTitle", albumService.selectAlbumCount(sysUser.getId()));//我的画廊数量
        long memory = sysUser.getMemory();//分配量
        Long usermemory = imgService.getUserMemory(sysUser.getId()) == null ? 0L : imgService.getUserMemory(sysUser.getId());
        if (memory == 0) {
            jsonObject.put("myMemory", "无容量");
        } else {
            double aDouble = Double.parseDouble(String.format("%.2f", (((double) usermemory / (double) memory) * 100)));
            if (aDouble >= 999) {
                jsonObject.put("myMemory", 999);
            } else {
                jsonObject.put("myMemory", aDouble);
            }
        }
        jsonObject.put("myMemorySum", SetFiles.readableFileSize(memory));
        if (sysUser.getLevel() > 1) {
            ok = "true";
            //管理员
            jsonObject.put("imgTotal", imgService.counts(null)); //admin  站点图片数
            jsonObject.put("userTotal", userService.getUserTotal()); //admin  用户个数
            jsonObject.put("ViolationImgTotal", imgreview.getCount()); //admin 非法图片
            jsonObject.put("ViolationSwitch", isImgreviewOK == null ? 0 : isImgreviewOK.getId()); //admin 非法图片开关
            jsonObject.put("VisitorUpload", uploadConfig.getIsUpdate());//是否禁用了游客上传
            jsonObject.put("VisitorMemory", SetFiles.readableFileSize(Long.parseLong(uploadConfig.getVisitorStorage())));//访客共大小
            if (uploadConfig.getIsUpdate() != 1) {
                jsonObject.put("VisitorUpload", 0);//是否禁用了游客上传
                jsonObject.put("VisitorProportion", 100.00);//游客用量%占比
                jsonObject.put("VisitorMemory", "禁用");//访客共大小
            } else {
                Long temp = imgService.getUserMemory("0") == null ? 0 : imgService.getUserMemory("0");
                jsonObject.put("UsedMemory", (temp == null ? 0 : SetFiles.readableFileSize(temp)));//访客已用大小
                if (Long.valueOf(uploadConfig.getVisitorStorage()) == 0) {
                    jsonObject.put("VisitorProportion", 100.00);//游客用量%占比
                } else if (Long.valueOf(uploadConfig.getVisitorStorage()) == -1) {
                    jsonObject.put("VisitorProportion", 0);//游客用量%占比
                    jsonObject.put("VisitorMemory", "无限");//访客共大小
                } else {
                    double sum = Double.valueOf(uploadConfig.getVisitorStorage());
                    Double aDouble = Double.valueOf(String.format("%.2f", ((double) temp / sum) * 100));
                    if (aDouble >= 999) {
                        jsonObject.put("VisitorProportion", 999);//游客用量%占比
                    } else {
                        jsonObject.put("VisitorProportion", aDouble);//游客用量%占比
                    }
                }
            }
        }
        jsonObject.put("ok", ok);
        msg.setData(jsonObject);
        return msg;
    }


    @PostMapping(value = "/SpaceExpansion/{code}")//new
    @ResponseBody
    public Msg SpaceExpansion(@PathVariable("code") String codeStr) {
        final Msg msg = new Msg();

        Subject subject = SecurityUtils.getSubject();
        SysUser sysUser = (SysUser) subject.getPrincipal();
        sysUser = userService.getUsers(sysUser);
        if (sysUser.getIsok() == 0) {
            msg.setCode("100403");
            msg.setInfo("你暂时无法使用此功能");
            return msg;
        }
        if (null == sysUser) {
            msg.setCode("100405");
            msg.setInfo("用户信息不存在");
            return msg;
        } else {
            long sizes = 0;
            Code code = codeService.selectCodeKey(codeStr);
            if (null == code) {
                msg.setCode("100404");
                msg.setInfo("扩容码不存在,请重新填写");
                return msg;
            }
            Long userMemory = Long.valueOf(sysUser.getMemory());
            sizes = Long.valueOf(code.getValue()) + userMemory;
            SysUser newMemorySysUser = new SysUser();
            newMemorySysUser.setMemory(sizes);
            newMemorySysUser.setId(sysUser.getId());
            userService.usersetmemory(newMemorySysUser, codeStr);
            msg.setInfo("你已成功扩容" + SetFiles.readableFileSize(sizes));
            return msg;
        }
    }


    @PostMapping("/getRecently")//new
    @ResponseBody
    public Msg getRecently() {
        Msg msg = new Msg();
        final JSONObject jsonObject = new JSONObject();
        try {
            Subject subject = SecurityUtils.getSubject();
            SysUser sysUser = (SysUser) subject.getPrincipal();
            sysUser = userService.getUsers(sysUser);
            if (sysUser.getLevel() > 1) {
                jsonObject.put("RecentlyUser", imgService.recentlyUser());
                jsonObject.put("RecentlyUploaded", imgService.recentlyUploaded(sysUser.getId()));
            } else {
                jsonObject.put("RecentlyUploaded", imgService.recentlyUploaded(sysUser.getId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg.setInfo("系统内部错误");
            msg.setCode("500");
            return msg;
        }
        msg.setData(jsonObject);
        return msg;
    }

    @PostMapping("/getYyyy")//new
    @ResponseBody
    public Msg getYyyy() {
        final Msg msg = new Msg();
        Subject subject = SecurityUtils.getSubject();
        SysUser u = (SysUser) subject.getPrincipal();
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("allYyyy", imgService.getyyyy(null));
        jsonObject.put("userYyyy", imgService.getyyyy(u.getId()));
        msg.setData(jsonObject);
        return msg;
    }

    @PostMapping("/getChart")//new
    @ResponseBody
    public Msg getChart(@RequestBody JSONObject data) {
        Msg msg = new Msg();
        JSONObject jsonObject = JSONObject.parseObject(String.valueOf(data));
        String yyyy = jsonObject.getString("year");
        Integer type = jsonObject.getInteger("type");

        Subject subject = SecurityUtils.getSubject();
        SysUser u = (SysUser) subject.getPrincipal();
        List<ImageVo> list = null;
        if (u.getLevel() > 1) {
            if (type == 2) {
                HomeImgDto homeImgDto = new HomeImgDto();
                homeImgDto.setYear(yyyy);
                //images.setYyyy(yyyy);
                list = imgService.countByM(homeImgDto);
            } else {
                HomeImgDto homeImgDto = new HomeImgDto();
                homeImgDto.setYear(yyyy);

                homeImgDto.setUserId(u.getId());
                list = imgService.countByM(homeImgDto);
            }
        } else {
            HomeImgDto homeImgDto = new HomeImgDto();
            homeImgDto.setYear(yyyy);

            homeImgDto.setUserId(u.getId());
            list = imgService.countByM(homeImgDto);
        }
        JSONArray json = JSONArray.parseArray("[{\"id\":1,\"monthNum\":\"一月\",\"countNum\":0},{\"id\":2,\"monthNum\":\"二月\",\"countNum\":0},{\"id\":3,\"monthNum\":\"三月\",\"countNum\":0},{\"id\":4,\"monthNum\":\"四月\",\"countNum\":0},{\"id\":5,\"monthNum\":\"五月\",\"countNum\":0},{\"id\":6,\"monthNum\":\"六月\",\"countNum\":0},{\"id\":7,\"monthNum\":\"七月\",\"countNum\":0},{\"id\":8,\"monthNum\":\"八月\",\"countNum\":0},{\"id\":9,\"monthNum\":\"九月\",\"countNum\":0},{\"id\":10,\"monthNum\":\"十月\",\"countNum\":0},{\"id\":11,\"monthNum\":\"十一月\",\"countNum\":0},{\"id\":12,\"monthNum\":\"十二月\",\"countNum\":0}]");
        System.out.println(list);
        System.out.println("这是啥");
        JSONArray jsonArray = new JSONArray();
        for (ImageVo imageVo : list) {
            for (int i = 0; i < json.size(); i++) {
                JSONObject jobj = json.getJSONObject(i);
                if (jobj.getInteger("id").equals(imageVo.getMonthNum())) {
                    jobj.put("monthNum", getChinaes(imageVo.getMonthNum()));
                    jobj.put("countNum", imageVo.getCountNum());
                }
            }
        }
        msg.setData(json);
        return msg;
    }

    @PostMapping("/getStorage")//new
    @ResponseBody
    public Msg getStorage() {
        Msg msg = new Msg();
        List<StorageKey> storage = keysService.getStorage();
        msg.setData(storage);
        return msg;
    }

    @PostMapping("/getStorageName")//new
    @ResponseBody
    public Msg getStorageName() {
        Msg msg = new Msg();
        List<StorageKey> storage = keysService.getStorageName();
        msg.setData(storage);
        return msg;
    }

    /**
     * 选择照片
     *
     * @param imgSearchDto img搜索dto
     * @return {@link Msg}
     */
    @PostMapping(value = "/selectPhoto")
    @ResponseBody
    public Msg selectPhoto(@RequestBody ImgSearchDto imgSearchDto) {
        Msg msg = new Msg();
        Subject subject = SecurityUtils.getSubject();
        SysUser sysUser = (SysUser) subject.getPrincipal();



        if (imgSearchDto.getStartTime() != null) {
            try {
               LocalDateTime date1 = LocalDateTime.parse(imgSearchDto.getStartTime(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                LocalDateTime date2 =imgSearchDto.getStopTime() == null ?LocalDateTime.now() :  LocalDateTime.parse(   imgSearchDto.getStopTime(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                int compareTo = date1.compareTo(date2);
                if (compareTo == 1) {
                    msg.setCode("110500");
                    msg.setInfo("起始日期不能大于结束日期");
                    return msg;
                }
            } catch (Exception e) {
                e.printStackTrace();
                msg.setCode("110500");
                msg.setInfo("您输入的日期不正确");
                return msg;
            }
        }


        if (imgSearchDto.getViolation()) {
            imgSearchDto.setViolation(true);
        }

        //if(img.getClassifulids()!=null){
        //    String[] calssif = img.getClassifulids().split(",");
        //    img.setClassifulids(calssif);
        //}
        if (subject.hasRole("admin")) {
            imgSearchDto.setUserId(null);
        } else {
            //普通用户
            imgSearchDto.setUserId(sysUser.getId());
        }
        imgSearchDto.setPageNum(1);
        imgSearchDto.setPageSize(10);
        List<Images> imgList = imgService.selectImages(imgSearchDto);
        Page<Images> images = new Page<>(1,10);
        images.setRecords(imgList);
        PageResultBean<Images> pageResultBean = new PageResultBean<>(images.getTotal(), images.getRecords());
        msg.setData(pageResultBean);
        return msg;
    }


    @PostMapping(value = "/getUserInfo") //new
    @ResponseBody
    public Msg getUserInfo() {
        Msg msg = new Msg();
        try {
            Subject subject = SecurityUtils.getSubject();
            SysUser sysUser = (SysUser) subject.getPrincipal();
            final SysUser u = new SysUser();
            u.setId(sysUser.getId());
            SysUser sysUserInfo = userService.getUsers(u);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", sysUserInfo.getUsername());
            jsonObject.put("email", sysUserInfo.getEmail());
            msg.setData(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode("110500");
            msg.setInfo("操作失败");
        }
        return msg;
    }

    @PostMapping("/setUserInfo") //new
    @ResponseBody
    public Msg setUserInfo(@RequestBody Map<String, String> jsonObject) {
        Msg msg = new Msg();
        try {
            String username = jsonObject.get("username");
            String email = jsonObject.get("email");
            String password = jsonObject.get("password");
            Subject subject = SecurityUtils.getSubject();
            SysUser u = (SysUser) subject.getPrincipal();
            SysUser sysUser = new SysUser();
            if (!SetText.checkEmail(email)) {
                msg.setCode("110403");
                msg.setInfo("邮箱格式不正确");
                return msg;
            }
            String regex = "^\\w+$";
            if (username.length() > 20 || !username.matches(regex)) {
                msg.setCode("110403");
                msg.setInfo("用户名不得超过20位字符");
                return msg;
            }
            if (subject.hasRole("admin")) {
                final SysUser sysUserOld = new SysUser();
                sysUserOld.setId(u.getId());
                SysUser sysUserInfo = userService.getUsers(sysUserOld);
                if (!sysUserInfo.getUsername().equals(username)) {
                    Integer countusername = userService.countusername(username);
                    if (countusername == 1 || !SysName.CheckSysName(username)) {
                        msg.setCode("110406");
                        msg.setInfo("此用户名已存在");
                        return msg;
                    } else {
                        sysUser.setUsername(username);
                    }
                }
                if (!sysUserInfo.getEmail().equals(email)) {
                    Integer countmail = userService.countmail(email);
                    if (countmail == 1) {
                        msg.setCode("110407");
                        msg.setInfo("此邮箱已被注册");
                        return msg;
                    } else {
                        sysUser.setEmail(email);
                    }
                }
                sysUser.setPassword(Base64Encryption.encryptBASE64(password.getBytes()));
                sysUser.setUid(u.getUid());
            } else {
                sysUser.setPassword(Base64Encryption.encryptBASE64(password.getBytes()));
                sysUser.setUid(u.getUid());
            }
            sysUser.setUpdateTime(LocalDateTime.now());
            userService.change(sysUser);
            msg.setInfo("信息修改成功，请重新登录");
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode("110500");
            msg.setInfo("服务执行异常，请稍后再试");
        }
        return msg;
    }


    @PostMapping("/deleImages") //new
    @ResponseBody
    public Msg deleImages(@RequestBody String[] images) {
        Msg msg = new Msg();

        Subject subject = SecurityUtils.getSubject();
        SysUser sysUser = (SysUser) subject.getPrincipal();

        if (null == sysUser) {
            msg.setCode("500");
            msg.setInfo("当前用户信息不存在");
            return msg;
        }
        if (images.length == 0) {
            msg.setCode("404");
            msg.setInfo("为获取到图像信息");
            return msg;
        }
        for (int i = 0; i < images.length; i++) {
            String imgid =  images[i] ;
            Images image = imgService.selectByPrimaryKey(imgid);
            String keyID = image.getSource();
            String imgname = image.getImgName();
            StorageKey key = keysService.selectKeys(keyID);

            if (!subject.hasRole("admin")) {
                if (!image.getUserId().equals(sysUser.getId())) {
                    break;
                }
            }
            boolean isDele = false;
            try {
                if (key.getStorageType() == 1) {
                    isDele = nosImageupload.delNOS(key.getId(), imgname);
                } else if (key.getStorageType() == 2) {
                    isDele = ossImageupload.delOSS(key.getId(), imgname);
                } else if (key.getStorageType() == 3) {
                    isDele = ussImageupload.delUSS(key.getId(), imgname);
                } else if (key.getStorageType() == 4) {
                    isDele = kodoImageupload.delKODO(key.getId(), imgname);
                } else if (key.getStorageType() == 5) {
                    isDele = LocUpdateImg.deleteLOCImg(imgname);
                } else if (key.getStorageType() == 6) {
                    isDele = cosImageupload.delCOS(key.getId(), imgname);
                } else if (key.getStorageType() == 7) {
                    isDele = ftpImageupload.delFTP(key.getId(), imgname);
                } else if (key.getStorageType() == 8) {
                    isDele = uFileImageupload.delUFile(key.getId(), imgname);
                } else {
                    System.err.println("未获取到对象存储参数，删除失败。");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (isDele) {
                try {
                    imgTempService.delImgAndExp(image.getImgUid());
                    imgService.deleteImgById(imgid);
                    imgAndAlbumService.deleteImgAndAlbum(imgname);
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.setInfo("图片记录删除失败，请重试");
                    msg.setCode("500");
                    return msg;
                }
                msg.setInfo("删除成功");
            } else {
                imgTempService.delImgAndExp(image.getImgUid());
                imgService.deleteImgById(imgid);
                imgAndAlbumService.deleteImgAndAlbum(imgname);
                msg.setInfo("图片记录已删除，但是图片源删除失败");
            }
        }
        return msg;
    }


    //工具函数
    private static String getChinaes(int v) {
        String ch = "";
        switch (v) {
            case 1:
                ch = "一月";
                break; //可选
            case 2:
                ch = "二月";
                break; //可选
            case 3:
                ch = "三月";
                break; //可选
            case 4:
                ch = "四月";
                break; //可选
            case 5:
                ch = "五月";
                break; //可选
            case 6:
                ch = "六月";
                break; //可选
            case 7:
                ch = "七月";
                break; //可选
            case 8:
                ch = "八月";
                break; //可选
            case 9:
                ch = "九月";
                break; //可选
            case 10:
                ch = "十月";
                break; //可选
            case 11:
                ch = "十一月";
                break; //可选
            case 12:
                ch = "十二月";
                break; //可选
            default:
                ch = "";//可选
                //语句
        }

        return ch;

    }

}
