package me.zhengjie.utils

import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO
import javax.servlet.http.HttpServletResponse

/**
 * 登录验证码工具类
 */
object RandImageUtil {
    const val key = "ELADMIN_LOGIN_KEY"

    /**
     * 定义图形大小
     */
    private const val width = 105

    /**
     * 定义图形大小
     */
    private const val height = 35

    /**
     * 定义干扰线数量
     */
    private const val count = 200

    /**
     * 干扰线的长度=1.414*lineWidth
     */
    private const val lineWidth = 2

    /**
     * 图片格式
     */
    private const val IMG_FORMAT = "JPEG"

    /**
     * base64 图片前缀
     */
    private const val BASE64_PRE = "data:image/jpg;base64,"

    /**
     * 直接通过response 返回图片
     * @param response
     * @param resultCode
     * @throws IOException
     */
    @Throws(IOException::class)
    fun generate(response: HttpServletResponse, resultCode: String) {
        val image = getImageBuffer(resultCode)
        // 输出图象到页面
        ImageIO.write(image, IMG_FORMAT, response.outputStream)
    }

    /**
     * 生成base64字符串
     * @param resultCode
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun generate(resultCode: String): String {
        val image = getImageBuffer(resultCode)
        val byteStream = ByteArrayOutputStream()
        //写入流中
        ImageIO.write(image, IMG_FORMAT, byteStream)
        //转换成字节
        val bytes = byteStream.toByteArray()
        //转换成base64串
        var base64 = Base64.getEncoder().encodeToString(bytes).trim { it <= ' ' }
        base64 = base64.replace("\n".toRegex(), "").replace("\r".toRegex(), "") //删除 \r\n

        //写到指定位置
        //ImageIO.write(bufferedImage, "png", new File(""));
        return BASE64_PRE + base64
    }

    private fun getImageBuffer(resultCode: String): BufferedImage {
        // 在内存中创建图象
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        // 获取图形上下文
        val graphics = image.graphics as Graphics2D
        // 设定背景颜色
        graphics.color = Color.WHITE // ---1
        graphics.fillRect(0, 0, width, height)
        // 设定边框颜色
//		graphics.setColor(getRandColor(100, 200)); // ---2
        graphics.drawRect(0, 0, width - 1, height - 1)
        val random = Random()
        // 随机产生干扰线，使图象中的认证码不易被其它程序探测到
        for (i in 0 until count) {
            graphics.color = getRandColor(150, 200) // ---3
            val x = random.nextInt(width - lineWidth - 1) + 1 // 保证画在边框之内
            val y = random.nextInt(height - lineWidth - 1) + 1
            val xl = random.nextInt(lineWidth)
            val yl = random.nextInt(lineWidth)
            graphics.drawLine(x, y, x + xl, y + yl)
        }
        // 取随机产生的认证码
        for (i in 0 until resultCode.length) {
            // 将认证码显示到图象中,调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
            // graphics.setColor(new Color(20 + random.nextInt(130), 20 + random
            // .nextInt(130), 20 + random.nextInt(130)));
            // 设置字体颜色
            graphics.color = Color.BLACK
            // 设置字体样式
//			graphics.setFont(new Font("Arial Black", Font.ITALIC, 18));
            graphics.font = Font("Times New Roman", Font.BOLD, 24)
            // 设置字符，字符间距，上边距
            graphics.drawString(resultCode[i].toString(), 23 * i + 8, 26)
        }
        // 图象生效
        graphics.dispose()
        return image
    }

    private fun getRandColor(fc: Int, bc: Int): Color { // 取得给定范围随机颜色
        var fc = fc
        var bc = bc
        val random = Random()
        if (fc > 255) {
            fc = 255
        }
        if (bc > 255) {
            bc = 255
        }
        val r = fc + random.nextInt(bc - fc)
        val g = fc + random.nextInt(bc - fc)
        val b = fc + random.nextInt(bc - fc)
        return Color(r, g, b)
    }
}