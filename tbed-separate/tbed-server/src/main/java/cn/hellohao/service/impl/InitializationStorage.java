package cn.hellohao.service.impl;

import cn.hellohao.auth.filter.SubjectFilter;
import cn.hellohao.mapper.KeysMapper;
import cn.hellohao.model.entity.StorageKey;
import cn.hellohao.util.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/28 15:33
 */

@Component
@Order(2)
public class InitializationStorage implements CommandLineRunner {

    @Value("${CROS_ALLOWED_ORIGINS}")
    private String[] allowedOrigins;

    @Value("${server.port}")
    private String port;
    @Autowired
    private KeysMapper keysMapper;

    @Override
    public void run(String... args) throws Exception {
        SubjectFilter.WEBHOST = allowedOrigins;
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        intiStorage();
        sout();
    }
    public void intiStorage(){
        List<StorageKey> keylist = keysMapper.getKeys();
        for (StorageKey key : keylist) {
            if(key.getStorageType()!=0 && key.getStorageType()!=null){
                int ret =0;
                if(key.getStorageType()==1){
                    ret =NOSImageupload.Initialize(key);
                }else if (key.getStorageType()==2){
                    ret =OSSImageupload.Initialize(key);
                }else if(key.getStorageType()==3){
                    ret = USSImageupload.Initialize(key);
                }else if(key.getStorageType()==4){
                    ret = KODOImageupload.Initialize(key);
                }else if(key.getStorageType()==6){
                    ret = COSImageupload.Initialize(key);
                }else if(key.getStorageType()==7){
                    ret = FTPImageupload.Initialize(key);
                }else if(key.getStorageType()==8){
                    ret = UFileImageupload.Initialize(key);
                }
            }
        }
    }

    public void sout(){
        Print.Normal("______________________________________________");
        Print.Normal("              Hellohao Tbed                ");
        Print.Normal("     Successful startup of the program      ");
        Print.Normal("     is OK!  Open http://localhost:"+port+"       ");
        Print.Normal("     is OK!  Open swagger http://localhost:"+port+"/swagger-ui.html");
        Print.Normal("______________________________________________");
    }
}
