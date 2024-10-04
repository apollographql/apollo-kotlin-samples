plugins {
    kotlin("jvm")
    id("com.apollographql.apollo")
}

dependencies {
    implementation("com.apollographql.apollo", "apollo-api")

    // Dependencies on other Apollo modules
    api(project(":graphqlSchema"))
}

apollo {
    service("service-a") {
        srcDir("src/main/graphql/servicea")
        packageName.set("com.example.servicea")

        // Depend on the schema from service "service-a" in module "graphqlSchema".
        dependsOn(project(":graphqlSchema"))

        /*
         * Enable the bidirectional dependencies which allow to reduce the size of the generated code by telling
         * upstream modules to only generate the used types.
         *
         * See https://www.apollographql.com/docs/kotlin/advanced/multi-modules/#auto-detection-of-used-types
         */
        isADependencyOf(project(":feature1"))
        isADependencyOf(project(":feature2"))
    }

    service("service-b") {
        srcDir("src/main/graphql/serviceb")
        packageName.set("com.example.serviceb")

        // Depend on the schema from service "service-b" in module "graphqlSchema".
        dependsOn(project(":graphqlSchema"))

        /*
         * Enable the bidirectional dependencies which allow to reduce the size of the generated code by telling
         * upstream modules to only generate the used types.
         *
         * See https://www.apollographql.com/docs/kotlin/advanced/multi-modules/#auto-detection-of-used-types
         */
        isADependencyOf(project(":feature1"))
        isADependencyOf(project(":feature2"))
    }
}
