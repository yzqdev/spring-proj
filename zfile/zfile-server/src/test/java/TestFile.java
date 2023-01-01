import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author yanni
 * @date time 2022/3/29 1:05
 * @modified By:
 */
public class TestFile {
    public static void main(String[] args){
        Path path = Paths.get("E:\\downloads\\a.mp4");
        String contentType = null;
        try {
            contentType = Files.probeContentType(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("File content type is : " + contentType);
    }
    @Test
    public void decodeUrl() throws UnsupportedEncodingException {
        String url="D:\\Video\\films\\%E4%B8%8B%E8%BD%BD%20(19).mp4";
        String dec=URLDecoder.decode(url,"utf-8");
        System.out.println(dec);
    }
}
