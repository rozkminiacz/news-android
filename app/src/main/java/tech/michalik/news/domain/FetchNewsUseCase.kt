package tech.michalik.news.domain

import tech.michalik.news.data.InMemoryDatabase
import tech.michalik.news.data.NewsApi

/**
 * Created by jaroslawmichalik
 */
class FetchNewsUseCase(
  private val newsApi: NewsApi,
  private val mapper: ArticleDtoToEntityMapper,
  private val inMemoryDatabase: InMemoryDatabase
) {
  suspend fun fetchData(query: String): List<ArticleEntity> {
    return newsApi.getBy(query).articles.map { mapper.map(it) }
      .also {
        inMemoryDatabase.update(it)
      }
  }
}