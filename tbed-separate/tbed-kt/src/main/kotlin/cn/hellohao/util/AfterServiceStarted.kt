package cn.hellohao.util

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class AfterServiceStarted : ApplicationRunner {
    @Throws(Exception::class)
    override fun run(args: ApplicationArguments) {
        Print.Normal("______________________________________________")
    }
}