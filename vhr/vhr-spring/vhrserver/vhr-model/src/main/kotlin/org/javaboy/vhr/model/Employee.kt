package org.javaboy.vhr.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.util.*

class Employee : Serializable {
    var id: Int? = null
    var name: String? = null
    var gender: String? = null
    override fun toString(): String {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday=" + birthday +
                ", idCard='" + idCard + '\'' +
                ", wedlock='" + wedlock + '\'' +
                ", nationId=" + nationId +
                ", nativePlace='" + nativePlace + '\'' +
                ", politicId=" + politicId +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", departmentId=" + departmentId +
                ", jobLevelId=" + jobLevelId +
                ", posId=" + posId +
                ", engageForm='" + engageForm + '\'' +
                ", tiptopDegree='" + tiptopDegree + '\'' +
                ", specialty='" + specialty + '\'' +
                ", school='" + school + '\'' +
                ", beginDate=" + beginDate +
                ", workState='" + workState + '\'' +
                ", workID='" + workID + '\'' +
                ", contractTerm=" + contractTerm +
                ", conversionTime=" + conversionTime +
                ", notWorkDate=" + notWorkDate +
                ", beginContract=" + beginContract +
                ", endContract=" + endContract +
                ", workAge=" + workAge +
                ", nation=" + nation +
                ", politicsstatus=" + politicsstatus +
                ", department=" + department +
                ", jobLevel=" + jobLevel +
                ", position=" + position +
                '}'
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    var birthday: Date? = null
    var idCard: String? = null
    var wedlock: String? = null
    var nationId: Int? = null
    var nativePlace: String? = null
    var politicId: Int? = null
    var email: String? = null
    var phone: String? = null
    var address: String? = null
    var departmentId: Int? = null
    var jobLevelId: Int? = null
    var posId: Int? = null
    var engageForm: String? = null
    var tiptopDegree: String? = null
    var specialty: String? = null
    var school: String? = null

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    var beginDate: Date? = null
    var workState: String? = null
    var workID: String? = null
    var contractTerm: Double? = null

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    var conversionTime: Date? = null

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    var notWorkDate: Date? = null

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    var beginContract: Date? = null

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    var endContract: Date? = null
    var workAge: Int? = null
    var nation: Nation? = null
    var politicsstatus: Politicsstatus? = null
    var department: Department? = null
    var jobLevel: JobLevel? = null
    var position: Position? = null
    var salary: Salary? = null
}