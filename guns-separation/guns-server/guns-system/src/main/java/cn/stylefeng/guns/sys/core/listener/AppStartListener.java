package cn.stylefeng.guns.sys.core.listener;

import cn.hutool.core.lang.Console;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author yanni
 * @date time 2021/10/28 9:54
 * @modified By:
 */

public class AppStartListener implements ApplicationListener<ApplicationEvent> {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {

        Console.print(" AppStartListener start\n");
    }
}
