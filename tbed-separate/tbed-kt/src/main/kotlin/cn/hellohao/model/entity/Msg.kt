package cn.hellohao.model.entity

import java.io.Serial
import java.io.Serializable

/**
 * 交互消息,默认成功
 *
 * @author Hellohao
 */
class Msg : Serializable {
    //返回码
    var code = "200"

    //提示信息
    var info = "操作成功"

    //返回数据
    var data: Any? = null

    //异常
    var exceptions: String? = null

    constructor() {}

    operator fun set(code: String, info: String) {
        this.code = code
        this.info = info
    }

    /**
     * 默认成功操作
     *
     * @param data
     */
    constructor(data: Any?) : super() {
        this.data = data
    }

    constructor(code: String, info: String) : super() {
        this.code = code
        this.info = info
    }

    constructor(code: String, info: String, data: Any?) : super() {
        this.code = code
        this.info = info
        this.data = data
    }

    constructor(code: String, info: String, data: Any?, exceptions: String?) : super() {
        this.code = code
        this.info = info
        this.data = data
        this.exceptions = exceptions
    }

    override fun toString(): String {
        return ("Msg [code=" + code + ", info=" + info + ", data=" + data
                + ", exceptions=" + exceptions + "]")
    }

    companion object {
        @Serial
        var  serialVersionUID = 5196249482551119279L
    }
}