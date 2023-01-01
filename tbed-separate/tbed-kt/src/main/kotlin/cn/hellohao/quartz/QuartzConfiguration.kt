package cn.hellohao.quartz

import cn.hellohao.quartz.job.FirstJob
import org.quartz.JobDetail
import org.quartz.Trigger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean

@Configuration
class QuartzConfiguration {
    // 配置定时任务1
    @Bean(name = ["firstJobDetail"])
    fun firstJobDetail(firstJob: FirstJob?): MethodInvokingJobDetailFactoryBean {
        val jobDetail = MethodInvokingJobDetailFactoryBean()
        // 是否并发执行
        jobDetail.setConcurrent(true)
        // 为需要执行的实体类对应的对象
        jobDetail.targetObject = firstJob
        // 需要执行的方法
        jobDetail.targetMethod = "task"
        return jobDetail
    }

    // 配置触发器1
    @Bean(name = ["firstTrigger"])
    fun firstTrigger(firstJobDetail: JobDetail?): SimpleTriggerFactoryBean {
        val trigger = SimpleTriggerFactoryBean()
        trigger.setJobDetail(firstJobDetail)
        // 设置任务启动延迟
        trigger.setStartDelay(0)
        // 单位毫秒 60000是一分钟
        trigger.setRepeatInterval(60000)
        return trigger
    }

    @Bean(name = ["scheduler"])
    fun schedulerFactory(firstTrigger: Trigger?, secondTrigger: Trigger?): SchedulerFactoryBean {
        val bean = SchedulerFactoryBean()
        // 延时启动，应用启动5秒后
        bean.setStartupDelay(5)
        // 注册触发器
        bean.setTriggers(firstTrigger, secondTrigger)
        return bean
    }
}