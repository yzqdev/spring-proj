package cn.hellohao.controller;

import cn.hellohao.auth.token.JWTUtil;
import cn.hellohao.auth.token.UserClaim;
import cn.hellohao.model.entity.*;
import cn.hellohao.service.*;
import cn.hellohao.service.impl.*;
import cn.hellohao.util.*;
import cn.hellohao.util.verifyCode.IVerifyCodeGen;
import cn.hellohao.util.verifyCode.SimpleCharVerifyCodeGenImpl;
import cn.hellohao.util.verifyCode.VerifyCode;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Map;

@RestController
public class IndexController {
    @Autowired
    private ImgService imgService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    IRedisService iRedisService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private UploadConfigService uploadConfigService;
    @Autowired
    private UploadServicelmpl uploadServicelmpl;
    @Autowired
    private NOSImageupload nosImageupload;
    @Autowired
    private OSSImageupload ossImageupload;
    @Autowired
    private COSImageupload cosImageupload;
    @Autowired
    private KODOImageupload kodoImageupload;
    @Autowired
    private USSImageupload ussImageupload;
    @Autowired
    private UFileImageupload uFileImageupload;
    @Autowired
    private FTPImageupload ftpImageupload;
    @Autowired
    AlbumService albumService;
    @Autowired
    private KeysService keysService;
    @Autowired
    private ImgTempService imgTempService;
    @Autowired
    private ImgAndAlbumService imgAndAlbumService;


    @GetMapping(value = "/webInfo")
    
    public Msg webInfo() {
        final Msg msg = new Msg();
        Config config = configService.getSourceType();
        UploadConfig updateConfig = uploadConfigService.getUpdateConfig();
        SysConfig sysConfig = sysConfigService.getstate();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("webname",config.getWebname());
        jsonObject.put("websubtitle",config.getWebsubtitle());
        jsonObject.put("keywords",config.getWebkeywords());
        jsonObject.put("description",config.getWebms());
        jsonObject.put("explain",config.getExplain());
        jsonObject.put("favicon",config.getWebfavicons());
        jsonObject.put("baidu",config.getBaidu());
        jsonObject.put("links",config.getLinks());
        jsonObject.put("aboutinfo",config.getAboutInfo());
        jsonObject.put("logo",config.getLogo());
        jsonObject.put("api",updateConfig.getApi());
        jsonObject.put("register",sysConfig.getRegister());
        msg.setData(jsonObject);
        return msg;
    }


    @PostMapping(value = "/upload")//upimg new
    
    public Msg upimg(
            @RequestParam(value = "file", required = false) MultipartFile multipartFile, int day,
            @RequestParam(value = "classifications", defaultValue = "" ) String classifications) {
        final JSONArray jsonArray = new JSONArray();
        HttpServletRequest request=RequestHelper.getRequest();
        if(!classifications.equals("")){
            String[] calssif = classifications.split(",");
            for (int i = 0; i < calssif.length; i++) {
                jsonArray.add(calssif[i]);
            }
        }
        return uploadServicelmpl.uploadForLoc(request,multipartFile,day,null,jsonArray);
    }

    //根据网络图片url上传
    @PostMapping(value = "/uploadForUrl") //new
    
    public Msg upurlimg(@RequestBody Map<String,Object> data) {

        final JSONArray jsonArray = new JSONArray();
        int setday = (int) data.get("day");
        String imgUrl = (String) data.get("imgUrl");
        String selectTreeListStr = (String) data.get("classifications");
        if(null != selectTreeListStr){
            String[] calssif = selectTreeListStr.split(",");
            for (int i = 0; i < calssif.length; i++) {
                jsonArray.add(calssif[i]);
            }
        }
        return uploadServicelmpl.uploadForLoc(RequestHelper.getRequest(),null,setday,imgUrl,jsonArray);
    }

    @GetMapping(value = "/getUploadInfo")//new
    
