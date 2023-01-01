package cn.hellohao.service.impl;

import cn.hellohao.model.entity.Msg;
import cn.hellohao.model.entity.StorageKey;
import cn.hellohao.model.entity.ReturnImage;
import cn.hellohao.util.*;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.*;

@Service
public class OSSImageupload {
    static OSSClient ossClient;
    static StorageKey key;

    public ReturnImage ImageuploadOSS(Map<String, File> fileMap, String username,String keyID){
        ReturnImage returnImage = new ReturnImage();
        File file = null;
        ObjectMetadata meta = new ObjectMetadata();
        meta.setHeader("Content-Disposition", "inline");
        try {
            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                String ShortUID = SetText.getShortUuid();
                file = entry.getValue();  Msg fileMiME = TypeDict.FileMiME(file);
                meta.setHeader("content-type", fileMiME.getData().toString());
                System.out.println("待上传的图片："+username + "/" + ShortUID + "." + entry.getKey());
                ossClient.putObject(key.getBucketName(), username + "/" + ShortUID + "." + entry.getKey(),file);
                returnImage.setImgName(username + "/" + ShortUID + "." + entry.getKey());//entry.getValue().getOriginalFilename()
                returnImage.setImgUrl(key.getRequestAddress() + "/" + username + "/" + ShortUID + "." + entry.getKey());
                returnImage.setImgSize(file.length());
                returnImage.setCode("200");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnImage.setCode("500");
        }
        return returnImage;

    }

    public static Integer Initialize(StorageKey k) {
        int ret = -1;
        ObjectListing objectListing = null;
        if(k.getEndpoint()!=null && k.getAccessSecret()!=null && k.getAccessKey()!=null && k.getEndpoint()!=null
                && k.getBucketName()!=null && k.getRequestAddress()!=null ) {
            if(!k.getEndpoint().equals("") && !k.getAccessSecret().equals("") && !k.getAccessKey().equals("") && !k.getEndpoint().equals("")
                    && !k.getBucketName().equals("") && !k.getRequestAddress().equals("") ) {
                OSS ossClient = new OSSClientBuilder().build(k.getEndpoint(), k.getAccessKey(), k.getAccessSecret());
                try {
                    objectListing = ossClient.listObjects(k.getBucketName());
                    ret=1;
                    ossClient = ossClient;
                    key = k;
                } catch (Exception e) {
                    System.out.println("OSS Object Is null");
                    ret = -1;
                }
            }
        }
        return ret;
    }

    public boolean delOSS(String keyID, String fileName){
        boolean b =true;
        try {
            ossClient.deleteObject(key.getBucketName(), fileName);
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b;
    }


}
