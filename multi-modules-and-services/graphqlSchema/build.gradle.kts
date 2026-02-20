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
    }
}

dependencies {
    implementation("com.apollographql.apollo", "apollo-api")
    testImplementation(kotlin("test"))

    // Enable the bidirectional dependencies which allows to reduce the size of the generated code by telling
    // upstream modules to only generate the used types.
    // See https://www.apollographql.com/docs/kotlin/advanced/multi-modules/#auto-detection-of-used-types
    add("apolloService-aUsedCoordinates", project(":graphqlShared"))
    add("apolloService-bUsedCoordinates", project(":graphqlShared"))
}
