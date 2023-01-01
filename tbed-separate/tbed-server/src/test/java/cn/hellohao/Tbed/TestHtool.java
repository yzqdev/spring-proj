package cn.hellohao.Tbed;

import cn.hutool.system.SystemUtil;
import org.junit.Test;

import java.util.Properties;

/**
 * @author yanni
 * @date time 2022/5/4 0:02
 * @modified By:
 */
public class TestHtool {
    @Test
    public void getOs(){
        System.out.println(SystemUtil.getOsInfo());
        Properties props=System.getProperties();
        System.out.println(props.getProperty("user.home"));
    }
}
