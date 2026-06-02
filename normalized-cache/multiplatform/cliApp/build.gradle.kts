@file:OptIn(KotlinNativeCacheApi::class)

import com.apollographql.apollo.annotations.ApolloExperimental
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.mpp.DisableCacheInKotlinVersion
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCacheApi

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.apollo)
}

kotlin {
  macosArm64 {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    binaries {
      executable {
        entryPoint = "main"
      }
    }

    // Workaround for https://youtrack.jetbrains.com/issue/KT-86570
    binaries.all {
      freeCompilerArgs += "-Xbinary=genericSafeCasts=false"
    }
  }

  sourceSets {
    commonMain {
      dependencies {
        implementation(libs.apollo.runtime)
        implementation(libs.apollo.cache.sqlite)
      }
    }
  }
}

apollo {
  service("main") {
    packageName.set("com.example.browsersample.graphql")

    @OptIn(ApolloExperimental::class)
    plugin("com.apollographql.cache:normalized-cache-apollo-compiler-plugin:${libs.versions.apolloCache.get()}")
    pluginArgument("com.apollographql.cache.packageName", packageName.get())

    introspection {
      endpointUrl.set("https://rickandmortyapi.com/graphql")
      schemaFile.set(file("src/commonMain/graphql/schema.graphqls"))
    }
  }
}
