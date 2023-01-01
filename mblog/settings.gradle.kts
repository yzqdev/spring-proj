pluginManagement {
    repositories {
        maven { url = uri("https://repo.spring.io/milestone") }
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {

        maven { url = uri("https://repo.spring.io/milestone") }
        maven {
            url = uri("https://maven.aliyun.com/repository/public/")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/google/")
        }
        mavenCentral()
        versionCatalogs {
            create("libs") {
                version("retrofit", "2.9.0")
                library("commons-lang3", "org.apache.commons", "commons-lang3").version {
                    strictly("[3.8, 4.0[")
                    prefer("3.9")
                }
                library("retrofit", "com.squareup.retrofit2", "retrofit").versionRef("retrofit")
                library( "hutool", "cn.hutool:hutool-all:5.8.9"  )
                library("mp","com.baomidou:mybatis-plus-boot-starter:3.5.2")
                library("commonIo","commons-io:commons-io:2.11.0")

            }
        }
    }
}
enableFeaturePreview("VERSION_CATALOGS")

rootProject.name = "mblog"
