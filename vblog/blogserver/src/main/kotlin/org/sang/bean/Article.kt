package org.sang.bean

import java.sql.Timestamp

/**
 * Created by sang on 2017/12/20.
 */
class Article {
    var id: Long? = null
    var title: String? = null
    var mdContent: String? = null
    var htmlContent: String? = null
    var summary: String? = null
    var cid: Long? = null
    var uid: Long? = null
    var publishDate: Timestamp? = null
    var state: Int? = null
    var pageView: Int? = null
    var editTime: Timestamp? = null
    var dynamicTags: Array<String>
    var nickname: String? = null
    var cateName: String? = null
    var tags: List<Tags>? = null
    var stateStr: String? = null
}