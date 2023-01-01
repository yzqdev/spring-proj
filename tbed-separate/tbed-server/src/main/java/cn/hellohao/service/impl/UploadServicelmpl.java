package cn.hellohao.service.impl;

import cn.hellohao.mapper.*;
import cn.hellohao.model.entity.*;
import cn.hellohao.model.vo.UploadImgVo;
import cn.hellohao.service.ImgTempService;
import cn.hellohao.service.SysConfigService;
import cn.hellohao.util.*;
import cn.hutool.core.lang.Console;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baidu.aip.contentcensor.AipContentCensor;
import com.baidu.aip.contentcensor.EImgType;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2020/1/9 15:46
 */

@Service
public class UploadServicelmpl {
    @Autowired
    ConfigMapper configMapper;
    @Autowired
    SysConfigService sysConfigService;
    @Autowired
    UploadConfigMapper uploadConfigMapper;
    @Autowired
    KeysMapper keysMapper;
    @Autowired
    ImgMapper imgMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ImgreviewMapper imgreviewMapper;
    @Autowired
    ImgTempService imgTempService;



    public Msg uploadForLoc(HttpServletRequest request,
                            MultipartFile multipartFile, int setday, String imgUrl, JSONArray selectTreeList) {
        Msg msg = new Msg();
        try{
            UploadImgVo jsonObject = new UploadImgVo();
            UploadConfig uploadConfig = uploadConfigMapper.getUpdateConfig();
            String userIp = GetIPS.getIpAddr(request);
            Subject subject = SecurityUtils.getSubject();
            SysUser u = (SysUser) subject.getPrincipal();
            if(null!=u){
                u =  userMapper.getUsers(u);
            }
           String sourceKeyId  ;
            String md5key = null;
            FileInputStream fis;
            File file;
            if(imgUrl==null){
                file = SetFiles.changeFile_new(multipartFile);
            }else{
                //说明是URL上传
                Msg imgData = uploadForURL(request, imgUrl);
                if(imgData.getCode().equals("200")){
                    file = new File((String) imgData.getData());
                }else{
                    return imgData;
                }
            }
            String imgUid = UUID.randomUUID().toString().replace("-", "");
            //判断上传前的一些用户限制信息
            Msg msg1 = updateImgCheck(u,uploadConfig);
            if(!msg1.getCode().equals("300")){
                return msg1;
            }

            //判断可用容量
            sourceKeyId = siteGroup.getKeyID();
            StorageKey key = keysMapper.selectKeys(sourceKeyId);
            long tmp = (memory == -1 ? -2 : UsedTotleMemory);
            if (tmp >= memory) {
                msg.setCode("4005");
                msg.setInfo(u==null?"游客空间已用尽":"您的可用空间不足");
                return msg;
            }

            //判断图片有没有超出设定大小
            if (file.length() > TotleMemory) {
                System.err.println("文件大小："+file.length());
                System.err.println("最大限制："+TotleMemory);
                msg.setCode("4006");
                msg.setInfo("图像超出系统限制大小");
                return msg;
            }
            try {
                fis = new FileInputStream(file);
                md5key = DigestUtils.md5Hex(fis);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("未获取到图片的MD5,成成UUID");
            }
            Msg fileMiME = TypeDict.FileMiME(file );
            if(!fileMiME.getCode().equals("200")) {
                //非图像文本
                msg.setCode("4000");
                msg.setInfo(fileMiME.getInfo());
                return msg;
            }
            if(md5key==null || md5key.equals("")){
                md5key = UUID.randomUUID().toString().replace("-", "");
            }

            //判断图片是否存在
            if(Integer.parseInt(sysConfigService.getstate().getCheckduplicate())==1){
                Images imaOBJ = new Images();
                imaOBJ.setMd5key(md5key);
                imaOBJ.setUserId(u==null?"0":u.getId());
                if(imgMapper.md5Count(imaOBJ)>0){
                    Images images = imgMapper.selectImgUrlByMD5(md5key);
                    jsonObject.setUrl(  images.getImgUrl());
                    jsonObject.setName(file.getName());
                    jsonObject.setImgUid(images.getImgUid());
//                    jsonObject.put("shortLink",images.getShortlink());
                    msg.setData(jsonObject);
                    return msg;
                }
            }


            String prefix = file.getName().substring(file.getName().lastIndexOf(".")+1);
            //判断黑名单
            if (uploadConfig.getBlacklist() != null) {
                String[] iparr = uploadConfig.getBlacklist().split(";");
                for (String s : iparr) {
                    if (s.equals(userIp)) {
                        file.delete();
                        msg.setCode("4003");
                        msg.setInfo("你暂时不能上传");
                        return msg;
                    }
                }
            }

            Map<String, File> map = new HashMap<>();
            if (file.exists()) {
                map.put(prefix, file);
            }
            long stime = System.currentTimeMillis();
            Map<ReturnImage, Integer> m = null;
            ReturnImage returnImage = GetSource.storageSource(key.getStorageType(), map, updatePath, key.getId());
            Images img = new Images();
            if(returnImage.getCode().equals("200")){
                String imgurl = returnImage.getImgUrl();
                Long imgsize = returnImage.getImgSize();
                String imgname = returnImage.getImgName();
                img.setImgUrl(imgurl);
                img.setUpdateTime(LocalDateTime.now());
                img.setCreateTime(LocalDateTime.now());
                img.setSource(key.getId());
                img.setUserId(u == null ? "0" : u.getId());
                img.setSizes(imgsize.intValue());
                if(uploadConfig.getUrlType()==2){
                    img.setImgName(imgname);
                }else{
                    img.setImgName(SetText.getSubString(imgname, key.getRequestAddress() + "/", ""));
                }
                if(setday == 1 || setday == 3 || setday == 7 || setday == 30){
                    img.setImgType(1);
                    ImgTemp imgDataExp = new ImgTemp();
                    imgDataExp.setDelTime( Timestamp.valueOf(plusDay(setday)));
                    imgDataExp.setImgUid(imgUid);
                    imgTempService.insertImgExp(imgDataExp);
                }else{
                    img.setImgType(0);
                }

                img.setAbnormal(userIp);
                img.setMd5key(md5key);
                img.setImgUid(imgUid);
                img.setFormat(fileMiME.getData().toString());
              imgMapper.insert(img);
                long etime = System.currentTimeMillis();
                Print.Normal("上传图片所用总时长：" + String.valueOf(etime - stime) + "ms");
                jsonObject.setUrl(  img.getImgUrl());
                jsonObject.setName(  imgname);
                jsonObject.setImgUid( img.getImgUid());
//                jsonObject.put("shortLink", img.getShortlink());
                new Thread(()->{LegalImageCheck(img);}).start();
            }else{
                msg.setCode("5001");
                msg.setInfo("上传服务内部错误");
                return msg;
            }
            file.delete();
            msg.setData(jsonObject);
            return msg;
        }catch (Exception e){
            e.printStackTrace();
            msg.setInfo("上传时发生了一些错误");
            msg.setCode("110500");
            return msg;
        }

    }


