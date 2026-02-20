plugins {
    kotlin("jvm")
    id("com.apollographql.apollo")
}

apollo {
    service("service-a") {
        // Enable generation of metadata for use by downstream modules
        generateApolloMetadata.set(true)

        srcDir("src/main/graphql/servicea")
        packageName.set("com.example.servicea")
    }

    service("service-b") {
        // Enable generation of metadata for use by downstream modules
        generateApolloMetadata.set(true)

        srcDir("src/main/graphql/serviceb")
        packageName.set("com.example.serviceb")
    }
}

dependencies {
    implementation("com.apollographql.apollo", "apollo-runtime")

    // Depend on the schema and fragments from service "service-a" in module "graphqlShared".
    // Note: this must happen **after** the `apollo {}` block or the `"apolloService"` configuration does not exist.
    add("apolloService-a", project(":graphqlSchema"))
    add("apolloService-a", project(":graphqlShared"))

    // Depend on the schema and fragments from service "service-b" in module "graphqlShared".
    add("apolloService-b", project(":graphqlSchema"))
    add("apolloService-b", project(":graphqlShared"))

    // Also add the Kotlin symbols as dependencies
    add("implementation", project(":graphqlSchema"))
    add("implementation", project(":graphqlShared"))
}
