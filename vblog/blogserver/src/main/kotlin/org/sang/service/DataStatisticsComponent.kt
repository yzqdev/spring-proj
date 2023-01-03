package org.sang.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * Created by sang on 2017/12/25.
 */
@Component
class DataStatisticsComponent {
    @Autowired
    var articleService: ArticleService? = null

    //每天执行一次，统计PV
    @Scheduled(cron = "1 0 0 * * ?")
    fun pvStatisticsPerDay() {
        articleService!!.pvStatisticsPerDay()
    }
}