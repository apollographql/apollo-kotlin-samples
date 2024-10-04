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

        // Depend on the schema and fragments from service "service-a" in module "graphqlShared".
        dependsOn(project(":graphqlShared"))
    }

    service("service-b") {
        srcDir("src/main/graphql/serviceb")
        packageName.set("com.example.serviceb")

        // Depend on the schema and fragments from service "service-b" in module "graphqlShared".
        dependsOn(project(":graphqlShared"))
    }
}
