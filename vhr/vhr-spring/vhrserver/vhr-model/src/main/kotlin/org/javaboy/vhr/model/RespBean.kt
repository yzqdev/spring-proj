package org.javaboy.vhr.model

/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 * @时间 2019-09-20 8:39
 */
class RespBean {
    var status: Int? = null
        private set
    var msg: String? = null
        private set
    var obj: Any? = null
        private set

    private constructor() {}
    private constructor(status: Int, msg: String, obj: Any?) {
        this.status = status
        this.msg = msg
        this.obj = obj
    }

    fun setStatus(status: Int?): RespBean {
        this.status = status
        return this
    }

    fun setMsg(msg: String?): RespBean {
        this.msg = msg
        return this
    }

    fun setObj(obj: Any?): RespBean {
        this.obj = obj
        return this
    }

    companion object {
        @kotlin.jvm.JvmStatic
        fun build(): RespBean {
            return RespBean()
        }

        fun ok(msg: String): RespBean {
            return RespBean(200, msg, null)
        }

        @kotlin.jvm.JvmStatic
        fun ok(msg: String, obj: Any?): RespBean {
            return RespBean(200, msg, obj)
        }

        fun error(msg: String): RespBean {
            return RespBean(500, msg, null)
        }

        fun error(msg: String, obj: Any?): RespBean {
            return RespBean(500, msg, obj)
        }
    }
}