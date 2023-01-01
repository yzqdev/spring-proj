import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("org.springframework.boot") version "2.7.4"
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.spring") version "1.7.20"
}


dependencies {
    // https://mvnrepository.com/artifact/org.flywaydb/flyway-core
    implementation("org.flywaydb:flyway-core:9.10.2")
// https://mvnrepository.com/artifact/org.flywaydb/flyway-mysql
    implementation("org.flywaydb:flyway-mysql:9.10.2")

    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-freemarker")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    compileOnly ("org.projectlombok:lombok:1.18.24")
    annotationProcessor ("org.projectlombok:lombok:1.18.24")
// https://mvnrepository.com/artifact/commons-io/commons-io
    implementation("commons-io:commons-io:2.11.0")

    implementation("org.apache.commons:commons-text:1.10.0")
    implementation("commons-httpclient:commons-httpclient:3.1")
    implementation("io.github.biezhi:oh-my-email:0.0.4")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("net.coobird:thumbnailator:0.4.18")
    implementation("com.alibaba:fastjson:2.0.21")
//    implementation("com.fasterxml.jackson.core:jackson-core:2.13.4")
//    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4")
    implementation("org.jsoup:jsoup:1.15.3")
    implementation("com.atlassian.commonmark:commonmark:0.17.0")
    implementation("com.atlassian.commonmark:commonmark-ext-gfm-tables:0.17.0")
    implementation("com.atlassian.commonmark:commonmark-ext-yaml-front-matter:0.17.0")
    implementation("org.hibernate:hibernate-search-orm:5.11.11.Final")
    implementation("org.hibernate:hibernate-entitymanager:5.6.12.Final")
    // https://mvnrepository.com/artifact/org.elasticsearch/elasticsearch
//    implementation("org.elasticsearch:elasticsearch:8.5.3")
//
//    implementation("org.apache.lucene:lucene-analyzers-smartcn:8.11.2")
//    implementation("org.apache.lucene:lucene-highlighter:9.4.2")
    implementation("net.sf.ehcache:ehcache:2.10.9.2")
    implementation("org.apache.shiro:shiro-ehcache:1.10.0")
    implementation("org.apache.shiro:shiro-spring-boot-web-starter:1.10.0")
    implementation("com.upyun:java-sdk:4.2.3")
    implementation("com.aliyun.oss:aliyun-sdk-oss:3.15.2")
    // https://mvnrepository.com/artifact/com.qiniu/qiniu-java-sdk
    implementation("com.qiniu:qiniu-java-sdk:7.12.1")


    runtimeOnly("mysql:mysql-connector-java")
    runtimeOnly("com.h2database:h2:2.1.214")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

group = "com.mtons.mblog"
version = "latest"
description = "mblog"
java.sourceCompatibility = JavaVersion.VERSION_17


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

 
tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
