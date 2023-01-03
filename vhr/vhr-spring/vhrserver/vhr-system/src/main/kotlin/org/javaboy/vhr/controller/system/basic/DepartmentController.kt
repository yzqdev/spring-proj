package org.javaboy.vhr.controller.system.basicimport

import org.javaboy.vhr.model.Department
import org.javaboy.vhr.model.RespBean
import org.javaboy.vhr.model.RespBean.Companion.ok
import org.javaboy.vhr.service.DepartmentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

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
import org.javaboy.vhr.service.NationService
import org.javaboy.vhr.service.PoliticsstatusService
import org.javaboy.vhr.service.JobLevelService
import org.javaboy.vhr.service.PositionService
import org.javaboy.vhr.service.DepartmentService
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
 * @时间 2019-10-21 8:02
 */
@RestController
@RequestMapping("/system/basic/department")
class DepartmentController {
    @Autowired
    var departmentService: DepartmentService? = null

    @get:GetMapping("/")
    val allDepartments: List<Department>
        get() = departmentService!!.allDepartments

    @PostMapping("/")
    fun addDep(@RequestBody dep: Department): RespBean {
        departmentService!!.addDep(dep)
        return if (dep.result == 1) {
            ok("添加成功", dep)
        } else RespBean.error("添加失败")
    }

    @DeleteMapping("/{id}")
    fun deleteDepById(@PathVariable id: Int?): RespBean {
        val dep = Department()
        dep.id = id
        departmentService!!.deleteDepById(dep)
        if (dep.result == -2) {
            return RespBean.error("该部门下有子部门，删除失败")
        } else if (dep.result == -1) {
            return RespBean.error("该部门下有员工，删除失败")
        } else if (dep.result == 1) {
            return ok("删除成功")
        }
        return RespBean.error("删除失败")
    }
}