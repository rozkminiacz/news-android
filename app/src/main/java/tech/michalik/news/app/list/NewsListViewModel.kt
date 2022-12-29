package tech.michalik.news.app.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import tech.michalik.news.DispatcherFacade
import tech.michalik.news.app.displayable.ArticleEntityToDisplayableMapper
import tech.michalik.news.app.displayable.NewsPageDisplayable
import tech.michalik.news.domain.AppError
import tech.michalik.news.domain.ResolveDefaultSearchQueryUseCase
import tech.michalik.news.domain.FetchNewsUseCase
import timber.log.Timber

/**
 * Created by jaroslawmichalik
 */
class NewsListViewModel(
  private val fetchNewsUseCase: FetchNewsUseCase,
  private val displayableMapper: ArticleEntityToDisplayableMapper,
  resolveDefaultSearchQuery: ResolveDefaultSearchQueryUseCase,
  dispatcherFacade: DispatcherFacade
) : ViewModel() {

  private val _state: MutableStateFlow<NewsListScreenState> =
    MutableStateFlow(NewsListScreenState.INITIAL.copy(
      searchQuery = resolveDefaultSearchQuery.fetchDefaultQuery()
    ))

  val state: StateFlow<NewsListScreenState>
    get() = _state.asStateFlow()

  private val searchFlow = MutableSharedFlow<String>(1)
    .also { it.tryEmit(_state.value.searchQuery) }

  fun search(text: String) {
    Timber.d("New state: search field update")
    _state.value = _state.value.copy(
      searchQuery = text,
    )

    searchFlow.tryEmit(text)
  }

  private suspend fun fetchData(query: String) {
    Timber.d("Fetching for $query")
    val newState = try {
      val page = fetchNewsUseCase.fetchData(query).map { displayableMapper.map(it) }.let {
        NewsPageDisplayable(
          elements = it
        )
      }
      _state.value.copy(
        pageDisplayable = page,
        error = null,
        loading = false
      )
    } catch (applicationError: AppError) {
      _state.value.copy(
        error = applicationError,
        loading = false
      )
    }

    Timber.d("New state: data fetch")
    _state.value = newState
  }

  fun rerunCurrentSearch() {
    Timber.d("Rerun search")
    search(_state.value.searchQuery)
  }

  init {
    viewModelScope.launch(dispatcherFacade.io) {
      searchFlow.debounce(300)
        .mapLatest {
          Timber.d("New state: start loading")
          _state.value = _state.value.copy(
            loading = true
          )
          fetchData(_state.value.searchQuery)
        }.collect()
    }
  }
}

