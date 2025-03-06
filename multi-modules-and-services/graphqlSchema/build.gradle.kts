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
        introspection {
            endpointUrl.set("https://schema-servicea.com")
            schemaFile.set(file("src/main/graphql/servicea/schema.graphqls"))
        }

        alwaysGenerateTypesMatching.set(emptyList())
    }

    service("service-b") {
        // Enable generation of metadata for use by downstream modules
        generateApolloMetadata.set(true)

        srcDir("src/main/graphql/serviceb")
        packageName.set("com.example.serviceb")
        introspection {
            endpointUrl.set("https://schema-serviceb.com")
            schemaFile.set(file("src/main/graphql/serviceb/schema.graphqls"))
        }

        alwaysGenerateTypesMatching.set(emptyList())
    }
}

dependencies {
    implementation("com.apollographql.apollo", "apollo-api")
    testImplementation(kotlin("test"))

    add("apolloServiceUsedCoordinates", project(":graphqlShared"))
}
