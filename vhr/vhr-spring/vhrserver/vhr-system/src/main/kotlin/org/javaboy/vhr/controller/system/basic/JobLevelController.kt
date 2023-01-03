package org.javaboy.vhr.controller.system.basic

import org.springframework.web.bind.annotation.RequestBody

/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 * @时间 2019-10-01 17:59
 */
@RestController
@RequestMapping("/system/basic/joblevel")
class JobLevelController {
    @Autowired
    var jobLevelService: JobLevelService? = null

    @get:GetMapping("/")
    val allJobLevels: List<Any>
        get() = jobLevelService.getAllJobLevels()

    @PostMapping("/")
    fun addJobLevel(@RequestBody jobLevel: JobLevel?): RespBean {
        return if (jobLevelService.addJobLevel(jobLevel) == 1) {
            RespBean.ok("添加成功!")
        } else RespBean.error("添加失败!")
    }

    @PutMapping("/")
    fun updateJobLevelById(@RequestBody jobLevel: JobLevel?): RespBean {
        return if (jobLevelService.updateJobLevelById(jobLevel) == 1) {
            RespBean.ok("更新成功!")
        } else RespBean.error("更新失败!")
    }

    @DeleteMapping("/{id}")
    fun deleteJobLevelById(@PathVariable id: Int?): RespBean {
        return if (jobLevelService.deleteJobLevelById(id) == 1) {
            RespBean.ok("删除成功!")
        } else RespBean.error("删除失败!")
    }

    @DeleteMapping("/")
    fun deleteJobLevelsByIds(ids: Array<Int?>): RespBean {
        return if (jobLevelService.deleteJobLevelsByIds(ids) == ids.size) {
            RespBean.ok("删除成功!")
        } else RespBean.error("删除失败!")
    }
}