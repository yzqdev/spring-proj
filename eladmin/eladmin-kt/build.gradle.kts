import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.3"
    id("io.spring.dependency-management") version "1.0.13.RELEASE"
    id ("com.diffplug.eclipse.apt") version "3.37.2"
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.spring") version "1.7.10"
}

group = "me.zhengjie"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

subprojects {
    apply {
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
        plugin("com.diffplug.eclipse.apt")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")
    }
    dependencyManagement {
        dependencies {
            dependency("org.apache.shiro:shiro-spring-boot-web-starter:1.9.1")
            dependency("cn.hutool:hutool-all:5.8.5")
            dependency("com.baomidou:mybatis-plus-boot-starter:3.5.2")
            dependency("com.alibaba:druid-spring-boot-starter:1.2.8")
            dependency("com.auth0:java-jwt:4.0.0")
            dependency("org.mapstruct:mapstruct:1.5.2.Final")
            dependency("org.mapstruct:mapstruct-processor:1.5.2.Final")
            dependency("org.springdoc:springdoc-openapi-ui:1.6.11")
            // https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
            dependency("org.apache.commons:commons-lang3:3.12.0")
            dependency("com.baomidou:mybatis-plus-generator:3.5.3")
            dependency("com.google.guava:guava:31.1-jre")
            dependency("org.zalando:problem-spring-web:0.27.0")
            dependency("org.apache.shiro:shiro-spring-boot-web-starter:1.9.0")
            dependency("org.apache.commons:commons-pool2:2.11.1")
            dependency("org.bgee.log4jdbc-log4j2:log4jdbc-log4j2-jdbc4.1:1.16")

            dependency("org.lionsoul:ip2region:1.7.2")
            dependency("org.apache.poi:poi:5.2.0")
            dependency("org.apache.poi:poi-ooxml:5.2.0")
            dependency("xerces:xercesImpl:2.12.1")
            // https://mvnrepository.com/artifact/de.svenkubiak/jBCrypt
            dependency("de.svenkubiak:jBCrypt:0.4.3")
            dependency("org.apache.velocity:velocity-engine-core:2.3")
            // https://mvnrepository.com/artifact/org.thymeleaf.extras/thymeleaf-extras-springsecurity5
            dependency("org.thymeleaf.extras:thymeleaf-extras-springsecurity5:3.0.4.RELEASE")


        }
    }

    dependencies {
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        annotationProcessor("org.projectlombok:lombok:1.18.24")
        compileOnly("org.projectlombok:lombok:1.18.24")
        implementation("com.alibaba:fastjson:1.2.76")
        implementation("org.postgresql:postgresql")
        implementation("cn.hutool:hutool-all")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        implementation("org.springframework.boot:spring-boot-configuration-processor")
        implementation("org.springframework.boot:spring-boot-starter-aop")
        implementation("org.springframework.boot:spring-boot-starter-data-redis")
        implementation("org.springframework.boot:spring-boot-starter-security")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.apache.commons:commons-pool2")
        implementation("org.bgee.log4jdbc-log4j2:log4jdbc-log4j2-jdbc4.1")
        implementation("com.auth0:java-jwt")
        implementation("org.lionsoul:ip2region")
        implementation("org.apache.poi:poi")
        implementation("org.apache.poi:poi-ooxml")
        implementation("xerces:xercesImpl")
        implementation("org.mapstruct:mapstruct")
        annotationProcessor("org.mapstruct:mapstruct-processor")
        implementation("com.google.guava:guava")

        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("com.alibaba:druid-spring-boot-starter")

        // https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
        implementation("org.apache.commons:commons-lang3")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.springdoc:springdoc-openapi-ui")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