    public Msg getUploadInfo() {
        Msg msg = new Msg();
        JSONObject jsonObject = new JSONObject();
        Subject subject = SecurityUtils.getSubject();
        SysUser sysUser = (SysUser) subject.getPrincipal();
        try {
            UploadConfig updateConfig = uploadConfigService.getUpdateConfig();
            jsonObject.put("suffix",updateConfig.getSuffix().split(","));
            if(null== sysUser){
                jsonObject.put("filesize",Integer.valueOf(updateConfig.getFileSizeTourists())/1024);
                jsonObject.put("imgcount",updateConfig.getImgCountTourists());
                jsonObject.put("uploadSwitch",updateConfig.getIsUpdate());
                jsonObject.put("uploadInfo","您登陆后才能使用此功能哦");
            }else{
                jsonObject.put("filesize",Integer.valueOf(updateConfig.getFileSizeUser())/1024);
                jsonObject.put("imgcount",updateConfig.getImgCountUser());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        msg.setData(jsonObject);
        return msg;
    }


    @PostMapping("/checkStatus")
    
    public Msg checkStatus(HttpServletRequest request) {
        Msg msg = new Msg();
        String token = request.getHeader("Authorization");
        if(token != null) {
            UserClaim tokenJson = JWTUtil.checkToken(token);
            if(Boolean.TRUE.equals(tokenJson.getCheck())){
                Subject subject = SecurityUtils.getSubject();
                UsernamePasswordToken tokenOBJ = new UsernamePasswordToken(tokenJson.getEmail(),tokenJson.getPassword());
                tokenOBJ.setRememberMe(true);
                try {
                    subject.login(tokenOBJ);
                    SecurityUtils.getSubject().getSession().setTimeout(3600000);
                    SysUser u = (SysUser) subject.getPrincipal();
                    final JSONObject jsonObject = new JSONObject();
                    jsonObject.put("RoleLevel",u.getLevel()==2?"admin":"user");
                    jsonObject.put("userName",u.getUsername());
                    msg.setCode("200");
                    msg.setData(jsonObject);
                } catch (Exception e) {
                    msg.setCode("40041");
                    msg.setInfo("登录失效，请重新登录");
                    System.err.println("登录失效，请重新登录");
                    e.printStackTrace();
                }
            }else{
                msg.setCode("40041");
                msg.setInfo("登录失效，请重新登录");
            }
        }else{
            msg.setCode("40040");
            msg.setInfo("当前未登录，请先登录");
        }
        return msg;
    }



    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse response,HttpSession httpSession) {
        IVerifyCodeGen iVerifyCodeGen = new SimpleCharVerifyCodeGenImpl();
        try {
            //登录页面验证码
            VerifyCode verifyCode = iVerifyCodeGen.generate(80, 38);
            String code = verifyCode.getCode();
            String userIP = GetIPS.getIpAddr(request);
            iRedisService.setValue(userIP+"_hellohao_verifyCode",code);
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            response.getOutputStream().write(verifyCode.getImgBytes());
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/verifyCodeForRegister")
    public void verifyCodeForRegister(HttpServletRequest request, HttpServletResponse response,HttpSession httpSession) {
        IVerifyCodeGen iVerifyCodeGen = new SimpleCharVerifyCodeGenImpl();
        try {
            VerifyCode verifyCode = iVerifyCodeGen.generate(80, 38);
            String code = verifyCode.getCode();
            String userIP = GetIPS.getIpAddr(request);
            iRedisService.setValue(userIP+"_hellohao_verifyCodeForRegister",code);
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            response.getOutputStream().write(verifyCode.getImgBytes());
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/verifyCodeForRetrieve")
    public void verifyCodeForRetrieve(HttpServletRequest request, HttpServletResponse response,HttpSession httpSession) {
        IVerifyCodeGen iVerifyCodeGen = new SimpleCharVerifyCodeGenImpl();
        try {
            VerifyCode verifyCode = iVerifyCodeGen.generate(80, 38);
            String code = verifyCode.getCode();
            System.out.println("verifyCodeForRetrieve-zhaoHui httpSession ID==="+httpSession.getId());
            String userIP = GetIPS.getIpAddr(request);
            iRedisService.setValue(userIP+"_hellohao_verifyCodeForEmailRetrieve",code);
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            response.getOutputStream().write(verifyCode.getImgBytes());
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //删除图像
    @PostMapping("/deleImagesByUid/{imgUid}") //new
    
    public Msg deleImagesByUid( @PathVariable("imgUid") String imgUid) {
        Msg msg = new Msg();

        Images image = imgService.selectImgUrlByImgUID(imgUid);
        Subject subject = SecurityUtils.getSubject();
        SysUser sysUser = (SysUser) subject.getPrincipal();
        if(null!= sysUser){
            if(!sysUser.getId().equals(image.getUserId())){
                msg.setInfo("删除失败，该图片不允许你执行操作");
                msg.setCode("100403");
                return msg;
            }
        }
        String keyID = image.getSource();
        String imgname = image.getImgName();
        StorageKey key = keysService.selectKeys(keyID);
        //删除图片
        boolean isDele = false;
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
        }else if (key.getStorageType() == 6) {
            isDele = cosImageupload.delCOS(key.getId(), imgname);
        }else if (key.getStorageType() == 7) {
            isDele = ftpImageupload.delFTP(key.getId(), imgname);
        }else if (key.getStorageType() == 8) {
            isDele = uFileImageupload.delUFile(key.getId(), imgname);
        }else {
            System.err.println("未获取到对象存储参数，删除失败。");
        }
        //删除库
        if(isDele){
            try {
                imgAndAlbumService.deleteImgAndAlbum(imgname);
                imgTempService.delImgAndExp(image.getImgUid());
                imgService.deleteImgById(image.getId());
            } catch (Exception e) {
                e.printStackTrace();
                msg.setInfo("图片记录时发生错误");
                msg.setCode("500");
                return msg;
            }
            msg.setInfo("删除成功");
        }else{
            imgAndAlbumService.deleteImgAndAlbum(imgname);
            imgTempService.delImgAndExp(image.getImgUid());
            imgService.deleteImgById(image.getId());
            msg.setInfo("图片记录已删除，但是图片源删除失败");
            msg.setCode("500");
        }
        System.out.println("返回的值："+msg.toString());
        return msg;
    }


    //没有权限
    @PostMapping("/authError")
    
    public Msg authError(HttpServletRequest request){
        Msg msg = new Msg();
        msg.setCode("4031");
        msg.setInfo("You don't have authority");
        return msg;
    }

    //认证失败
    @PostMapping("/jurisError")
    public Msg jurisError(HttpServletRequest request){
        Msg msg = new Msg();
        msg.setCode("4031");
        msg.setInfo("Authentication request failed");
        return msg;
    }



}
