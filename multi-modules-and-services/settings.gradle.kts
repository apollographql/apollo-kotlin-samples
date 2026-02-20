@file:Suppress("UnstableApiUsage")

include(":graphqlSchema", ":graphqlShared", ":feature1", ":feature2", ":app")

pluginManagement {
    repositories {
//        mavenLocal()
//        maven {
//            url = uri("https://central.sonatype.com/repository/maven-snapshots/")
//        }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
//        mavenLocal()
//        maven {
//            url = uri("https://central.sonatype.com/repository/maven-snapshots/")
//        }
        google()
        mavenCentral()
    }
}
