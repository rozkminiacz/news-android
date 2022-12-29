package tech.michalik.news.app.details

import tech.michalik.news.app.displayable.NewsDisplayable

data class NewsFullPageState(
  val displayable: NewsDisplayable? = null,
  val loading: Boolean = false,
  val error: Throwable? = null
)