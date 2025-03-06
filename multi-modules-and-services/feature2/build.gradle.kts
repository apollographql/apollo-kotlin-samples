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
    implementation("com.apollographql.apollo", "apollo-runtime")

    // Dependencies on other Apollo modules
    implementation(project(":graphqlShared"))
    add("apolloService-a", project(":graphqlShared"))
    add("apolloService-b", project(":graphqlShared"))
}