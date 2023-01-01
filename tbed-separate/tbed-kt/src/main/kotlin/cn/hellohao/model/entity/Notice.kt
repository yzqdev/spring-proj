package cn.hellohao.model.entity

class Notice {
    var id: Int? = null
    private var text: String? = null

    constructor() : super() {}
    constructor(
        id: Int?, text: String?, password: String?, email: String?, birthder: String?, level: Int?,
        keyID: String?
    ) : super() {
        this.id = id
        this.text = text
    }

    fun gettext(): String? {
        return text
    }

    fun settext(text: String?) {
        this.text = text
    }
}