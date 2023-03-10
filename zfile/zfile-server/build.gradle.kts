/*
 * This file was generated by the Gradle 'init' task.
 *
 * This project uses @Incubating APIs which are subject to change.
 */

plugins {
    id("org.springframework.boot") version "2.7.3"
    id("io.spring.dependency-management") version "1.0.13.RELEASE"
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.spring") version "1.7.10"
}


dependencies {
    implementation("org.springdoc:springdoc-openapi-ui:1.6.11")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("cn.hutool:hutool-all:5.8.6")
    implementation("com.upyun:java-sdk:4.2.3")
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.221")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("commons-net:commons-net:3.8.0")
    implementation("com.mpatric:mp3agic:0.9.1")
    implementation("com.alibaba:fastjson:2.0.3")
    implementation("cn.dev33:sa-token-spring-boot-starter:1.30.0")
    runtimeOnly("org.postgresql:postgresql:42.3.6")
    runtimeOnly("com.h2database:h2:2.1.212")
    compileOnly("org.projectlombok:lombok:1.18.24")
	annotationProcessor ("org.projectlombok:lombok:1.18.24")
}

group = "im.zhaojun"
version = "3.2"
description = "zfile"
java.sourceCompatibility = JavaVersion.VERSION_17


tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
