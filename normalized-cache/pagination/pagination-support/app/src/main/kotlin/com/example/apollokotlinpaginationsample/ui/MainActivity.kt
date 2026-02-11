package com.example.apollokotlinpaginationsample.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.apollokotlinpaginationsample.R
import com.example.apollokotlinpaginationsample.graphql.fragment.RepositoryFields
import com.example.apollokotlinpaginationsample.repository.ApolloRepository
import com.example.apollokotlinpaginationsample.ui.MainViewModel.State.InitialLoading
import com.example.apollokotlinpaginationsample.ui.MainViewModel.State.Loaded

class MainActivity : ComponentActivity() {
  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val state: MainViewModel.State by viewModel.state.collectAsState()
      MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
          when (val state = state) {
            is InitialLoading -> {
              Text(text = "Loading...")
            }

            is Loaded -> {
              if (state.loadingError != null) {
                Text(
                  text = "Error loading repositories: ${state.loadingError.message}",
                  color = MaterialTheme.colorScheme.error
                )
              }
              if (state.isLoadingMore) {
                Text(
                  text = "Loading more...",
                  color = MaterialTheme.colorScheme.primary
                )
              }
              RefreshBanner()
              RepositoryList(state.repositories)
            }
          }
        }
      }
    }
  }

  @Composable
  private fun RefreshBanner() {
    Box(modifier = Modifier.fillMaxWidth()) {
      Button(
        modifier = Modifier.align(Alignment.Center),
        onClick = {
          viewModel.refresh()
        }
      ) {
        Text("Refresh")
      }
    }
  }

  @Composable
  private fun RepositoryList(repositories: ApolloRepository.PaginatedRepositories) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
      items(repositories.repositories) {
        RepositoryItem(it)
      }
      item {
        if (repositories.hasNextPage) {
          LoadingItem()
          LaunchedEffect(Unit) {
            viewModel.fetchAndMergeNextPage()
          }
        }
      }
    }
  }

  @Composable
  private fun RepositoryItem(repositoryFields: RepositoryFields) {
    ListItem(
      headlineContent = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(modifier = Modifier.weight(1F), text = repositoryFields.name)
          Text(text = repositoryFields.stargazers.totalCount.toString(), style = MaterialTheme.typography.bodyMedium)
          Icon(
            painter = painterResource(R.drawable.ic_star_black_16dp),
            contentDescription = null
          )
        }
      },
      supportingContent = {
        Text(repositoryFields.description.orEmpty())
      }
    )
  }


  @Composable
  private fun LoadingItem() {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      CircularProgressIndicator()
    }
  }
}
