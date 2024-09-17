plugins {
    kotlin("jvm")
    id("com.apollographql.apollo")
}

dependencies {
    implementation("com.apollographql.apollo", "apollo-runtime")

    // Dependencies on other Apollo modules
    implementation(project(":graphqlShared"))
}

apollo {
    service("service-a") {
        srcDir("src/main/graphql/servicea")
        packageName.set("com.example.servicea")

        /*
         * Depend on the schema and fragments from service "service-a" in module "graphqlShared".
         * 
         * The `bidirectional` parameter allows to reduce the size of the generated code by telling
         * upstream modules to only generate the used types.
         *
         * See https://www.apollographql.com/docs/kotlin/advanced/multi-modules/#auto-detection-of-used-types
         */
        dependsOn(project(":graphqlShared"), bidirectional = true)
    }

    service("service-b") {
        srcDir("src/main/graphql/serviceb")
        packageName.set("com.example.serviceb")

        /*
         * Depend on the schema and fragments from service "service-b" in module "graphqlShared".
         * 
         * The `bidirectional` parameter allows to reduce the size of the generated code by telling
         * upstream modules to only generate the used types.
         *
         * See https://www.apollographql.com/docs/kotlin/advanced/multi-modules/#auto-detection-of-used-types
         */
        dependsOn(project(":graphqlShared"), bidirectional = true)
    }
}
