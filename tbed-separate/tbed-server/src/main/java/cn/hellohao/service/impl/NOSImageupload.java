package cn.hellohao.service.impl;

import java.io.File;
import java.util.*;

import cn.hellohao.model.entity.ReturnImage;
import cn.hellohao.model.entity.StorageKey;
import cn.hellohao.util.*;
import com.netease.cloud.services.nos.model.ObjectListing;
import org.springframework.stereotype.Service;

import com.netease.cloud.auth.BasicCredentials;
import com.netease.cloud.auth.Credentials;
import com.netease.cloud.services.nos.NosClient;

@Service
public class NOSImageupload {
    static NosClient nosClient;
    static StorageKey key;

    public ReturnImage Imageupload(Map<String, File> fileMap, String username,String keyID){
        ReturnImage returnImage = new ReturnImage();
        File file = null;
        try {
            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                String ShortUID = SetText.getShortUuid();
                file = entry.getValue();
                nosClient.putObject(key.getBucketName(), username + "/" + ShortUID+ "." + entry.getKey(), file);
                returnImage.setImgName(username + "/" + ShortUID+ "." + entry.getKey());
                returnImage.setImgUrl(key.getRequestAddress() + "/" + username + "/" + ShortUID + "." + entry.getKey());
                returnImage.setImgSize(entry.getValue().length());
                returnImage.setCode("200");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnImage.setCode("500");
        }
        return returnImage;

    }


    //初始化网易NOS对象存储
    public static Integer Initialize(StorageKey k) {
        int ret = -1;
        if(k.getEndpoint()!=null && k.getAccessSecret()!=null && k.getEndpoint()!=null
                && k.getBucketName()!=null && k.getRequestAddress()!=null ){
            if(!k.getEndpoint().equals("") && !k.getAccessSecret().equals("") && !k.getEndpoint().equals("")
                    && !k.getBucketName().equals("") && !k.getRequestAddress().equals("") ){
                // 初始化
                Credentials credentials = new BasicCredentials(k.getAccessKey(), k.getAccessSecret());
                NosClient nosClient = new NosClient(credentials);
                nosClient.setEndpoint(k.getEndpoint());
                ObjectListing objectListing = null;
                try {
                    objectListing = nosClient.listObjects(k.getBucketName());
                    ret = 1;
                    nosClient = nosClient;
                    key = k;
                }catch (Exception e){
                    System.out.println("NOS Object Is null");
                    ret = -1;
                }
            }
        }
        //throw new StorageSourceInitException("当前数据源配置不完整，请管理员前往后台配置。");
        return ret;
    }


    public Boolean delNOS(String keyID, String fileName) {
        boolean b =true;
        try {
            //这种方法不能删除指定文件夹下的文件
            boolean isExist = nosClient.doesObjectExist(key.getBucketName(), fileName, null);
            if (isExist) {
                nosClient.deleteObject(key.getBucketName(), fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            b =false;
        }
        return b;
    }


}
