plugins {
    kotlin("jvm")
}

dependencies {
    implementation("com.apollographql.apollo", "apollo-runtime", "5.2.0")

    // Dependencies on other Apollo modules
    implementation(project(":graphqlShared"))
    implementation(project(":feature1"))
    implementation(project(":feature2"))
}
