package org.javaboy.vhr.mapper

java.util.*
interface EmployeeMapper {
    fun deleteByPrimaryKey(id: Int?): Int
    fun insert(record: Employee?): Int
    fun insertSelective(record: Employee?): Int
    fun selectByPrimaryKey(id: Int?): Employee?
    fun updateByPrimaryKeySelective(record: Employee?): Int
    fun updateByPrimaryKey(record: Employee?): Int
    fun getEmployeeByPage(
        @Param("page") page: Int?,
        @Param("size") size: Int?,
        @Param("emp") employee: Employee?,
        @Param("beginDateScope") beginDateScope: Array<Date?>?
    ): List<Employee?>?

    fun getTotal(@Param("emp") employee: Employee?, @Param("beginDateScope") beginDateScope: Array<Date?>?): Long?
    fun maxWorkID(): Int?
    fun addEmps(@Param("list") list: List<Employee?>?): Int?
    fun getEmployeeById(id: Int?): Employee?
    fun getEmployeeByPageWithSalary(@Param("page") page: Int?, @Param("size") size: Int?): List<Employee?>?
    fun updateEmployeeSalaryById(@Param("eid") eid: Int?, @Param("sid") sid: Int?): Int?
}