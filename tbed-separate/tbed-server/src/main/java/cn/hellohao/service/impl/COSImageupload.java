package cn.hellohao.service.impl;

import cn.hellohao.model.entity.StorageKey;
import cn.hellohao.model.entity.ReturnImage;
import cn.hellohao.util.*;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class COSImageupload {
    static COSClient cosClient;
    static StorageKey key;

    public ReturnImage ImageuploadCOS(Map<String, File> fileMap, String username,String keyID) {
        ReturnImage returnImage = new ReturnImage();
        File file = null;
        Map<ReturnImage, Integer> ImgUrl = new HashMap<>();
        try {
            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                String ShortUID = SetText.getShortUuid();
                file = entry.getValue();
                try {
                    String bucketName = key.getBucketName();
                    String userkey =username + "/" + ShortUID + "." + entry.getKey();
                    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, userkey, file);
                    PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
                    returnImage.setImgName(userkey);
                    returnImage.setImgUrl(key.getRequestAddress() + "/" + userkey);
                    returnImage.setImgSize(entry.getValue().length());
                    returnImage.setCode("200");
                } catch (CosServiceException serverException) {
                    returnImage.setCode("400");
                    serverException.printStackTrace();
                } catch (CosClientException clientException) {
                    returnImage.setCode("400");
                    clientException.printStackTrace();
                }
            }
        }catch(Exception e){

            e.printStackTrace();
            returnImage.setCode("500");
        }
        return returnImage;
    }


    public static Integer Initialize(StorageKey k) {
        int ret = -1;
        if(k.getEndpoint()!=null && k.getAccessSecret()!=null && k.getEndpoint()!=null
                && k.getBucketName()!=null && k.getRequestAddress()!=null ) {
            if (!k.getEndpoint().equals("") && !k.getAccessSecret().equals("") && !k.getEndpoint().equals("")
                    && !k.getBucketName().equals("") && !k.getRequestAddress().equals("")) {
                String secretId = k.getAccessKey();
                String secretKey = k.getAccessSecret();
                COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
                Region region = new Region(k.getEndpoint());
                ClientConfig clientConfig = new ClientConfig(region);
                COSClient cosClient = new COSClient(cred, clientConfig);
                ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
                listObjectsRequest.setBucketName(k.getBucketName());
                listObjectsRequest.setDelimiter("/");
                listObjectsRequest.setMaxKeys(1);
                ObjectListing objectListing = null;
                try {
                    objectListing = cosClient.listObjects(listObjectsRequest);
                    ret = 1;
                    cosClient = cosClient;
                    key = k;
                } catch (Exception e) {
                    System.out.println("COS Object Is null");
                    ret = -1;
                }
            }
        }
        return ret;
    }

    public Boolean delCOS(String keyID, String fileName) {
        boolean b = true;
        try {
            cosClient.deleteObject(key.getBucketName(), fileName);
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b ;
    }




}
