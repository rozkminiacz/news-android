package tech.michalik.news.app.details

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tech.michalik.news.app.displayable.ArticleEntityToDisplayableMapper
import tech.michalik.news.domain.FetchNewsItemUseCase

class NewsFullPageViewModel(
  newsId: String,
  fetchNewsItem: FetchNewsItemUseCase,
  private val mapper: ArticleEntityToDisplayableMapper
) : ViewModel() {
  private val _state: MutableStateFlow<NewsFullPageState> = MutableStateFlow(NewsFullPageState())

  val state: StateFlow<NewsFullPageState>
    get() = _state.asStateFlow()

  init {
    val displayable = fetchNewsItem.fetchNewsItemById(newsId).let(mapper::map)
    _state.value = _state.value.copy(
      displayable = displayable
    )
  }
}