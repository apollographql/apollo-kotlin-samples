package com.example.main

import com.example.servicea.Feature1ServiceAQuery
import com.example.servicea.Feature2ServiceAQuery
import com.example.servicea.SharedModuleServiceAQuery
import com.example.serviceb.Feature1ServiceBQuery
import com.example.serviceb.Feature2ServiceBQuery
import com.example.serviceb.SharedModuleServiceBQuery

fun main() {
    // Defined in :graphqlSchema / service-a
    SharedModuleServiceAQuery()

    // Defined in :feature1 / service-a
    Feature1ServiceAQuery()

    // Defined in :feature2 / service-a
    Feature2ServiceAQuery()


    // Defined in :graphqlSchema / service-b
    SharedModuleServiceBQuery()

    // Defined in :feature1 / service-b
    Feature1ServiceBQuery()

    // Defined in :feature2 / service-b
    Feature2ServiceBQuery()
}
