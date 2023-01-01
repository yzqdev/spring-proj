package cn.hellohao.service.impl;

import cn.hellohao.model.entity.StorageKey;
import cn.hellohao.model.entity.ReturnImage;
import cn.hellohao.util.*;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Service
public class KODOImageupload {
    static String upToken;
    static BucketManager bucketManager;
    static StorageKey key;

    public ReturnImage ImageuploadKODO(Map<String, File> fileMap, String username, String keyID){
        ReturnImage returnImage = new ReturnImage();
        StorageKey key = null;
        Configuration cfg;
        if (key.getEndpoint().equals("1")) {
            cfg = new Configuration(Zone.zone0());
        } else if (key.getEndpoint().equals("2")) {
            cfg = new Configuration(Zone.zone1());
        } else if (key.getEndpoint().equals("3")) {
            cfg = new Configuration(Zone.zone2());
        } else if (key.getEndpoint().equals("4")) {
            cfg = new Configuration(Zone.zoneNa0());
        } else {
            cfg = new Configuration(Zone.zoneAs0());
        }
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(key.getAccessKey(), key.getAccessSecret());
        String upToken = auth.uploadToken(key.getBucketName(),null,7200,null);
        File file = null;
        try {
            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                String ShortUID = SetText.getShortUuid();
                file = entry.getValue();
                try {
                    Response response = uploadManager.put(file,username + "/" + ShortUID + "." + entry.getKey(),upToken);
                    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                    returnImage.setImgName(username + "/" + ShortUID + "." + entry.getKey());
                    returnImage.setImgUrl(key.getRequestAddress() + "/" + username + "/" + ShortUID + "." + entry.getKey());
                    returnImage.setImgSize(entry.getValue().length());
                    returnImage.setCode("200");
                } catch (QiniuException ex) {
                    Response r = ex.response;
                    System.err.println(r.toString());
                    try {
                        System.err.println(r.bodyString());
                    } catch (QiniuException ex2) {
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            returnImage.setCode("500");
        }
        return returnImage;
    }

    public static Integer Initialize(StorageKey k) {
        int ret = -1;
        if (k.getEndpoint() != null && k.getAccessSecret() != null && k.getEndpoint() != null
                && k.getBucketName() != null && k.getRequestAddress() != null) {
            if (!k.getEndpoint().equals("") && !k.getAccessSecret() .equals("") && !k.getEndpoint() .equals("")
                    && !k.getBucketName().equals("") && !k.getRequestAddress() .equals("")) {
                Configuration cfg;
                if (k.getEndpoint().equals("1")) {
                    cfg = new Configuration(Zone.zone0());
                } else if (k.getEndpoint().equals("2")) {
                    cfg = new Configuration(Zone.zone1());
                } else if (k.getEndpoint().equals("3")) {
                    cfg = new Configuration(Zone.zone2());
                } else if (k.getEndpoint().equals("4")) {
                    cfg = new Configuration(Zone.zoneNa0());
                } else {
                    cfg = new Configuration(Zone.zoneAs0());
                }
                UploadManager uploadManager = new UploadManager(cfg);
                Auth auth = Auth.create(k.getAccessKey(), k.getAccessSecret());
                String upToken = auth.uploadToken(k.getBucketName(),null,7200,null);//auth.uploadToken(k.getBucketname());
                BucketManager bucketManager = new BucketManager(auth, cfg);
                BucketManager.FileListIterator fileListIterator = null;
                try {
                    fileListIterator = bucketManager.createFileListIterator(k.getBucketName(), "", 1, "/");
                    FileInfo[] items = fileListIterator.next();
                    if(items!=null){
                        ret = 1;
                        bucketManager = bucketManager;
                        key = k;
                    }
                }catch (Exception e){
                    System.out.println("KODO Object Is null");
                    ret = -1;
                }
            }
        }
        return ret;
    }

    public Boolean delKODO(String keyID, String fileName) {
        boolean b = true;
        try {
            bucketManager.delete(key.getBucketName(), fileName);
        } catch (Exception ex) {
            b = false;
        }
        return b;
    }


}
