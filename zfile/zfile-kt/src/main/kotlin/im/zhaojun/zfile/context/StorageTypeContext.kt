package im.zhaojun.zfile.context

import im.zhaojun.zfile.model.enums.StorageTypeEnum
import im.zhaojun.zfile.service.base.AbstractBaseFileService
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

/**
 * 存储类型工厂类
 * @author zhaojun
 */
@Component
class StorageTypeContext : ApplicationContextAware {
    /**
     * 项目启动时执行
     */
    @Throws(BeansException::class)
    override fun setApplicationContext(act: ApplicationContext) {
        applicationContext = act

        // 获取 Spring 容器中所有 FileService 类型的类
        storageTypeEnumFileServiceMap = act.getBeansOfType(
            AbstractBaseFileService::class.java
        )
    }

    companion object {
        private var storageTypeEnumFileServiceMap: Map<String, AbstractBaseFileService>? = null
        var applicationContext: ApplicationContext? = null
            private set

        /**
         * 获取指定存储类型 Service
         */
        fun getStorageTypeService(type: StorageTypeEnum): AbstractBaseFileService? {
            var result: AbstractBaseFileService? = null
            for (fileService in storageTypeEnumFileServiceMap!!.values) {
                if (fileService.storageTypeEnum === type) {
                    result = fileService
                    break
                }
            }
            return result
        }
    }
}