import im.zhaojun.zfile.model.dto.SystemConfigDTO
import im.zhaojun.zfile.repository.SystemConfigRepository
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.annotation.Resource

/**
 * @author yanni
 * @date time 2022/3/29 11:25
 * @modified By:
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class SystemDTOTest {
    private val systemConfigClazz = SystemConfigDTO::class.java

    @Resource
    private val systemConfigRepository: SystemConfigRepository? = null
    @Test
    @Throws(IllegalAccessException::class, NoSuchFieldException::class)
    fun reflectTest() {
        val systemConfigDTO: SystemConfigDTO = SystemConfigDTO.builder().id(1).domain("http://localhost:8080").build()
        val key: String = systemConfigDTO.getDomain()
        val f = systemConfigClazz.getDeclaredField("domain")
        f.isAccessible = true
        println(f[systemConfigDTO])
    }

    @Test
    fun findAll() {
        val systemConfigList = systemConfigRepository!!.findAll()
        println(systemConfigList)
    }
}