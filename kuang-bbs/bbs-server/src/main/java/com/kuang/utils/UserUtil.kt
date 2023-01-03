package com.kuang.utils

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.kuang.model.entity.User

object UserUtil {
    @JvmStatic
    fun getUserByUserCode(userCode: String?): User {
        val u = User()
        u.userId = userCode
        return u.selectOne(QueryWrapper(u))
    }

    fun getUserByName(name: String?): User {
        val u = User()
        u.username = name
        return u.selectOne(QueryWrapper(u))
    }
}