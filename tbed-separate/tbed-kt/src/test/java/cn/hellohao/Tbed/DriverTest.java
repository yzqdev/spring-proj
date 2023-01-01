package cn.hellohao.Tbed;

import org.junit.Test;

/**
 * @author yanni
 * @date time 2022/5/16 0:36
 * @modified By:
 */
public class DriverTest {

    @Test
    public void getPg(){
        String DBDRIVER="org.postgresql.Driver";
        try {
            Class.forName(DBDRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
