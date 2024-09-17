include(":graphqlSchema", ":graphqlShared", ":feature1", ":feature2", ":app")

pluginManagement {
    repositories {
//        mavenLocal()
//        maven {
//            url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
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
//            url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
//        }
        google()
        mavenCentral()
    }
}
