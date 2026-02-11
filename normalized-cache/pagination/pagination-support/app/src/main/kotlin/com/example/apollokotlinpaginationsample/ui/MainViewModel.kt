package com.example.apollokotlinpaginationsample.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.apollokotlinpaginationsample.repository.ApolloRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application = application) {
  sealed interface State {
    object InitialLoading : State
    data class Loaded(
      val repositories: ApolloRepository.PaginatedRepositories,
      val isLoadingMore: Boolean,
      val loadingError: Throwable?,
    ) : State
  }

  private val apolloRepository = ApolloRepository(application)

  private val isLoadingMore: MutableStateFlow<Boolean> = MutableStateFlow(false)
  private val loadingError: MutableStateFlow<Throwable?> = MutableStateFlow(null)

  val state: StateFlow<State> = combine(
    apolloRepository.watchRepositories(),
    isLoadingMore,
    loadingError
  ) { repositories, isLoadingMore, loadingError ->
    State.Loaded(
      repositories = repositories,
      isLoadingMore = isLoadingMore,
      loadingError = loadingError
    )
  }
    .stateIn(scope = viewModelScope, started = Lazily, initialValue = State.InitialLoading)

  fun refresh() {
    isLoadingMore.value = true
    viewModelScope.launch {
      try {
        apolloRepository.refreshRepositories()
        loadingError.value = null
      } catch (t: Throwable) {
        loadingError.value = t
      } finally {
        isLoadingMore.value = false
      }
    }
  }

  fun fetchAndMergeNextPage() {
    isLoadingMore.value = true
    viewModelScope.launch {
      try {
        apolloRepository.fetchAndMergeNextPage()
        loadingError.value = null
      } catch (t: Throwable) {
        loadingError.value = t
      } finally {
        isLoadingMore.value = false
      }
    }
  }
}
