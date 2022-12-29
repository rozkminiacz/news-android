package tech.michalik.news.data

import tech.michalik.news.domain.ArticleEntity

class InMemoryDatabase() {
  private val data = HashMap<String, ArticleEntity>()

  fun findById(id: String): ArticleEntity? {
    return data[id]
  }

  fun update(elements: List<ArticleEntity>) {
    data.clear()
    data.putAll(
      elements.map {
        it.id to it
      }.toMap()
    )
  }
}

val db = InMemoryDatabase()
