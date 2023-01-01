package cn.hellohao.service


interface MobilePaperService {
    fun GetMobilepaper(start: Int, count: Int, category: String?): String
    fun GetMobilepaperCategory(): String

    companion object {
        const val ISURL = "687474703a2f2f736572766963652e7069636173736f2e616465736b2e636f6d2f76312f766572746963616c"
    }
}