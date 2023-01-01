/*
 * This file was generated by the Gradle 'init' task.
 *
 * This project uses @Incubating APIs which are subject to change.
 */

plugins {
    id("cn.stylefeng.java-conventions")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.alibaba:druid:1.2.8")
    implementation("com.baomidou:mybatis-plus-boot-starter:3.5.2")
    implementation("cn.hutool:hutool-all:5.8.2")
    compileOnly ("org.projectlombok:lombok:1.18.24")
    annotationProcessor ("org.projectlombok:lombok:1.18.24")
    implementation("com.alibaba:fastjson:1.2.78")
    implementation("cn.afterturn:easypoi-base:4.4.0")
}

description = "guns-core"
