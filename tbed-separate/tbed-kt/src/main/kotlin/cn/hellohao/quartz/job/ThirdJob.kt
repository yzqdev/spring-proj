package cn.hellohao.quartz.job

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//@Component
//@EnableScheduling
class ThirdJob {
    fun task() {
        println("任务3执行....当前时间为" + dateFormat().format(LocalDateTime.now()))
    }

    private fun dateFormat(): DateTimeFormatter {
        return DateTimeFormatter.ofPattern("HH:mm:ss")
    }
}