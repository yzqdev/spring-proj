package cn.hellohao.service.impl;

import cn.hellohao.service.WallpaperService;
import cn.hellohao.util.ToStringTools;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


@Service
public class WallpaperServiceImpl implements WallpaperService {
    private String ISURL ="687474703a2f2f77616c6c70617065722e6170632e3336302e636e2f696e6465782e706870";
    public String getWallpaper(Integer start, Integer count, Integer category) {
        StringBuffer buffer = new StringBuffer();
        HttpURLConnection httpConn = null;
        BufferedReader reader = null;
        String strURL = null;
        try {
            if (category < 1) {
                strURL = new StringBuilder().append(ToStringTools.decodeString(ISURL)).append("?c=WallPaper&a=getAppsByOrder&order=create_time&start=").append(start).append("&count=").append(count).append("&from=360chrome").toString();
            } else {
                strURL = ToStringTools.decodeString(ISURL)+"?c=WallPaper&a=getAppsByCategory&cid=" + category + "&start=" + start + "&count=" + count + "&from=360chrome";
            }

            URL url = new URL(strURL);
            System.out.println(strURL);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            //httpConn.setRequestProperty("Authorization", author);
            httpConn.connect();
            reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer.toString();
    }

    public String getWallpaperCategory() {
        StringBuffer CategoryJson = new StringBuffer();
        HttpURLConnection httpConn = null;
        BufferedReader reader = null;
        try {
            String strURL = ToStringTools.decodeString(ISURL)+"?c=WallPaper&a=getAllCategoriesV2";
            URL url = new URL(strURL);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                CategoryJson.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return CategoryJson.toString();
    }
}
