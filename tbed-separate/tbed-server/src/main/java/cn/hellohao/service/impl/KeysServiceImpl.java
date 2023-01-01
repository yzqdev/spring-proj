package cn.hellohao.service.impl;

import cn.hellohao.model.entity.Msg;
import cn.hellohao.model.entity.StorageKey;
import cn.hellohao.util.Print;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import cn.hellohao.mapper.KeysMapper;
import cn.hellohao.service.KeysService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeysServiceImpl extends ServiceImpl<KeysMapper, StorageKey> implements KeysService {

   
    private final KeysMapper keysMapper;

   
    private final NOSImageupload nOSImageupload;
   
    private final OSSImageupload ossImageupload;
   
    private final USSImageupload ussImageupload;
   
    private final KODOImageupload kodoImageupload;
   
    private final COSImageupload cosImageupload;
   
    private final FTPImageupload ftpImageupload;
   
    private final UFileImageupload uFileImageupload;


    @Override
    public StorageKey selectKeys(String id) {
        // TODO Auto-generated method stub
        return keysMapper.selectKeys(id);
    }
    @Override
    public List<StorageKey> getStorageName() {
        return keysMapper.getStorageName();
    }

    @Override
    public List<StorageKey> getStorage() {
        return keysMapper.getStorage();
    }

    @Override
    public Msg updateKey(StorageKey key) {
        Msg msg = new Msg();
        Integer ret = -2;
        //修改完初始化
        if(key.getStorageType()==1){
            ret =nOSImageupload.Initialize(key);//实例化网易
        }else if (key.getStorageType()==2){
            ret = ossImageupload.Initialize(key);
        }else if(key.getStorageType()==3 ){
            ret = ussImageupload.Initialize(key);
        }else if(key.getStorageType()==4){
            ret = kodoImageupload.Initialize(key);
        }else if(key.getStorageType()==5){
            ret = 1;
        }else if(key.getStorageType()==6){
            ret = cosImageupload.Initialize(key);
        }else if(key.getStorageType()==7){
            ret = ftpImageupload.Initialize(key);
        }else if(key.getStorageType()==8){
            ret = uFileImageupload.Initialize(key);
        }else{
            Print.Normal("为获取到存储参数，或者使用存储源是本地的。");
        }
        if(ret>0){
            keysMapper.updateById(key);
            msg.setInfo("保存成功");
        }else{
            if(key.getStorageType()==5){
                keysMapper.updateById(key);
                msg.setInfo("保存成功");
            }else{
                msg.setCode("4002");
                msg.setInfo("对象存储初始化失败,请检查参数是否正确");
            }
        }

        return msg;


    }

    @Override
    public List<StorageKey> getKeys() {
        return keysMapper.getKeys();
    }

}
