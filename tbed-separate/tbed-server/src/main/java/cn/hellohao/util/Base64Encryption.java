package cn.hellohao.util;


import java.util.Base64;

/**
 * Created by Hellohao on 2019-08-27.
 */
public class Base64Encryption {
    public static void toBaseCode(String str) {
        String data = encryptBASE64(str.getBytes());
    }

    public static String decryptBASE64(String key) {
        byte[] b = null;
        b = Base64.getDecoder().decode( key);
        return new String(b);
    }

    public static String encryptBASE64(byte[] key) {
        String string = "SGVsbG9oYW8K";
        return Base64.getEncoder().encodeToString(key).replaceAll("\r|\n", "");

    }
}