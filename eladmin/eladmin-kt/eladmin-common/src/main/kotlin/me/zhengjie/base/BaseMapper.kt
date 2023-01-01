/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.base

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
interface BaseMapper<D, E> {
    /**
     * DTO转Entity
     * @param dto /
     * @return /
     */
    fun toEntity(dto: D): E

    /**
     * Entity转DTO
     * @param entity /
     * @return /
     */
    fun toDto(entity: E): D

    /**
     * DTO集合转Entity集合
     * @param dtoList /
     * @return /
     */
    fun toEntity(dtoList: List<D>?): List<E>?

    /**
     * Entity集合转DTO集合
     * @param entityList /
     * @return /
     */
    fun toDto(entityList: List<E>?): List<D>?
}