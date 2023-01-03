package org.javaboy.vhr.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*

class Salary {
    var id: Int? = null
    var basicSalary: Int? = null
    var bonus: Int? = null
    var lunchSalary: Int? = null
    var trafficSalary: Int? = null
    var allSalary: Int? = null
    var pensionBase: Int? = null
    var pensionPer: Float? = null

    @JsonFormat(pattern = "yyyy-MM-dd")
    var createDate: Date? = null
    var medicalBase: Int? = null
    var medicalPer: Float? = null
    var accumulationFundBase: Int? = null
    var accumulationFundPer: Float? = null
    var name: String? = null
}