    //通过图片Url上传图片
    public static Msg uploadForURL(HttpServletRequest request, String imgurl){
        final Msg msg = new Msg();
        //先判断是不是有效链接
//        final boolean valid = ImgUrlUtil.isValid(imgurl);
        if(true){
            Long imgsize = null;
            try {
                imgsize = ImgUrlUtil.getFileLength(imgurl);
                if(imgsize>0){
//                    String uuid= UUID.randomUUID().toString().replace("-", "");
                    String ShortUID = SetText.getShortUuid();
                    String savePath = request.getSession().getServletContext().getRealPath("/")+File.separator+"hellohaotmp"+File.separator;
                    Map<String ,Object> bl =ImgUrlUtil.downLoadFromUrl (imgurl, ShortUID, savePath);
                    if((Boolean) bl.get("res")==true){
//                        File file = new File();
                        msg.setCode("200");
                        msg.setData(bl.get("imgPath"));//savePath + File.separator + ShortUID
                        return msg;
                    }else{
                        if(bl.get("StatusCode").equals("110403")){
                            msg.setInfo("该链接非图像文件，无法上传");
                        }else{
                            msg.setInfo("该链接暂时无法上传");
                        }
                        msg.setCode("500");
                    }
                }else{
                    msg.setCode("500");
                    msg.setInfo("获取资源失败");
                }
            } catch (IOException e) {
                msg.setCode("500");
                msg.setInfo("获取资源失败");
            }
        }else{
            msg.setCode("500");
            msg.setInfo("该链接无效");
        }
        return msg;
    }


    /**
     * 上传用户或游客的所属分组
     */
    public static SiteGroup siteGroup;
    /**
     * 上传用户或者游客的分配容量 memory
     */
    public static Long memory;
    /**
     * //用户或者游客下可使用的总容量 //maxsize
     */
    public static Long TotleMemory;
    //用户或者游客已经用掉的总容量 //usermemory
    public static Long UsedTotleMemory;
    public static String updatePath="tourist";

