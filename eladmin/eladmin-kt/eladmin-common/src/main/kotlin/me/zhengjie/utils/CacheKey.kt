/*
 * Copyright 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.zhengjie.utils

/**
 * @author: liaojinlong
 * @date: 2020/6/11 15:49
 * @apiNote: 关于缓存的Key集合
 */
interface CacheKey {
    companion object {
        /**
         * 用户
         */
        const val USER_ID = "user::id:"

        /**
         * 数据
         */
        const val DATA_USER = "data::user:"

        /**
         * 菜单
         */
        const val MENU_ID = "menu::id:"
        const val MENU_USER = "menu::user:"

        /**
         * 角色授权
         */
        const val ROLE_AUTH = "role::auth:"

        /**
         * 角色信息
         */
        const val ROLE_ID = "role::id:"

        /**
         * 部门
         */
        const val DEPT_ID = "dept::id:"

        /**
         * 岗位
         */
        const val JOB_ID = "job::id:"

        /**
         * 数据字典
         */
        const val DICT_NAME = "dict::name:"
    }
}