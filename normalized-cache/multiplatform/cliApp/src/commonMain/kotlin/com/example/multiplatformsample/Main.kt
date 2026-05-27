package com.example.multiplatformsample

import com.apollographql.apollo.ApolloClient
import com.apollographql.cache.normalized.memory.MemoryCacheFactory
import com.apollographql.cache.normalized.sql.SqlNormalizedCacheFactory
import com.example.browsersample.graphql.CharactersQuery
import com.example.browsersample.graphql.cache.Cache.cache

class Main {
  val apolloClient: ApolloClient by lazy {
    val memoryCache = MemoryCacheFactory(maxSizeBytes = 5 * 1024 * 1024)
    val sqlCache = SqlNormalizedCacheFactory()
    val memoryThenSqlCache = memoryCache.chain(sqlCache)

    ApolloClient.Builder()
      .serverUrl("https://rickandmortyapi.com/graphql")
      .cache(memoryThenSqlCache)
      .build()
  }

  suspend fun run() {
    println(apolloClient.query(CharactersQuery()).execute().data!!.characters!!.results!!.map { it!!.name }.joinToString())
  }
}
