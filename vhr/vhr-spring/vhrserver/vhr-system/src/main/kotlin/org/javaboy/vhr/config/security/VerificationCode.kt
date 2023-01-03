package org.javaboy.vhr.config.securityimport

import java.awt.BasicStrokeimport

java.awt.Colorimport java.awt.Fontimport java.awt.Graphics2Dimport java.awt.image.BufferedImageimport java.io.IOExceptionimport java.io.OutputStreamimport java.util.*import javax.imageio.ImageIO

org.javaboy.vhr.model.Employee.id
import org.javaboy.vhr.model.Employee.name
import org.javaboy.vhr.model.Employee.workID
import org.javaboy.vhr.model.Employee.gender
import org.javaboy.vhr.model.Employee.birthday
import org.javaboy.vhr.model.Employee.idCard
import org.javaboy.vhr.model.Employee.wedlock
import org.javaboy.vhr.model.Employee.nation
import org.javaboy.vhr.model.Nation.getName
import org.javaboy.vhr.model.Employee.nativePlace
import org.javaboy.vhr.model.Employee.politicsstatus
import org.javaboy.vhr.model.Politicsstatus.getName
import org.javaboy.vhr.model.Employee.phone
import org.javaboy.vhr.model.Employee.address
import org.javaboy.vhr.model.Employee.department
import org.javaboy.vhr.model.Department.getName
import org.javaboy.vhr.model.Employee.jobLevel
import org.javaboy.vhr.model.JobLevel.name
import org.javaboy.vhr.model.Employee.position
import org.javaboy.vhr.model.Position.getName
import org.javaboy.vhr.model.Employee.engageForm
import org.javaboy.vhr.model.Employee.tiptopDegree
import org.javaboy.vhr.model.Employee.specialty
import org.javaboy.vhr.model.Employee.school
import org.javaboy.vhr.model.Employee.beginDate
import org.javaboy.vhr.model.Employee.workState
import org.javaboy.vhr.model.Employee.email
import org.javaboy.vhr.model.Employee.contractTerm
import org.javaboy.vhr.model.Employee.beginContract
import org.javaboy.vhr.model.Employee.endContract
import org.javaboy.vhr.model.Employee.conversionTime
import org.javaboy.vhr.model.Employee.nationId
import org.javaboy.vhr.model.Nation.id
import org.javaboy.vhr.model.Employee.politicId
import org.javaboy.vhr.model.Politicsstatus.id
import org.javaboy.vhr.model.Employee.departmentId
import org.javaboy.vhr.model.Department.id
import org.javaboy.vhr.model.Employee.jobLevelId
import org.javaboy.vhr.model.JobLevel.id
import org.javaboy.vhr.model.Employee.posId
import org.javaboy.vhr.model.Position.id
import org.javaboy.vhr.model.Hr.setUsername
import org.javaboy.vhr.model.Hr.password
import org.javaboy.vhr.model.RespBean.Companion.ok
import org.javaboy.vhr.model.RespBean.setMsg
import org.javaboy.vhr.model.Salary.createDate
import org.javaboy.vhr.model.Hr.roles
import org.javaboy.vhr.model.Hr.id
import org.javaboy.vhr.model.RespPageBean.data
import org.javaboy.vhr.model.RespPageBean.total
import org.javaboy.vhr.model.JobLevel.createDate
import org.javaboy.vhr.model.JobLevel.enabled
import org.javaboy.vhr.model.Position.enabled
import org.javaboy.vhr.model.Position.createDate
import org.javaboy.vhr.model.Department.enabled
import org.javaboy.vhr.model.RespBean.Companion.build
import org.javaboy.vhr.model.RespBean.setStatus
import org.javaboy.vhr.model.RespBean.setObj
import org.javaboy.vhr.model.Department.result
import org.javaboy.vhr.model.ChatMsg.from
import org.javaboy.vhr.model.Hr.getUsername
import org.javaboy.vhr.model.ChatMsg.fromNickname
import org.javaboy.vhr.model.Hr.name
import org.javaboy.vhr.model.ChatMsg.date
import org.javaboy.vhr.model.ChatMsg.to
import org.javaboy.vhr.model.Hr.userface
import org.springframework.beans.factory.annotation.Autowired
import org.javaboy.vhr.service.MailSendLogService
import org.javaboy.vhr.service.EmployeeService
import org.springframework.scheduling.annotation.Scheduled
import org.javaboy.vhr.model.MailSendLog
import org.javaboy.vhr.model.Employee
import org.javaboy.vhr.model.MailConstants
import org.javaboy.vhr.model.Hr
import org.javaboy.vhr.utils.JwtUtil
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile
import org.javaboy.vhr.model.Nation
import org.javaboy.vhr.model.Politicsstatus
import org.javaboy.vhr.model.Department
import org.javaboy.vhr.model.JobLevel
import org.javaboy.vhr.utils.ColorEnum
import org.javaboy.vhr.utils.ColorUtil
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession
import org.javaboy.vhr.config.security.LoginFilter
import org.springframework.web.filter.OncePerRequestFilter
import org.javaboy.vhr.service.HrService
import javax.servlet.ServletException
import org.javaboy.vhr.config.security.CustomFilterInvocationSecurityMetadataSource
import org.javaboy.vhr.config.security.CustomUrlDecisionManager
import org.javaboy.vhr.model.RespBean
import org.javaboy.vhr.service.MenuService
import org.springframework.web.cors.CorsConfiguration
import org.javaboy.vhr.config.FastDFSUtils
import org.javaboy.vhr.config.RabbitConfig
import org.aspectj.lang.JoinPoint
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import org.aspectj.lang.annotation.AfterReturning
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import org.springdoc.core.GroupedOpenApi
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.javaboy.vhr.config.SwaggerUiWebMvcConfigurer
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.javaboy.vhr.model.OpLog
import org.javaboy.vhr.model.HrRole
import org.javaboy.vhr.model.Salary
import org.javaboy.vhr.model.SysMsg
import org.javaboy.vhr.model.Appraise
import org.javaboy.vhr.model.MenuRole
import org.javaboy.vhr.model.EmpSalary
import org.javaboy.vhr.model.Employeeec
import org.javaboy.vhr.model.MsgContent
import org.javaboy.vhr.model.AdjustSalary
import org.javaboy.vhr.model.Employeetrain
import org.javaboy.vhr.model.Employeeremove
import lombok.RequiredArgsConstructor
import org.javaboy.vhr.mapper.SalaryMapper
import org.javaboy.vhr.service.SalaryService
import org.javaboy.vhr.mapper.HrMapper
import org.javaboy.vhr.mapper.HrRoleMapper
import org.javaboy.vhr.utils.HrUtils
import org.springframework.cache.annotation.CacheConfig
import org.javaboy.vhr.mapper.MenuMapper
import org.javaboy.vhr.mapper.MenuRoleMapper
import org.springframework.cache.annotation.Cacheable
import org.javaboy.vhr.mapper.RoleMapper
import org.javaboy.vhr.mapper.NationMapper
import org.javaboy.vhr.mapper.EmployeeMapper
import org.javaboy.vhr.model.RespPageBean
import org.javaboy.vhr.mapper.JobLevelMapper
import org.javaboy.vhr.mapper.PositionMapper
import org.javaboy.vhr.mapper.DepartmentMapper
import org.javaboy.vhr.mapper.MailSendLogMapper
import org.javaboy.vhr.mapper.PoliticsstatusMapper
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.javaboy.vhr.service.NationService
import org.javaboy.vhr.service.PoliticsstatusService
import org.javaboy.vhr.service.JobLevelService
import org.javaboy.vhr.service.PositionService
import org.javaboy.vhr.service.DepartmentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.javaboy.vhr.utils.POIUtils
import org.javaboy.vhr.service.RoleService
import org.javaboy.vhr.model.ChatMsg
import org.javaboy.vhr.config.security.VerificationCode
import org.javaboy.vhr.controller.LoginController
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.boot.SpringApplication

