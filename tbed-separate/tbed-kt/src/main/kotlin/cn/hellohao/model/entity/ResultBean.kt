package cn.hellohao.model.entity

/**
 * 结果
 *
 * @author yanni
 * @date 2021/11/17
 */
class ResultBean private constructor() {
    var code = 0
    private var msg: String? = null
    var data: Any? = null
    fun getmsg(): String? {
        return msg
    }

    fun setmsg(msg: String?) {
        this.msg = msg
    }

    companion object {
        fun error(code: Int, msg: String?): ResultBean {
            val resultBean = ResultBean()
            resultBean.code = code
            resultBean.setmsg(msg)
            return resultBean
        }

        fun success(): ResultBean {
            val resultBean = ResultBean()
            resultBean.code = 0
            resultBean.setmsg("success")
            return resultBean
        }

        fun success(data: Any?): ResultBean {
            val resultBean = ResultBean()
            resultBean.code = 200
            resultBean.setmsg("success")
            resultBean.data = data
            return resultBean
        }
    }
}