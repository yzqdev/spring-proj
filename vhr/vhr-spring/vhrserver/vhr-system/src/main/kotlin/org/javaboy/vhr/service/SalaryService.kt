package org.javaboy.vhr.service

import com.baomidou.mybatisplus.extension.service.IService

/**
 * 工资服务
 *
 * @author yanni
 * @date 2022/06/07
 */
interface SalaryService : IService<Salary?> {
    val allSalaries: List<Any?>
    fun addSalary(salary: Salary): Int
    fun deleteSalaryById(id: Int?): Int
    fun updateSalaryById(salary: Salary?): Int
}