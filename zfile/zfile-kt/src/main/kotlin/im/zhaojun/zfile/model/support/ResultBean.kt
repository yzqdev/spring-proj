package im.zhaojun.zfile.model.support

import java.io.Serializable

/**
 * @author zhaojun
 */
class ResultBean : Serializable {
    var msg = "操作成功"
    var code = SUCCESS
    var data: Any? = null

    private constructor() : super() {}
    private constructor(msg: String, data: Any?, code: Int) {
        this.msg = msg
        this.data = data
        this.code = code
    }

    companion object {
        private const val serialVersionUID = -8276264968757808344L
        const val SUCCESS = 0
        const val FAIL = -1
        const val REQUIRED_PASSWORD = -2
        const val INVALID_PASSWORD = -3
        @kotlin.jvm.JvmStatic
        fun successData(data: Any?): ResultBean {
            return success("操作成功", data)
        }

        fun successPage(data: Any?, total: Long?): ResultBean {
            return success("操作成功", data)
        }

        fun success(data: Any?): ResultBean {
            return success("操作成功", data)
        }

        @JvmOverloads
        fun success(msg: String = "操作成功", data: Any? = null): ResultBean {
            return ResultBean(msg, data, SUCCESS)
        }

        fun error(msg: String): ResultBean {
            val resultBean = ResultBean()
            resultBean.code = FAIL
            resultBean.msg = msg
            return resultBean
        }

        @kotlin.jvm.JvmStatic
        fun error(msg: String, code: Int): ResultBean {
            val resultBean = ResultBean()
            resultBean.code = code
            resultBean.msg = msg
            return resultBean
        }
    }
}