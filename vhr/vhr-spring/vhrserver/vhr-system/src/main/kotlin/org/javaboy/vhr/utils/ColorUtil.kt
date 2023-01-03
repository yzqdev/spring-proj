package org.javaboy.vhr.utils

enum class ColorEnum(var code: Int) {
    /**
     * 黑色
     */
    BLACK(30),

    /**
     * 红色
     */
    RED(31),

    /**
     * 绿色
     */
    GREEN(32),

    /**
     * 黄色
     */
    YELLOW(33),

    /**
     * 蓝色
     */
    BLUE(34),

    /**
     * 粉红色
     */
    PINK(35),

    /**
     * 青色
     */
    CYAN(36), WHITE(37), bgBLACK(40),

    /**
     * 红色
     */
    bgRED(41),

    /**
     * 绿色
     */
    bgGREEN(42),

    /**
     * 黄色
     */
    bgYELLOW(44),

    /**
     * 蓝色
     */
    bgBLUE(44),

    /**
     * 粉红色
     */
    bgPINK(45),

    /**
     * 青色
     */
    bgCYAN(46), bgWHITE(47), lightRed(91);
}

object ColorUtil {
    const val RESET = "\u001b[0m"
    fun print(color: ColorEnum, str: String?) {
//\033[4;36m  cyan underline
        System.out.printf("\u001b[%s;%sm%s%s%n", "2", color.code, str, RESET)
    }

    fun printUnderline(color: ColorEnum, str: String?) {
        System.out.printf("\u001b[%s;%sm%s%s%n", "4", color.code, str, RESET)
    }

    fun printBg(foregroundColor: ColorEnum, bgColor: ColorEnum, str: String?) {
        System.out.printf("\u001b[%s;%s;%sm%s%s%n", "4", foregroundColor.code, bgColor.code, str, RESET)
    }

    /**
     * 红色
     *
     * @param str  str
     * @param args 是否下划线
     */
    fun red(str: String?, vararg args: String?) {
        if (args.size > 0) {
            printUnderline(ColorEnum.RED, str)
        } else {
            print(ColorEnum.RED, str)
        }
    }

    fun green(str: String?, vararg args: String?) {
        if (args.size > 0) {
            printUnderline(ColorEnum.GREEN, str)
        } else {
            print(ColorEnum.GREEN, str)
        }
    }

    fun yellow(str: String?, vararg args: String?) {
        if (args.size > 0) {
            printUnderline(ColorEnum.YELLOW, str)
        } else {
            print(ColorEnum.YELLOW, str)
        }
    }

    fun blue(str: String?, vararg args: String?) {
        if (args.size > 0) {
            printUnderline(ColorEnum.BLUE, str)
        } else {
            print(ColorEnum.BLUE, str)
        }
    }

    fun cyan(str: String?) {
        print(ColorEnum.CYAN, str)
    }

    fun cyan(str: String?, vararg args: String?) {
        if (args.size > 0) {
            printUnderline(ColorEnum.CYAN, str)
        }
    }

    fun pink(str: String?, vararg args: String?) {
        if (args.size > 0) {
            printUnderline(ColorEnum.PINK, str)
        } else {
            print(ColorEnum.PINK, str)
        }
    }

    fun black(str: String?, vararg args: String?) {
        if (args.size > 0) {
            printUnderline(ColorEnum.BLACK, str)
        } else {
            print(ColorEnum.BLACK, str)
        }
    }

    fun bgGreen(str: String?) {
        printBg(ColorEnum.RED, ColorEnum.bgGREEN, str)
    }

    fun bgRed(str: String?) {
        printBg(ColorEnum.WHITE, ColorEnum.bgRED, str)
    }

    fun bgYellow(str: String?) {
        printBg(ColorEnum.WHITE, ColorEnum.bgYELLOW, str)
    }

    fun bgBlue(str: String?) {
        print(ColorEnum.bgBLUE, str)
    }

    fun bgPink(str: String?) {
        printBg(ColorEnum.WHITE, ColorEnum.bgPINK, str)
    }

    fun bgCyan(str: String?) {
        printBg(ColorEnum.WHITE, ColorEnum.CYAN, str)
    }

    fun bgWhite(str: String?) {
        printBg(ColorEnum.WHITE, ColorEnum.bgWHITE, str)
    }

    fun lightWhite(str: String?) {
        printBg(ColorEnum.WHITE, ColorEnum.lightRed, str)
    }
}