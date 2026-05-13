import com.apollographql.apollo.annotations.ApolloExperimental
import java.io.File

fun prop(key: String) = project.findProperty(key).toString()

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.jetbrains.compose)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.apollo)
}

// Generate a BuildConfig.kt file with a constant for the GitHub OAuth key.
val generateBuildConfigTask = tasks.register("generateBuildConfig") {
  val outputDir = layout.buildDirectory.dir("generated/source/kotlin").get().asFile
  outputs.dir(outputDir)
  doFirst {
    val outputWithPackageDir = File(outputDir, "com/example/browsersample").apply { mkdirs() }
    File(outputWithPackageDir, "BuildConfig.kt").writeText(
        """
        package com.example.browsersample
        object BuildConfig {
          const val GITHUB_OAUTH_KEY = "${prop("githubOauthKey")}"
        }
      """.trimIndent()
    )
  }
}

kotlin {
  js {
    browser()
    binaries.executable()
    compilerOptions {
      target.set("es2015")
    }
  }

  sourceSets {
    jsMain {
      kotlin.srcDir(generateBuildConfigTask)

      dependencies {
        // Standard library
        implementation(libs.kotlin.stdlib.js)

        // Coroutines
        implementation(libs.kotlinx.coroutines.core)

        // Compose
        implementation(libs.compose.runtime)
        implementation(libs.compose.html.core)

        // Apollo
        implementation(libs.apollo.runtime)
        implementation(libs.apollo.cache.sqlite)

        // sqlite.js / SQLDelight
        implementation(libs.sqldelight.web.worker.driver)
        implementation(devNpm("copy-webpack-plugin", "9.1.0"))
        implementation(npm("sql.js", "1.8.0"))

        // Commented out as we use our own custom Worker that loads/saves the db file via OPFS.
        // See `src/jsMain/resources/sqljs.opfs.worker.js`
        // Uncomment to use the default SQLDelight worker instead, which stays in memory.
        // implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.1.0"))
      }
    }
  }
}

apollo {
  service("main") {
    packageName.set("com.example.browsersample.graphql")

    plugin("com.apollographql.cache:normalized-cache-apollo-compiler-plugin:${libs.versions.apolloCache.get()}")
    pluginArgument("com.apollographql.cache.packageName", packageName.get())

    introspection {
      endpointUrl.set("https://api.github.com/graphql")
      schemaFile.set(file("src/main/graphql/schema.graphqls"))
      headers.put("Authorization", "Bearer ${prop("githubOauthKey")}")
    }
  }
}

// `./gradlew jsBrowserDevelopmentRun --continuous` to run the dev server in continuous mode (should open `http://localhost:8080/`)
// `./gradlew jsBrowserDevelopmentExecutableDistribution` to build the dev distribution, results are in `build/dist/js/developmentExecutable`
// `./gradlew jsBrowserDistribution` to build the release distribution, results are in `build/dist/js/productionExecutable`
