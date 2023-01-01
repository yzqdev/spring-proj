package cn.hellohao.util;

import cn.hellohao.config.GlobalConstant;
import cn.hellohao.model.entity.StorageKey;
import cn.hellohao.model.entity.ReturnImage;
import cn.hellohao.service.impl.KeysServiceImpl;

import java.io.*;
import java.util.Map;

public class LocUpdateImg {
    public static boolean deleteLOCImg(String imagename){
        boolean isDele = false;
        try {
            String filePath = GlobalConstant.LOCPATH + File.separator+imagename;
            File file = new File(filePath);
            isDele = file.delete();
        }catch (Exception e){
            e.printStackTrace();
            isDele = false;
        }
        return isDele;
    }

    public static ReturnImage ImageuploadLOC(Map<String, File> fileMap, String username,String keyID) throws Exception {
        KeysServiceImpl keysService = SpringContextHolder.getBean(KeysServiceImpl.class);
        final StorageKey key = keysService.selectKeys(keyID);
        ReturnImage returnImage = new ReturnImage();
        String filePath = GlobalConstant.LOCPATH + File.separator;
        File file = null;
        for (Map.Entry<String, File> entry : fileMap.entrySet()) {
            String ShortUID = SetText.getShortUuid();
            System.out.println("待上传的图片："+username + File.separator + ShortUID + "." + entry.getKey());
            File dest = new File(filePath + username + File.separator+ ShortUID + "." + entry.getKey());
            File temppath = new File(filePath + username+ File.separator );
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                InputStream fileInputStream = new FileInputStream(entry.getValue());
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest));
                byte[] bs = new byte[1024];
                int len;
                while ((len = fileInputStream.read(bs)) != -1) {
                    bos.write(bs, 0, len);
                }
                bos.flush();
                bos.close();
                returnImage.setImgName(username + "/" +ShortUID + "." + entry.getKey());//entry.getValue().getOriginalFilename()
                returnImage.setImgUrl(key.getRequestAddress() +"/ota/"+username + "/" + ShortUID + "." + entry.getKey());
                returnImage.setImgSize(entry.getValue().length());
                returnImage.setCode("200");

            } catch (IOException e) {
                e.printStackTrace();
                returnImage.setCode("500");
                System.err.println("上传失败");
            }
        }
        return returnImage;
    }


}