    //判断用户 或 游客 当前上传图片的一系列校验
    private Msg updateImgCheck(SysUser sysUser, UploadConfig uploadConfig){
      Msg msg = new Msg();
       DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        try {
            if (sysUser == null) {
                //用户没有登陆，值判断游客能不能上传即可
                if(uploadConfig.getIsUpdate()!=1){
                    msg.setCode("1000");
                    msg.setInfo("系统已禁用游客上传");
                    return msg;
                }
                siteGroup = GetCurrentSource.GetSource(null);
                //单位 B 游客设置总量
                memory = Long.valueOf(uploadConfig.getVisitorStorage());
                //单位 B  游客单文件大小
                TotleMemory = Long.valueOf(uploadConfig.getFileSizeTourists());
                //单位 B
                UsedTotleMemory = imgMapper.getUserMemory("0")==null?0L : imgMapper.getUserMemory("0");
            } else {
                //判断用户能不能上传
                if(uploadConfig.getUserclose()!=1){
                    msg.setCode("1001");
                    msg.setInfo("系统已禁用上传功能");
                    return msg;
                }
                updatePath = sysUser.getUsername();
                siteGroup = GetCurrentSource.GetSource(sysUser.getId());
                //单位 B
                memory = sysUser.getMemory()*1024*1024;
                //单位 B
                TotleMemory = Long.valueOf(uploadConfig.getFileSizeUser());
                //单位 B
                UsedTotleMemory = imgMapper.getUserMemory(sysUser.getId())==null?0L:imgMapper.getUserMemory(sysUser.getId());
            }
            if (uploadConfig.getUrlType() == 2) {
                updatePath = dateFormat.format(LocalDateTime.now());
            }
            msg.setCode("300");
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode("500");
        }
        return msg;
    }

    private synchronized void LegalImageCheck(Images images){
        System.out.println("非法图像鉴别进程启动");
        Imgreview imgreview = null;
        try {
            imgreview = imgreviewMapper.selectByusing(1);
        } catch (Exception e) {
            Print.warning("获取鉴别程序的时候发生错误");
            e.printStackTrace();
        }
        if(null != imgreview){
            LegalImageCheckForBaiDu(imgreview,images);
        }

    }

    private void LegalImageCheckForBaiDu(Imgreview imgreview,Images images){
        System.out.println("非法图像鉴别进程启动-BaiDu");
        if(imgreview.getUsing()==1){
            try {
                AipContentCensor client = new AipContentCensor(imgreview.getAppId(), imgreview.getApiKey(), imgreview.getSecretKey());
                client.setConnectionTimeoutInMillis(5000);
                client.setSocketTimeoutInMillis(30000);
                org.json.JSONObject res = client.imageCensorUserDefined(images.getImgUrl(), EImgType.FILE, null);
                res = client.imageCensorUserDefined(images.getImgUrl(), EImgType.URL, null);
                System.err.println("返回的鉴黄json:"+res.toString());
                com.alibaba.fastjson.JSONArray jsonArray = JSON.parseArray("[" + res.toString() + "]");
                for (Object o : jsonArray) {
                    com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) o;
                    com.alibaba.fastjson.JSONArray data = jsonObject.getJSONArray("data");
                    Integer conclusionType = jsonObject.getInteger("conclusionType");
                    if(conclusionType!=null){
                        if (conclusionType == 2) {
                            for (Object datum : data) {
                                com.alibaba.fastjson.JSONObject imgdata = (com.alibaba.fastjson.JSONObject) datum;
                                if (imgdata.getInteger("type") == 1) {
                                    Images img = new Images();
                                    img.setImgName(images.getImgName());
                                    img.setViolation("1[1]");
                                    imgMapper.setImg(img);
                                    Imgreview imgv = new Imgreview();
                                    imgv.setId("1");
                                    Integer count = imgreview.getCount();
                                    System.out.println("违法图片总数：" + count);
                                    imgv.setCount(count + 1);
                                    imgreviewMapper.updateByPrimaryKeySelective(imgv);
                                    System.err.println("存在非法图片，进行处理操作");
                                }
                            }
                        }
                    }
                }

            }catch (Exception e){
                System.out.println("图像鉴黄线程执行过程中出现异常");
                e.printStackTrace();

            }

        }
    }


    //计算时间
    public static LocalDateTime plusDay(int setday){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
       LocalDateTime today=LocalDateTime.now();
       Console.log("现在的日期是：" + today);

       LocalDateTime enddate =  today.plusDays(setday) ;
        Console.log("到期的日期：" + enddate.format(format));
        return enddate;
    }


}