/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 * @时间 2019-09-29 7:37
 *
 * 生成验证码的工具类
 */
class VerificationCode {
    private val width = 100 // 生成验证码图片的宽度
    private val height = 30 // 生成验证码图片的高度
    private val fontNames = arrayOf("隶书", "微软雅黑")
    private val bgColor = Color(255, 255, 255) // 定义验证码图片的背景颜色为白色
    private val random = Random()
    private val codes = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    var text // 记录随机字符串
            : String? = null
        private set

    /**
     * 获取一个随意颜色
     *
     * @return
     */
    private fun randomColor(): Color {
        val red = random.nextInt(150)
        val green = random.nextInt(150)
        val blue = random.nextInt(150)
        return Color(red, green, blue)
    }

    /**
     * 获取一个随机字体
     *
     * @return
     */
    private fun randomFont(): Font {
        val name = fontNames[random.nextInt(fontNames.size)]
        val style = random.nextInt(4)
        val size = random.nextInt(5) + 24
        return Font(name, style, size)
    }

    /**
     * 获取一个随机字符
     *
     * @return
     */
    private fun randomChar(): Char {
        return codes[random.nextInt(codes.length)]
    }

    /**
     * 创建一个空白的BufferedImage对象
     *
     * @return
     */
    private fun createImage(): BufferedImage {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val g2 = image.graphics as Graphics2D
        g2.color = bgColor // 设置验证码图片的背景颜色
        g2.fillRect(0, 0, width, height)
        return image
    }

    val image: BufferedImage
        get() {
            val image = createImage()
            val g2 = image.graphics as Graphics2D
            val sb = StringBuffer()
            for (i in 0..3) {
                val s = randomChar().toString() + ""
                sb.append(s)
                g2.color = randomColor()
                g2.font = randomFont()
                val x = i * width * 1.0f / 4
                g2.drawString(s, x, (height - 8).toFloat())
            }
            text = sb.toString()
            drawLine(image)
            return image
        }

    /**
     * 绘制干扰线
     *
     * @param image
     */
    private fun drawLine(image: BufferedImage) {
        val g2 = image.graphics as Graphics2D
        val num = 5
        for (i in 0 until num) {
            val x1 = random.nextInt(width)
            val y1 = random.nextInt(height)
            val x2 = random.nextInt(width)
            val y2 = random.nextInt(height)
            g2.color = randomColor()
            g2.stroke = BasicStroke(1.5f)
            g2.drawLine(x1, y1, x2, y2)
        }
    }

    companion object {
        @Throws(IOException::class)
        fun output(image: BufferedImage?, out: OutputStream?) {
            ImageIO.write(image, "JPEG", out)
        }
    }
}