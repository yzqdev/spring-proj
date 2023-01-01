package cn.hellohao.model.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import lombok.AllArgsConstructor
import lombok.Data

/**
 * 配置
 *
 * @author yanni
 * @date 2021/11/17
 */
@Data
@AllArgsConstructor
class Config {
    @TableId(type = IdType.ASSIGN_ID)
    var  id: String? = null
    var  sourcekey: Int? = null
    var  emails: Int? = null
    var  webname: String? = null
    var  explain: String? = null
    var  video: String? = null
    var  links: String? = null
    var  notice: String? = null
    var  baidu: String? = null
    var  backtype: String? = null
    var  domain: String? = null
    var  background1: String? = null
    var  background2: String? = null
    var  webms: String? = null
    var  webkeywords: String? = null
    var  webfavicons: String? = null
    var  websubtitle: String? = null
    var  logo: String? = null
    var  aboutInfo: String? = null
}