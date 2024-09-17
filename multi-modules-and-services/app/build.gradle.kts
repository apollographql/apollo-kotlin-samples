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

        /*
         * Depend on the schema and fragments from "service-a" service in "feature1" and "feature2" modules.
         * 
         * The `bidirectional` parameter allows to reduce the size of the generated code by telling
         * upstream modules to only generate the used types.
         *
         * See https://www.apollographql.com/docs/kotlin/advanced/multi-modules/#auto-detection-of-used-types
         */
        dependsOn(project(":feature1"), bidirectional = true)
        dependsOn(project(":feature2"), bidirectional = true)
    }

    service("service-b") {
        packageName.set("com.example.serviceb")

        /*
         * Depend on the schema and fragments from service "service-b" in feature1 and feature2 modules.
         * 
         * The `bidirectional` parameter allows to reduce the size of the generated code by telling
         * upstream modules to only generate the used types.
         *
         * See https://www.apollographql.com/docs/kotlin/advanced/multi-modules/#auto-detection-of-used-types
         */
        dependsOn(project(":feature1"), bidirectional = true)
        dependsOn(project(":feature2"), bidirectional = true)
    }
}
