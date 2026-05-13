@file:OptIn(ApolloExperimental::class)

import com.apollographql.apollo.annotations.ApolloExperimental

fun prop(key: String) = project.findProperty(key).toString()

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  id("com.apollographql.apollo")
}

android {
  namespace = "com.example.apollokotlinpaginationsample"
  compileSdk {
    version = release(36) {
      minorApiLevel = 1
    }
  }

  defaultConfig {
    applicationId = "com.example.apollokotlinpaginationsample"
    minSdk = 24
    targetSdk = 37
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    buildConfigField("String", "GITHUB_OAUTH_KEY", "\"${prop("githubOauthKey")}\"")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  buildFeatures {
    compose = true
    buildConfig = true
  }
}

apollo {
  service("main") {
    packageName.set("com.example.apollokotlinpaginationsample.graphql")

    plugin("com.apollographql.cache:normalized-cache-apollo-compiler-plugin:${libs.versions.apolloCache.get()}")
    pluginArgument("com.apollographql.cache.packageName", packageName.get())

    introspection {
      endpointUrl.set("https://api.github.com/graphql")
      schemaFile.set(file("src/main/graphql/schema.graphqls"))
      headers.put("Authorization", "Bearer ${prop("githubOauthKey")}")
    }
  }
}

dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  testImplementation(libs.junit)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
  debugImplementation(libs.androidx.compose.ui.tooling)

  implementation(libs.apollo.runtime)
  implementation(libs.apollo.cache.sqlite)
  implementation(libs.apollo.debug.server)
}
