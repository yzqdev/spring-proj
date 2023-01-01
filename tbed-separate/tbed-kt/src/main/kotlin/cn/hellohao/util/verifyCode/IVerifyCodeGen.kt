package cn.hellohao.util.verifyCode

import cn.hellohao.util.verifyCodeimport.VerifyCode
import java.io.IOException
import java.io.OutputStream

/**
 * 验证码生成接口
 */
interface IVerifyCodeGen {
    /**
     * 生成验证码并返回code，将图片写的os中
     *
     * @param width
     * @param height
     * @param os
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun generate(width: Int, height: Int, os: OutputStream?): String?

    /**
     * 生成验证码对象
     *
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun generate(width: Int, height: Int): VerifyCode?
}