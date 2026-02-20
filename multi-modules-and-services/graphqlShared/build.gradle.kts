plugins {
    kotlin("jvm")
    id("com.apollographql.apollo")
}

apollo {
    service("service-a") {
        srcDir("src/main/graphql/servicea")
        packageName.set("com.example.servicea")
        generateApolloMetadata.set(true)
    }

    service("service-b") {
        srcDir("src/main/graphql/serviceb")
        packageName.set("com.example.serviceb")
        generateApolloMetadata.set(true)
    }
}

dependencies {
    implementation("com.apollographql.apollo", "apollo-api")

    // Depend on the schema from service "service-a" in module "graphqlSchema".
    // Note: this must happen **after** the `apollo {}` block or the `"apolloService"` configuration does not exist.
    add("apolloService-a", project(":graphqlSchema"))

    // Depend on the schema from service "service-b" in module "graphqlSchema".
    add("apolloService-b", project(":graphqlSchema"))

    // Also add the Kotlin symbols as dependencies
    add("implementation", project(":graphqlSchema"))

    // Enable the bidirectional dependencies which allows to reduce the size of the generated code by telling
    // upstream modules to only generate the used types.
    // See https://www.apollographql.com/docs/kotlin/advanced/multi-modules/#auto-detection-of-used-types
    add("apolloService-aUsedCoordinates", project(":feature1"))
    add("apolloService-aUsedCoordinates", project(":feature2"))
    add("apolloService-bUsedCoordinates", project(":feature1"))
    add("apolloService-bUsedCoordinates", project(":feature2"))
}
