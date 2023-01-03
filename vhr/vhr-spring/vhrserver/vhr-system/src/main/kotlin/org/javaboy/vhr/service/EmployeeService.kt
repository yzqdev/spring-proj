package org.javaboy.vhr.service

org.springframework.amqp.rabbit.connection.CorrelationDataimport org.springframework.stereotype.Serviceimport java.text.DecimalFormatimport java.text.SimpleDateFormatimport java.util.*
/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 * @时间 2019-10-29 7:44
 */
@Service
class EmployeeService {
    @Autowired
    var employeeMapper: EmployeeMapper? = null

    @Autowired
    var rabbitTemplate: RabbitTemplate? = null

    @Autowired
    var mailSendLogService: MailSendLogService? = null
    var yearFormat = SimpleDateFormat("yyyy")
    var monthFormat = SimpleDateFormat("MM")
    var decimalFormat = DecimalFormat("##.00")
    fun getEmployeeByPage(page: Int?, size: Int?, employee: Employee?, beginDateScope: Array<Date?>?): RespPageBean {
        var page = page
        if (page != null && size != null) {
            page = (page - 1) * size
        }
        val data: List<Employee> = employeeMapper.getEmployeeByPage(page, size, employee, beginDateScope)
        val total: Long = employeeMapper.getTotal(employee, beginDateScope)
        val bean = RespPageBean()
        bean.data = data
        bean.total = total
        return bean
    }

    fun addEmp(employee: Employee): Int {
        val beginContract: Date = employee.beginContract
        val endContract: Date = employee.endContract
        val month = (yearFormat.format(endContract).toDouble() - yearFormat.format(beginContract)
            .toDouble()) * 12 + (monthFormat.format(endContract).toDouble() - monthFormat.format(beginContract)
            .toDouble())
        employee.contractTerm = decimalFormat.format(month / 12).toDouble()
        val result: Int = employeeMapper.insertSelective(employee)
        if (result == 1) {
            val emp: Employee = employeeMapper.getEmployeeById(employee.id)
            //生成消息的唯一id
            val msgId = UUID.randomUUID().toString()
            val mailSendLog = MailSendLog()
            mailSendLog.setMsgId(msgId)
            mailSendLog.setCreateTime(Date())
            mailSendLog.setExchange(MailConstants.MAIL_EXCHANGE_NAME)
            mailSendLog.setRouteKey(MailConstants.MAIL_ROUTING_KEY_NAME)
            mailSendLog.setEmpId(emp.id)
            mailSendLog.setTryTime(Date(System.currentTimeMillis() + 1000 * 60 * MailConstants.MSG_TIMEOUT))
            mailSendLogService!!.insert(mailSendLog)
            rabbitTemplate.convertAndSend(
                MailConstants.MAIL_EXCHANGE_NAME,
                MailConstants.MAIL_ROUTING_KEY_NAME,
                emp,
                CorrelationData(msgId)
            )
        }
        return result
    }

    fun maxWorkID(): Int {
        return employeeMapper.maxWorkID()
    }

    fun deleteEmpByEid(id: Int?): Int {
        return employeeMapper.deleteByPrimaryKey(id)
    }

    fun updateEmp(employee: Employee?): Int {
        return employeeMapper.updateByPrimaryKeySelective(employee)
    }

    fun addEmps(list: List<Employee?>?): Int {
        return employeeMapper.addEmps(list)
    }

    fun getEmployeeByPageWithSalary(page: Int?, size: Int?): RespPageBean {
        var page = page
        if (page != null && size != null) {
            page = (page - 1) * size
        }
        val list: List<Employee> = employeeMapper.getEmployeeByPageWithSalary(page, size)
        val respPageBean = RespPageBean()
        respPageBean.data = list
        respPageBean.total = employeeMapper.getTotal(null, null)
        return respPageBean
    }

    fun updateEmployeeSalaryById(eid: Int?, sid: Int?): Int {
        return employeeMapper.updateEmployeeSalaryById(eid, sid)
    }

    fun getEmployeeById(empId: Int?): Employee {
        return employeeMapper.getEmployeeById(empId)
    }

    companion object {
        val logger = LoggerFactory.getLogger(EmployeeService::class.java)
    }
}