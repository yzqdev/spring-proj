package cn.hellohao.config;



import java.io.File;
import java.util.Properties;


/**

 * @author Hellohao

 * @version 1.0

 * @date 29/11/2021 上午 10:35

 */

public class GlobalConstant {

    public static    String SYSTYPE = "LINUX";
static Properties props=System.getProperties();
    public static   String LOCPATH =props.getProperty("user.home")+ File.separator + "HellohaoData";

}