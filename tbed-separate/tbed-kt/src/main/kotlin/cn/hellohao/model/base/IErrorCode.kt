package cn.hellohao.model.base

/**
 * 封装API的错误码
 * Created by macro on 2019/4/19.
 */
interface IErrorCode {
    val code: Long
    val message: String
}