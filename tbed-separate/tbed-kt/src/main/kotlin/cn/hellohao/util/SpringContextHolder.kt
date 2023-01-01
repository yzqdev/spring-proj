package cn.hellohao.util

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

/**
 * Spring的ApplicationContext的持有者,可以用静态方法的方式获取spring容器中的bean
 * @author Hellohao
 * @date 2018年5月27日 下午6:32:11
 */
@Component
class SpringContextHolder : ApplicationContextAware {
    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        SpringContextHolder.Companion.applicationContext = applicationContext
    }

    companion object {
        val applicationContext: ApplicationContext?
            get() {
                SpringContextHolder.Companion.assertApplicationContext()
                return SpringContextHolder.Companion.applicationContext
            }

        fun <T> getBean(beanName: String?): T {
            SpringContextHolder.Companion.assertApplicationContext()
            return SpringContextHolder.Companion.applicationContext.getBean(beanName)
        }

        fun <T> getBean(requiredType: Class<T>?): T {
            SpringContextHolder.Companion.assertApplicationContext()
            return SpringContextHolder.Companion.applicationContext.getBean<T>(requiredType)
        }

        private fun assertApplicationContext() {
            if (SpringContextHolder.Companion.applicationContext == null) {
                throw RuntimeException("applicaitonContext属性为null,请检查是否注入了SpringContextHolder!")
            }
        }
    }
}