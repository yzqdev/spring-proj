package cn.hellohao.util.verifyCode

import cn.hellohao.util.verifyCodeimport.VerifyCode
import org.slf4j.LoggerFactory
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import javax.imageio.ImageIO


/**
 * 验证码实现类
 */
class SimpleCharVerifyCodeGenImpl : IVerifyCodeGen {
    /**
     * 生成随机字符
     *
     * @param width
     * @param height
     * @param os
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    override fun generate(width: Int, height: Int, os: OutputStream?): String? {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val graphics = image.graphics
        fillBackground(graphics, width, height)
        val randomStr = RandomUtils.randomString(VALICATE_CODE_LENGTH)
        createCharacter(graphics, randomStr)
        graphics.dispose()
        //设置JPEG格式
        ImageIO.write(image, "JPEG", os)
        return randomStr
    }

    /**
     * 验证码生成
     *
     * @param width
     * @param height
     * @return
     */
    override fun generate(width: Int, height: Int): VerifyCode? {
        var verifyCode: VerifyCode? = null
        try {
            ByteArrayOutputStream().use { baos ->
                val code = generate(width, height, baos)
                verifyCode = VerifyCode()
                verifyCode!!.code = code
                verifyCode!!.imgBytes = baos.toByteArray()
            }
        } catch (e: IOException) {
            logger.error(e.message, e)
            verifyCode = null
        }
        return verifyCode
    }

    /**
     * 设置字符颜色大小
     *
     * @param g
     * @param randomStr
     */
    private fun createCharacter(g: Graphics, randomStr: String?) {
        val charArray = randomStr!!.toCharArray()
        for (i in charArray.indices) {
//设置RGB颜色算法参数
            g.color = Color(
                50 + RandomUtils.nextInt(100),
                50 + RandomUtils.nextInt(100),
                50 + RandomUtils.nextInt(100)
            )
            //设置字体大小，类型
            g.font = Font(
                FONT_TYPES[RandomUtils.nextInt(
                    FONT_TYPES.size
                )], Font.BOLD, 26
            )
            //设置x y 坐标
            g.drawString(charArray[i].toString(), 15 * i + 5, 19 + RandomUtils.nextInt(8))
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SimpleCharVerifyCodeGenImpl::class.java)
        private val FONT_TYPES =
            arrayOf("\u5b8b\u4f53", "\u65b0\u5b8b\u4f53", "\u9ed1\u4f53", "\u6977\u4f53", "\u96b6\u4e66")
        private const val VALICATE_CODE_LENGTH = 4

        /**
         * 设置背景颜色及大小，干扰线
         *
         * @param graphics
         * @param width
         * @param height
         */
        private fun fillBackground(graphics: Graphics, width: Int, height: Int) {
// 填充背景
            graphics.color = Color.WHITE
            //设置矩形坐标x y 为0
            graphics.fillRect(0, 0, width, height)

// 加入干扰线条
            for (i in 0..7) {
//设置随机颜色算法参数
                graphics.color = RandomUtils.randomColor(40, 150)
                val random = Random()
                val x = random.nextInt(width)
                val y = random.nextInt(height)
                val x1 = random.nextInt(width)
                val y1 = random.nextInt(height)
                graphics.drawLine(x, y, x1, y1)
            }
        }
    }
}