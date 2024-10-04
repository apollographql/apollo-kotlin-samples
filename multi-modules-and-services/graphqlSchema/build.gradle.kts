plugins {
    kotlin("jvm")
    id("com.apollographql.apollo")
}

dependencies {
    implementation("com.apollographql.apollo", "apollo-api")
    testImplementation(kotlin("test"))
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

        /*
         * Enable the bidirectional dependency which allows to reduce the size of the generated code by telling
         * upstream modules to only generate the used types.
         *
         * See https://www.apollographql.com/docs/kotlin/advanced/multi-modules/#auto-detection-of-used-types
         */
        isADependencyOf(project(":graphqlShared"))
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

        /*
         * Enable the bidirectional dependency which allows to reduce the size of the generated code by telling
         * upstream modules to only generate the used types.
         *
         * See https://www.apollographql.com/docs/kotlin/advanced/multi-modules/#auto-detection-of-used-types
         */
        isADependencyOf(project(":graphqlShared"))
    }
}
