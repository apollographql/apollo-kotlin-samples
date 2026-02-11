package com.example.apollokotlinpaginationsample.repository

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.apollographql.apollo.debugserver.ApolloDebugServer
import com.apollographql.apollo.exception.CacheMissException
import com.apollographql.cache.normalized.FetchPolicy
import com.apollographql.cache.normalized.api.CacheKey
import com.apollographql.cache.normalized.fetchPolicy
import com.apollographql.cache.normalized.memory.MemoryCacheFactory
import com.apollographql.cache.normalized.sql.SqlNormalizedCacheFactory
import com.apollographql.cache.normalized.watch
import com.example.apollokotlinpaginationsample.BuildConfig
import com.example.apollokotlinpaginationsample.graphql.RepositoryListQuery
import com.example.apollokotlinpaginationsample.graphql.cache.Cache.cache
import com.example.apollokotlinpaginationsample.graphql.fragment.RepositoryFields
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map

private const val SERVER_URL = "https://api.github.com/graphql"

private const val HEADER_AUTHORIZATION = "Authorization"
private const val HEADER_AUTHORIZATION_BEARER = "Bearer"

class ApolloRepository(context: Context) {
  private val apolloClient: ApolloClient by lazy {
    val memoryCache = MemoryCacheFactory(maxSizeBytes = 5 * 1024 * 1024)
    val sqlCache = SqlNormalizedCacheFactory(context, "app.db")
    val memoryThenSqlCache = memoryCache.chain(sqlCache)

    ApolloClient.Builder()
      .serverUrl(SERVER_URL)

      // Add headers for authentication
      .addHttpHeader(
        HEADER_AUTHORIZATION,
        "$HEADER_AUTHORIZATION_BEARER ${BuildConfig.GITHUB_OAUTH_KEY}"
      )

      // Normalized cache
      .cache(
        normalizedCacheFactory = memoryThenSqlCache,
        keyScope = CacheKey.Scope.SERVICE,
      )

      .build()

      // Apollo Debug Server
      .also {
        if (BuildConfig.DEBUG) ApolloDebugServer.registerApolloClient(it)
      }
  }

  data class PaginatedRepositories(
    val repositories: List<RepositoryFields>,
    val hasNextPage: Boolean,
  )

  fun watchRepositories(): Flow<PaginatedRepositories> = apolloClient.query(RepositoryListQuery())
    .watch()
    .filterNot { it.exception is CacheMissException }
    .map { response ->
      PaginatedRepositories(
        response.data!!.organization!!.repositories.edges!!.map { it!!.node!!.repositoryFields },
        response.data!!.organization!!.repositories.pageInfo.hasNextPage
      )
    }

  suspend fun refreshRepositories() {
    // Re-fetching the 1st page from the network will discard all other pages from the cache
    apolloClient.query(RepositoryListQuery())
      .fetchPolicy(FetchPolicy.NetworkOnly)
      .execute()
      .dataOrThrow()
  }

  suspend fun fetchAndMergeNextPage() {
    // 1. Get the current list from the cache
    val listQuery = RepositoryListQuery()
    val cacheResponse = apolloClient.query(listQuery).fetchPolicy(FetchPolicy.CacheOnly).execute()

    // 2. Fetch the next page from the network and store it in the cache
    val after = cacheResponse.data!!.organization!!.repositories.pageInfo.endCursor
    apolloClient.query(RepositoryListQuery(after = Optional.presentIfNotNull(after)))
      .fetchPolicy(FetchPolicy.NetworkOnly)
      .execute()
      .dataOrThrow()
  }
}
