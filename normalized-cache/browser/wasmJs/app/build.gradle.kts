import com.apollographql.apollo.annotations.ApolloExperimental
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import java.io.File

fun prop(key: String) = project.findProperty(key).toString()

plugins {
  kotlin("multiplatform")
  id("org.jetbrains.compose")
  kotlin("plugin.compose")
  id("com.apollographql.apollo")
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
  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    browser()
    binaries.executable()
    compilerOptions {
      target.set("es2015")
    }
  }

  sourceSets {
    wasmJsMain {
      kotlin.srcDir(generateBuildConfigTask)

      dependencies {
        // Standard library
        implementation(kotlin("stdlib-wasm-js"))

        // Coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

        // Compose
        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.material3)
        implementation(compose.components.resources)

        // Apollo
        implementation("com.apollographql.apollo:apollo-runtime")
        implementation("com.apollographql.cache:normalized-cache-sqlite:1.0.0-beta.6")

        // sqlite.js / SQLDelight
        implementation("app.cash.sqldelight:web-worker-driver:2.1.0")
        implementation(devNpm("copy-webpack-plugin", "9.1.0"))
        implementation(npm("sql.js", "1.8.0"))

        implementation("org.jetbrains.kotlinx:kotlinx-browser:0.3")
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

    @OptIn(ApolloExperimental::class)
    plugin("com.apollographql.cache:normalized-cache-apollo-compiler-plugin:1.0.0-beta.6") {
      argument("com.apollographql.cache.packageName", packageName.get())
    }

    introspection {
      endpointUrl.set("https://api.github.com/graphql")
      schemaFile.set(file("src/main/graphql/schema.graphqls"))
      headers.put("Authorization", "Bearer ${prop("githubOauthKey")}")
    }
  }
}

compose.resources {
  packageOfResClass = "com.example.browsersample"
  generateResClass = always
}

// `./gradlew wasmJsBrowserDevelopmentRun --continuous` to run the dev server in continuous mode (should open `http://localhost:8080/`)
// `./gradlew wasmJsBrowserDevelopmentExecutableDistribution` to build the dev distribution, results are in `build/dist/js/developmentExecutable`
// `./gradlew wasmJsBrowserDistribution` to build the release distribution, results are in `build/dist/js/productionExecutable`
