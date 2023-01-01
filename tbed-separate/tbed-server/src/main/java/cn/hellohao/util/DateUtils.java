package cn.hellohao.util;

import cn.hutool.core.lang.Console;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/9/20 16:27
 */
public class DateUtils {
    //计算时间
    public static LocalDateTime plusDay(int setday){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime today=LocalDateTime.now();
        Console.log("现在的日期是：" + today);

        LocalDateTime enddate =  today.plusDays(setday) ;
        Console.log("到期的日期：" + enddate.format(format));
        return enddate;
    }
}
