package im.zhaojun.zfile.util

import org.slf4j.LoggerFactory
import org.springframework.beans.BeansException
import org.springframework.beans.factory.DisposableBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.lang.NonNull
import org.springframework.stereotype.Service

/**
 * @author zhaojun
 */
@Service
class SpringContextHolder : ApplicationContextAware, DisposableBean {
    /**
     * 实现 DisposableBean 接口, 在 Context 关闭时清理静态变量.
     */
    override fun destroy() {
        clearHolder()
    }

    @Throws(BeansException::class)
    override fun setApplicationContext(@NonNull applicationContext: ApplicationContext) {
        Companion.applicationContext = applicationContext
    }

    companion object {
        /**
         * 取得存储在静态变量中的 ApplicationContext.
         */
        var applicationContext: ApplicationContext? = null
            private set
        private val logger = LoggerFactory.getLogger(SpringContextHolder::class.java)

        /**
         * 从静态变量 applicationContext 中取得 Bean, 自动转型为所赋值对象的类型.
         */
        fun <T> getBean(name: String?): T {
            return applicationContext!!.getBean(name) as T
        }

        /**
         * 从静态变量 applicationContext 中取得 Bean, 自动转型为所赋值对象的类型.
         */
        @JvmStatic
        fun <T> getBean(requiredType: Class<T>?): T {
            return applicationContext!!.getBean(requiredType)
        }

        /**
         * 清除 SpringContextHolder 中的 ApplicationContext 为 Null.
         */
        fun clearHolder() {
            if (logger.isDebugEnabled) {
                logger.debug("清除SpringContextHolder中的ApplicationContext:" + applicationContext)
            }
            logger.info("清除SpringContextHolder中的ApplicationContext:" + applicationContext)
            applicationContext = null
        }

        @JvmStatic
        fun <T> getBeansOfType(classz: Class<T>): Map<String, T> {
            return applicationContext!!.getBeansOfType(classz)
        }
    }
}