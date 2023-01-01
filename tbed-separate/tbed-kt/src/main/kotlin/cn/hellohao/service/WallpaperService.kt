package cn.hellohao.service

interface WallpaperService {
    fun getWallpaper(start: Int, count: Int, category: Int): String
    fun wallpaperCategory(): String

    companion object {
        const val ISURL = "687474703a2f2f77616c6c70617065722e6170632e3336302e636e2f696e6465782e706870"
    }
}