package cn.hellohao.service;


public interface WallpaperService {
     String ISURL ="687474703a2f2f77616c6c70617065722e6170632e3336302e636e2f696e6465782e706870";
    public String getWallpaper(Integer start, Integer count, Integer category)  ;

    public String getWallpaperCategory() ;
}
