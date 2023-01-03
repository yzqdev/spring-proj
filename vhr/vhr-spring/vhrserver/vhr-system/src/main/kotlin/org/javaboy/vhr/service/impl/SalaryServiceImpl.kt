package org.javaboy.vhr.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author yanni
 * @date time 2022/6/7 15:31
 * @modified By:
 */
@Service
@RequiredArgsConstructor
class SalaryServiceImpl : ServiceImpl<SalaryMapper?, Salary?>(), SalaryService {
    private val salaryMapper: SalaryMapper? = null
    val allSalaries: List<Any?>
        get() = salaryMapper.getAllSalaries()

    override fun addSalary(salary: Salary): Int {
        salary.createDate = Date()
        return salaryMapper.insertSelective(salary)
    }

    override fun deleteSalaryById(id: Int?): Int {
        return salaryMapper.deleteByPrimaryKey(id)
    }

    override fun updateSalaryById(salary: Salary?): Int {
        return salaryMapper.updateByPrimaryKeySelective(salary)
    }
}