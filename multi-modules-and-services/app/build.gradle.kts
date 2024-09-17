plugins {
    kotlin("jvm")
    id("com.apollographql.apollo")
}

dependencies {
    implementation("com.apollographql.apollo", "apollo-runtime")

    // Dependencies on other Apollo modules
    implementation(project(":graphqlSchema"))
    implementation(project(":feature1"))
    implementation(project(":feature2"))
}

apollo {
    service("service-a") {
        packageName.set("com.example.servicea")

        // Dependencies on other Apollo modules
        dependsOn(project(":graphqlSchema"), bidirectional = true)
        dependsOn(project(":feature1"), bidirectional = true)
        dependsOn(project(":feature2"), bidirectional = true)
    }

    service("service-b") {
        packageName.set("com.example.serviceb")

        // Dependencies on other Apollo modules
        dependsOn(project(":graphqlSchema"), bidirectional = true)
        dependsOn(project(":feature1"), bidirectional = true)
        dependsOn(project(":feature2"), bidirectional = true)
    }
}
