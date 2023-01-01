import im.zhaojun.zfile.model.dto.SystemConfigDTO;
import im.zhaojun.zfile.model.entity.SystemConfig;
import im.zhaojun.zfile.repository.SystemConfigRepository;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @author yanni
 * @date time 2022/3/29 11:25
 * @modified By:
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@ExtendWith(SpringExtension.class)
public class SystemDTOTest {
    private Class<SystemConfigDTO> systemConfigClazz = SystemConfigDTO.class;
    @Resource
    private SystemConfigRepository systemConfigRepository;
    @Test
    public void reflectTest() throws IllegalAccessException, NoSuchFieldException {
        SystemConfigDTO systemConfigDTO = SystemConfigDTO.builder().id(1).domain("http://localhost:8080").build();
        String key = systemConfigDTO.getDomain();
        Field f = systemConfigClazz.getDeclaredField("domain");
        f.setAccessible(true);
        System.out.println(f.get(systemConfigDTO));

    }

    @Test
   public void findAll(){
        List<SystemConfig> systemConfigList = systemConfigRepository.findAll();
        System.out.println(systemConfigList);
    }
}
