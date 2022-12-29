package tech.michalik.news.app

import kotlinx.datetime.Instant
import tech.michalik.news.data.dto.ArticleDto
import tech.michalik.news.data.dto.SourceDto
import tech.michalik.news.domain.ArticleEntity

fun fakeArticles(count: Int): List<ArticleEntity> {
  return (0 until count).map {
    ArticleEntity(
      sourceName = "Source #$it",
      title = "Title #$it",
      content = "Content",
      author = "Author",
      publishedAt = Instant.DISTANT_PAST,
      urlToImage = null,
      description = null,
      url = "https://link.to.article/$it"
    )
  }
}

fun fakeArticleDtos(count: Int): List<ArticleDto> {
  return (0 until count).map {
    ArticleDto(
      source = SourceDto(null, "Source #$it"),
      title = "Title #$it",
      content = "Content",
      author = "Author",
      publishedAt = "2022-12-16T11:22:11Z",
      urlToImage = null,
      description = null,
      url = "https://link.to.article/$it"
    )
  }
}