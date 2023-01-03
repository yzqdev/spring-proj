package com.kuang

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@MapperScan("com.kuang.mapper")
class StudyApplication
    fun main(args: Array<String>) {
       runApplication<StudyApplication>(*args)
    }
