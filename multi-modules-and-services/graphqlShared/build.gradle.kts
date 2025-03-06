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


        alwaysGenerateTypesMatching.set(emptyList())
    }

    service("service-b") {
        // Enable generation of metadata for use by downstream modules
        generateApolloMetadata.set(true)

        srcDir("src/main/graphql/serviceb")
        packageName.set("com.example.serviceb")

        alwaysGenerateTypesMatching.set(emptyList())
    }
}

dependencies {
    implementation("com.apollographql.apollo", "apollo-api")

    // Dependencies on other Apollo modules
    api(project(":graphqlSchema"))
    add("apolloService-a", project(":graphqlSchema"))
    add("apolloService-b", project(":graphqlSchema"))

    add("apolloService-aUsedCoordinates", project(":feature1"))
    add("apolloService-aUsedCoordinates", project(":feature2"))
    add("apolloService-bUsedCoordinates", project(":feature1"))
    add("apolloService-bUsedCoordinates", project(":feature2"))
}