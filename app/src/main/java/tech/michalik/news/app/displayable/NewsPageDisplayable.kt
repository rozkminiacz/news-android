package tech.michalik.news.app.displayable

data class NewsPageDisplayable(
  val elements: List<NewsDisplayable>
) : List<NewsDisplayable> by elements
