package tech.michalik.news.app.list

import tech.michalik.news.app.displayable.NewsPageDisplayable
import tech.michalik.news.domain.AppError

data class NewsListScreenState(
  val pageDisplayable: NewsPageDisplayable,
  val loading: Boolean,
  val error: AppError?,
  val searchQuery: String,
) {
  fun isError() = error != null
  fun hasEmptyResults() = pageDisplayable.isEmpty()
  fun hasResults() = pageDisplayable.isNotEmpty()

  companion object {
    val INITIAL = NewsListScreenState(
      NewsPageDisplayable(emptyList()),
      loading = true,
      error = null,
      searchQuery = ""
    )
  }
}