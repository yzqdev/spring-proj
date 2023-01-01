package im.zhaojun.zfile.exception

import cn.dev33.satoken.exception.NotLoginException
import im.zhaojun.zfile.model.support.ResultBean
import im.zhaojun.zfile.model.support.ResultBean.Companion.error
import org.apache.catalina.connector.ClientAbortException
import org.slf4j.LoggerFactory
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import java.net.ConnectException

/**
 * 全局异常处理器
 * @author zhaojun
 */
@ControllerAdvice
class GlobleExceptionHandler {
    /**
     * 不存在的文件异常
     */
    @ExceptionHandler(NotEnabledDriveException::class)
    @ResponseBody
    fun notEnabledDrive(): ResultBean {
        return error("驱动器已关闭")
    }

    /**
     * 不存在的文件异常
     */
    @ExceptionHandler(NotExistFileException::class)
    @ResponseBody
    fun notExistFile(): ResultBean {
        return error("文件不存在")
    }

    /**
     * 捕获 ClientAbortException 异常, 不做任何处理, 防止出现大量堆栈日志输出, 此异常不影响功能.
     */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException::class, ClientAbortException::class)
    @ResponseBody
    @ResponseStatus
    fun clientAbortException() {
        // if (log.isDebugEnabled()) {
        //     log.debug("出现了断开异常:", ex);
        // }
    }

    /**
     * 文件预览异常
     */
    @ExceptionHandler(PasswordVerifyException::class)
    @ResponseBody
    @ResponseStatus
    fun passwordVerifyException(ex: PasswordVerifyException): ResultBean {
        return error(ex.message!!)
    }

    /**
     * 无效的驱动器异常
     */
    @ExceptionHandler(InvalidDriveException::class)
    @ResponseBody
    @ResponseStatus
    fun invalidDriveException(ex: InvalidDriveException): ResultBean {
        return error(ex.message!!)
    }

    /**
     * 文件预览异常
     */
    @ExceptionHandler(PreviewException::class)
    @ResponseBody
    @ResponseStatus
    fun previewException(ex: PreviewException): ResultBean {
        return error(ex.message!!)
    }

    /**
     * 初始化异常
     */
    @ExceptionHandler(InitializeDriveException::class)
    @ResponseBody
    @ResponseStatus
    fun initializeException(ex: InitializeDriveException): ResultBean {
        return error(ex.message!!)
    }

    /**
     * 登录异常拦截器
     */
    @ExceptionHandler(NotLoginException::class)
    @ResponseBody
    @ResponseStatus
    fun handlerNotLoginException(e: NotLoginException?): ResultBean {
        return error("未登录")
    }

    /**
     * 登录异常拦截器
     */
    @ExceptionHandler(ConnectException::class)
    @ResponseBody
    fun handlerConnectException(e: ConnectException?): ResultBean {
        return error("请求失败, 清稍后再试")
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus
    fun extraExceptionHandler(e: Exception): ResultBean {
        log.error(e.message, e)
        return if (e.javaClass == Exception::class.java) {
            error("系统异常, 请联系管理员")
        } else {
            error(e.message!!)
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(GlobleExceptionHandler::class.java)
    }
}