package cn.hellohao.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.hellohao.model.entity.StorageKey;
import cn.hellohao.model.dto.HomeImgDto;
import cn.hellohao.model.dto.ImgSearchDto;
import cn.hellohao.model.vo.ImageVo;
import cn.hellohao.model.vo.RecentUserVo;
import cn.hellohao.util.Print;
import com.UpYun;
import com.aliyun.oss.OSSClient;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.region.Region;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import com.upyun.UpException;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;

import com.netease.cloud.auth.BasicCredentials;
import com.netease.cloud.auth.Credentials;
import com.netease.cloud.services.nos.NosClient;
import com.netease.cloud.services.nos.model.Bucket;
import com.netease.cloud.services.nos.model.CannedAccessControlList;
import com.netease.cloud.services.nos.transfer.TransferManager;

import cn.hellohao.mapper.ImgMapper;
import cn.hellohao.model.entity.Images;
import cn.hellohao.service.ImgService;

import javax.annotation.Resource;

@Service
public class ImgServiceImpl extends ServiceImpl<ImgMapper, Images> implements ImgService {
    @Resource
    private ImgMapper imgMapper;

    @Override
    public List<Images> selectImages(ImgSearchDto  imgSearchDto) {
        return imgMapper.selectImageData(imgSearchDto);
    }

    @Override
    public Integer deleteImgById(String id) {

        return imgMapper.deleteById(id);
    }

    @Override
    public Integer deleimgForImgUid(String imguid) {
        return imgMapper.deleimgForImgUid(imguid);
    }

    @Override
    public Images selectByPrimaryKey(String id) {
        return imgMapper.selectByPrimaryKey(id);
    }

    //删除对象存储的图片文件
    public void delect(StorageKey key, String fileName) {
        // 初始化
        Credentials credentials = new BasicCredentials(key.getAccessKey(), key.getAccessSecret());
        NosClient nosClient = new NosClient(credentials);
        nosClient.setEndpoint(key.getEndpoint());
        // 初始化TransferManager
        TransferManager transferManager = new TransferManager(nosClient);
        //列举桶
        ArrayList bucketList = new ArrayList();
        String tname = "";
        for (Bucket bucket : nosClient.listBuckets()) {
            bucketList.add(bucket.getName());
        }
        for (Object object : bucketList) {
            tname = object.toString();
            //查看桶的ACL
            CannedAccessControlList acl = nosClient.getBucketAcl(object.toString());
        }
        //这种方法不能删除指定文件夹下的文件
        boolean isExist = nosClient.doesObjectExist(tname, fileName, null);
        System.out.println("文件是否存在：" + isExist);
        if (isExist) {
            nosClient.deleteObject(tname, fileName);
        }
    }

    public void delectOSS(StorageKey key, String fileName) {
        String endpoint = key.getEndpoint();
        String accessKeyId = key.getAccessKey();
        String accessKeySecret = key.getAccessSecret();
        String bucketName = key.getBucketName();
        String objectName = fileName;
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ossClient.deleteObject(bucketName, objectName);
        ossClient.shutdown();
    }

    //删除USS对象存储的图片文件
    public void delectUSS(StorageKey key, String fileName) {
        UpYun upyun = new UpYun(key.getBucketName(), key.getAccessKey(), key.getAccessSecret());
        try {
            boolean result = upyun.deleteFile(fileName, null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UpException e) {
            e.printStackTrace();
        }
    }

    public void delectUFile(StorageKey key, String fileName) {
        UpYun upyun = new UpYun(key.getBucketName(), key.getAccessKey(), key.getAccessSecret());
        try {
            boolean result = upyun.deleteFile(fileName, null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UpException e) {
            e.printStackTrace();
        }
    }

    public void delectKODO(StorageKey key, String fileName) {
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
        Auth auth = Auth.create(key.getAccessKey(), key.getAccessSecret());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(key.getBucketName(), fileName);
        } catch (QiniuException ex) {

            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }

    //删除COS对象存储的图片文件
    public void delectCOS(StorageKey key, String fileName) {
        COSCredentials cred = new BasicCOSCredentials(key.getAccessKey(), key.getAccessSecret());
        Region region = new Region(key.getEndpoint());
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cosClient = new COSClient(cred, clientConfig);
        try {
            String bucketName = key.getBucketName();
            String userkey = fileName;
            cosClient.deleteObject(key.getBucketName(), userkey);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
    }

    public void delectFTP(StorageKey key, String fileName) {
        FTPClient ftp = new FTPClient();
        String[] host = key.getEndpoint().split("\\:");
        String h = host[0];
        Integer p = Integer.parseInt(host[1]);
        try {
            if (!ftp.isConnected()) {
                ftp.connect(h, p);
            }
            ftp.login(key.getAccessKey(), key.getAccessSecret());
            ftp.deleteFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            Print.warning("删除FTP存储的图片失败");
        }
    }

    @Override
    public Integer counts(String userid) {
        // TODO Auto-generated method stub

        return imgMapper.counts(userid);
    }

    @Override
    public Integer countimg(String userid) {
        return Math.toIntExact(imgMapper.selectCount(new LambdaQueryWrapper<Images>().eq(Images::getUserId, userid)));
        //return imgMapper.countimg(userid);
    }

    @Override
    public Integer setImg(Images images) {
        return imgMapper.setImg(images);
    }

    @Override
    public Integer deleimgname(String imgname) {
        return imgMapper.deleimgname(imgname);
    }

    @Override
    public Integer deleall(String id) {
        return imgMapper.deleall(id);
    }

    @Override
    public List<Images> gettimeimg(String time) {
        return imgMapper.getTimeImg(time);
    }

    @Override
    public Long getUserMemory(String userid) {
        return imgMapper.getUserMemory(userid);
    }

    @Override
    public Long getSourceMemory(String source) {
        return imgMapper.getSourceMemory(source);
    }

    @Override
    public Integer md5Count(Images images) {
        return imgMapper.md5Count(images);
    }

    @Override
    public Images selectImgUrlByMD5(String md5key) {
        return imgMapper.selectImgUrlByMD5(md5key);
    }

    @Override
    public List<Images> recentlyUploaded(String userid) {

        return imgMapper.recentlyUploaded(userid);
    }

    @Override
    public List<RecentUserVo> recentlyUser() {
        return imgMapper.recentlyUser();
    }


    @Override
    public List<String> getyyyy(String userid) {
        return imgMapper.getyyyy(userid);
    }

    @Override
    public List<ImageVo> countByM(HomeImgDto images) {
        return imgMapper.countByM(images);
    }

    @Override
    public Images selectImgUrlByImgUID(String imguid) {
        return imgMapper.selectOne(new LambdaQueryWrapper<Images>().eq(Images::getImgUid, imguid));

    }

}
