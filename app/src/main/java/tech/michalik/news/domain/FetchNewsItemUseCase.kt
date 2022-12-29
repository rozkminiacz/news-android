package tech.michalik.news.domain

import tech.michalik.news.data.InMemoryDatabase

class FetchNewsItemUseCase(private val inMemoryDatabase: InMemoryDatabase) {
  fun fetchNewsItemById(newsId: String): ArticleEntity {
    return inMemoryDatabase.findById(newsId) ?: throw ArticleNotFoundException(newsId)
  }
}
