package org.sang.utils

import org.sang.bean.User
import org.springframework.security.core.context.SecurityContextHolder

/**
 * Created by sang on 2017/12/20.
 */
object Util {
    val currentUser: User
        get() = SecurityContextHolder.getContext().authentication.principal as User
}