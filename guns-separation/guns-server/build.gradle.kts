import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.4"
    id("io.spring.dependency-management") version "1.0.13.RELEASE"
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.spring") version "1.7.20"
}

group = "com.example"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_17



subprojects {
    apply {
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")
    }
    dependencyManagement {
        dependencies {
            dependency("org.apache.shiro:shiro-spring-boot-web-starter:1.9.1")
            dependency("cn.hutool:hutool-all:5.8.5")
            dependency("com.baomidou:mybatis-plus-boot-starter:3.5.2")
            dependency("com.auth0:java-jwt:4.0.0")
            dependency("org.mapstruct:mapstruct:1.4.2.Final")
            dependency("org.mapstruct:mapstruct-processor:1.4.2.Final")
            dependency("org.springdoc:springdoc-openapi-ui:1.6.11")
            // https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
            dependency("org.apache.commons:commons-lang3:3.12.0")
            dependency("com.baomidou:mybatis-plus-generator:3.5.3")
            dependency("com.google.guava:guava:31.1-jre")
            dependency("org.zalando:problem-spring-web:0.27.0")
            dependency("org.apache.shiro:shiro-spring-boot-web-starter:1.9.0")
            // https://mvnrepository.com/artifact/de.svenkubiak/jBCrypt
            dependency("de.svenkubiak:jBCrypt:0.4.3")
            dependency("org.apache.velocity:velocity-engine-core:2.3")
            // https://mvnrepository.com/artifact/org.thymeleaf.extras/thymeleaf-extras-springsecurity5
            dependency("org.thymeleaf.extras:thymeleaf-extras-springsecurity5:3.0.4.RELEASE")


        }
    }

    dependencies {
        annotationProcessor("org.projectlombok:lombok:1.18.24")
        compileOnly("org.projectlombok:lombok:1.18.24")
        implementation("com.alibaba:fastjson:1.2.76")
        implementation("org.postgresql:postgresql")
        implementation("cn.hutool:hutool-all")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-configuration-processor")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.springdoc:springdoc-openapi-ui")
        implementation("cn.afterturn:easypoi-spring-boot-starter:4.4.0")

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
