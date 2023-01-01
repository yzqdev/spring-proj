package cn.hellohao.service.impl;

import cn.hellohao.mapper.*;
import cn.hellohao.model.entity.*;
import cn.hellohao.service.ImgAndAlbumService;
import cn.hellohao.service.SysConfigService;
import cn.hellohao.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.contentcensor.AipContentCensor;
import com.baidu.aip.contentcensor.EImgType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2021/10/28 16:38
 */
@Service
@RequiredArgsConstructor
public class ClientService {

    
    private final ImgAndAlbumService imgAndAlbumService;
    
    private final NOSImageupload nOSImageupload;
    
    private final OSSImageupload ossImageupload;
    
    private final USSImageupload ussImageupload;
    
    private final KODOImageupload kodoImageupload;
    
    private final COSImageupload cosImageupload;
    
    private final SysConfigService sysConfigService;
    
    private final FTPImageupload ftpImageupload;
    
    private final UFileImageupload uFileImageupload;
    
    private final UserMapper userMapper;
    
    private final KeysMapper keysMapper;
    
    private final ConfigMapper configMapper;
    
    private final UploadConfigMapper uploadConfigMapper;
    
    private final ImgMapper imgMapper;
    
    ImgreviewMapper imgreviewMapper;


    public Msg uploadImg(HttpServletRequest request, MultipartFile multipartFile, String email, String pass ){
        Msg msg = new Msg();
        try {
            String sourceKeyId  ;
            FileInputStream fis = null;
            String md5key = null;
            Integer setday = 0;
            JSONObject jsonObject = new JSONObject();
            Config config = configMapper.getSourceype();
            String userip = GetIPS.getIpAddr(request);
            UploadConfig uploadConfig = uploadConfigMapper.getUpdateConfig();
            if (uploadConfig.getApi() != 1) {
                msg.setCode("4003");
                msg.setInfo("管理员关闭了API接口");
                return msg;
            }
            File file = SetFiles.changeFile_new(multipartFile);
            SysUser u2 = new SysUser();
            if (!file.exists() || email == null || pass == null) {
                msg.setCode("4005");
                msg.setInfo("必要参数不能为空");
                return msg;
            }
            u2.setEmail(email);
            u2.setPassword(Base64Encryption.encryptBASE64(pass.getBytes()));
            SysUser u = userMapper.getUsers(u2);
            //判断用户的账号密码是否存在（正确）
            if (null == u || u.getIsok() != 1) {
                msg.setCode("4006");
                msg.setInfo("用户信息不正确,账号异常");
                return msg;
            }
            String imguid = UUID.randomUUID().toString().replace("-", "");
            //判断上传前的一些用户限制信息
            Msg msg1 = updateImgCheck(u, uploadConfig);
            if (!msg1.getCode().equals("300")) {
                return msg1;
            }
            sourceKeyId = siteGroup.getKeyID();
            StorageKey key = keysMapper.selectKeys(sourceKeyId);
            Long tmp = (memory == -1 ? -2 : UsedTotleMemory);
            if (tmp >= memory) {
                msg.setCode("4007");
                msg.setInfo(u == null ? "游客空间已用尽" : "您的可用空间不足");
                return msg;
            }
            if (file.length() > TotleMemory) {
                System.err.println("文件大小：" + file.length());
                System.err.println("最大限制：" + TotleMemory);
                msg.setCode("4008");
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
            if (!fileMiME.getCode().equals("200")) {
                //非图像文本
                msg.setCode("4009");
                msg.setInfo(fileMiME.getInfo());
                return msg;
            }
            if (md5key == null || md5key.equals("")) {
                md5key = UUID.randomUUID().toString().replace("-", "");
            }
            String prefix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            if (uploadConfig.getBlacklist() != null) {
                String[] iparr = uploadConfig.getBlacklist().split(";");
                for (String s : iparr) {
                    if (s.equals(userip)) {
                        msg.setCode("4010");
                        msg.setInfo("你暂时不能上传");
                        return msg;
                    }
                }
            }
            //判断图片是否存在
            if(Integer.valueOf(sysConfigService.getstate().getCheckduplicate())==1) {
                Images imaOBJ = new Images();
                imaOBJ.setMd5key(md5key);
                imaOBJ.setUserId(u.getId());
                if (imgMapper.md5Count(imaOBJ) > 0) {
                    Images images = imgMapper.selectImgUrlByMD5(md5key);
                    jsonObject.put("url", images.getImgUrl());
                    jsonObject.put("name", file.getName());
                    jsonObject.put("size", images.getSizes());
                    msg.setData(jsonObject);
                    return msg;
                }
            }
            Map<String, File> map = new HashMap<>();
            String fileName = file.getName();
            if (file.exists()) {
                map.put(prefix, file);
            }
            long stime = System.currentTimeMillis();
            Map<ReturnImage, Integer> m = null;
            ReturnImage returnImage = GetSource.storageSource(key.getStorageType(), map, updatePath,  key.getId());
            Images img = new Images();
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if (returnImage.getCode().equals("200")) {
                String imgurl = returnImage.getImgUrl();
                Long imgsize = returnImage.getImgSize();
                String imgname = returnImage.getImgName();
                img.setImgUrl(imgurl);
                img.setUpdateTime(LocalDateTime.now());
                img.setSource(key.getId());
                img.setUserId(u == null ? "0": u.getId());
                img.setSizes(imgsize.intValue());
                if (uploadConfig.getUrlType() == 2) {
                    img.setImgName(imgname);
                } else {
                    img.setImgName(SetText.getSubString(imgname, key.getRequestAddress() + "/", ""));
                }
                img.setImgType(setday > 0 ? 1 : 0);
                img.setAbnormal(userip);
                img.setMd5key(md5key);
                img.setImgUid(imguid);
                img.setFormat(fileMiME.getData().toString());
                imgMapper.insert(img);
                long etime = System.currentTimeMillis();
                Print.Normal("上传图片所用总时长：" + String.valueOf(etime - stime) + "ms");
                jsonObject.put("url", img.getImgUrl());
                jsonObject.put("name", imgname);
                jsonObject.put("size", img.getSizes());
                //启动鉴黄线程
                new Thread(() -> {
                    LegalImageCheck(img);
                }).start();
            } else {
                msg.setCode("5001");
                msg.setInfo("上传服务内部错误");
                return msg;
            }
            file.delete();
            msg.setData(jsonObject);
//            新代码结束=========
            return msg;
        }catch (Exception e){
            e.printStackTrace();
            msg.setCode("5001");
            msg.setInfo("Error for server:500");
            return msg;
        }

    }


    public static SiteGroup siteGroup; //上传用户或游客的所属分组
    public static Long memory;//上传用户或者游客的分配容量 memory
    public static Long TotleMemory;//用户或者游客下可使用的总容量 //maxsize
    public static Long UsedTotleMemory;//用户或者游客已经用掉的总容量 //usermemory
    public static String updatePath="tourist";

    //判断用户 或 游客 当前上传图片的一系列校验
     Msg updateImgCheck(SysUser sysUser, UploadConfig uploadConfig){
        final Msg msg = new Msg();
        DateTimeFormatter dateFormat =DateTimeFormatter.ofPattern("yyyy/MM/dd");
        try {
            if (sysUser == null) {
                //用户没有登陆，值判断游客能不能上传即可
                if(uploadConfig.getIsUpdate()!=1){
                    msg.setCode("1000");
                    msg.setInfo("系统已禁用游客上传");
                    return msg;
                }
                siteGroup = GetCurrentSource.GetSource(null);
                memory = Long.valueOf(uploadConfig.getVisitorStorage());//单位 B 游客设置总量
                TotleMemory = Long.valueOf(uploadConfig.getFileSizeTourists());//单位 B  游客单文件大小
                UsedTotleMemory = imgMapper.getUserMemory("0")==null?0L : imgMapper.getUserMemory("0");//单位 B
            } else {
                //判断用户能不能上传
                if(uploadConfig.getUserclose()!=1){
                    msg.setCode("1001");
                    msg.setInfo("系统已禁用上传功能");
                    return msg;
                }
                updatePath = sysUser.getUsername();
                siteGroup = GetCurrentSource.GetSource(sysUser.getId());
                memory = Long.valueOf(sysUser.getMemory())*1024*1024;//单位 B
                TotleMemory = Long.valueOf(uploadConfig.getFileSizeUser());//单位 B
                UsedTotleMemory = imgMapper.getUserMemory(sysUser.getId())==null?0L:imgMapper.getUserMemory(sysUser.getId());//单位 B
            }
            //判断上传的图片目录结构类型
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

    //图片鉴黄

      synchronized void LegalImageCheck(Images images){
        System.out.println("非法图像鉴别进程启动");
        Imgreview imgreview = null;
        try {
            imgreview = imgreviewMapper.selectByusing(1);
        } catch (Exception e) {
            Print.warning("获取鉴别程序的时候发生错误");
            e.printStackTrace();
        }
        LegalImageCheckForBaiDu(imgreview,images);
    }

      void LegalImageCheckForBaiDu(Imgreview imgreview,Images images){
        if(imgreview.getUsing()==1){
            try {
                AipContentCensor client = new AipContentCensor(imgreview.getAppId(), imgreview.getApiKey(), imgreview.getSecretKey());
                client.setConnectionTimeoutInMillis(5000);
                client.setSocketTimeoutInMillis(30000);
                org.json.JSONObject res = client.imageCensorUserDefined(images.getImgUrl(), EImgType.FILE, null);
                res = client.imageCensorUserDefined(images.getImgUrl(), EImgType.URL, null);
                com.alibaba.fastjson.JSONArray jsonArray = JSON.parseArray("[" + res.toString() + "]");
                for (Object o : jsonArray) {
                    JSONObject jsonObject = (JSONObject) o;
                    com.alibaba.fastjson.JSONArray data = jsonObject.getJSONArray("data");
                    Integer conclusionType = jsonObject.getInteger("conclusionType");
                    if(conclusionType!=null){
                        if (conclusionType == 2) {
                            for (Object datum : data) {
                                JSONObject imgdata = (JSONObject) datum;
                                if (imgdata.getInteger("type") == 1) {
                                    Images img = new Images();
                                    img.setImgName(images.getImgName());
                                    //数字是鉴别平台的主键ID，括号是非法的类型，参考上面的注释
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


}
