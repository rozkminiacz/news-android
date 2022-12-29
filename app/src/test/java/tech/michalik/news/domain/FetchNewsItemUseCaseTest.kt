package tech.michalik.news.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import tech.michalik.news.app.fakeArticles

/**
 * Created by jaroslawmichalik
 */
class FetchNewsItemUseCaseTest: StringSpec({
  "it should load data if present"{
    val data = fakeArticles(1).first()

    val usecase = FetchNewsItemUseCase(
      inMemoryDatabase = mockk{
        every { findById(any()) } returns data
      }
    )

    usecase.fetchNewsItemById("asd") shouldBe data
  }

  "it should throw error if not found"{
    val usecase = FetchNewsItemUseCase(
      inMemoryDatabase = mockk{
        every { findById(any()) } returns null
      }
    )

    shouldThrow<ArticleNotFoundException> {
      usecase.fetchNewsItemById("asd")
    }
  }

})