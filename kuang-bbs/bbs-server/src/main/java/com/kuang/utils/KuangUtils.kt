package com.kuang.utils

import java.sql.Timestamp
import java.util.*

object KuangUtils {
    var printFlag = true
    @JvmStatic
    val uuid: String
        get() = UUID.randomUUID().toString().replace("-".toRegex(), "")
    @JvmStatic
    val time: Timestamp
        get() = Timestamp(System.currentTimeMillis())

    @JvmStatic
    fun print(msg: String) {
        if (printFlag) {
            println("kuangshen:=>$msg")
        }
    }
}