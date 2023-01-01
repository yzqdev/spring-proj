package cn.hellohao.service.impl;

import cn.hellohao.model.entity.Msg;
import cn.hellohao.model.entity.StorageKey;
import cn.hellohao.model.entity.ReturnImage;
import cn.hellohao.util.*;
import com.UpYun;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class UFileImageupload {
    static UpYun uFile;
    static StorageKey key;

    public ReturnImage ImageuploadUFile(Map<String, File> fileMap, String username,String keyID) {
        ReturnImage returnImage = new ReturnImage();
        File file = null;
        ObjectMetadata meta = new ObjectMetadata();
        meta.setHeader("Content-Disposition", "inline");
        try {
            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                String ShortUID = SetText.getShortUuid();
                file = entry.getValue();   Msg fileMiME = TypeDict.FileMiME(file);
                meta.setHeader("content-type", fileMiME.getData().toString());
                uFile.setContentMD5(UpYun.md5(file));
                boolean result = uFile.writeFile(username + "/" + ShortUID + "." + entry.getKey(), file, true);
                if(result){
                    returnImage.setImgName(username + "/" + ShortUID + "." + entry.getKey());
                    returnImage.setImgUrl(key.getRequestAddress() + "/" +username + "/" + ShortUID + "." + entry.getKey());
                    returnImage.setImgSize(entry.getValue().length());
                    returnImage.setCode("200");
                }else{
                    returnImage.setCode("400");
                    System.err.println("上传失败");
                }
            }
        }catch(Exception e){
            returnImage.setCode("500");
        }
        return returnImage;


    }

    //ufile初始化
    public static Integer Initialize(StorageKey k) {
        int ret = -1;
        if(k.getAccessSecret()!=null && k.getAccessKey()!=null
                && k.getBucketName()!=null && k.getRequestAddress()!=null ) {
            if(!k.getAccessSecret().equals("") && !k.getAccessKey().equals("")
                    && !k.getBucketName().equals("") && !k.getRequestAddress().equals("") ) {
                // 创建UpYun实例。
                UpYun uFile = new UpYun(k.getBucketName(), k.getAccessKey(), k.getAccessSecret());
                List<UpYun.FolderItem> items = null;
                try {
                    items = uFile.readDir("/",null);
                    ret = 1;
                    uFile = uFile;
                    key = k;
                } catch (Exception e) {
                    System.out.println("UFile Object Is null");
                    ret = -1;
                }
            }
        }
        return ret;
    }

    public Boolean delUFile(String keyID, String fileName) {
        boolean b = true;
        try {
            boolean result = uFile.deleteFile(fileName, null);
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b;
    }

}
