package tech.michalik.news.domain

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import tech.michalik.news.app.fakeArticleDtos
import tech.michalik.news.data.InMemoryDatabase
import tech.michalik.news.data.dto.NewsResponseDto

/**
 * Created by jaroslawmichalik
 */
class FetchNewsUseCaseTest : BehaviorSpec({
  Given("data present in API") {

    val inMemoryDatabase : InMemoryDatabase = mockk(relaxUnitFun = true)

    val usecase = FetchNewsUseCase(
      newsApi = mockk {
        coEvery { getBy(any()) } returns NewsResponseDto(
          status = "ok",
          totalResults = 5,
          articles = fakeArticleDtos(5)
        )
      },
      mapper = ArticleDtoToEntityMapper(),
      inMemoryDatabase = inMemoryDatabase
    )

    When("invoke use case") {

      val result = usecase.fetchData("asd")

      Then("return data") {
        result.size shouldBe 5
      }
      Then("update inmemory db") {
        verify {
          inMemoryDatabase.update(any())
        }
      }
    }
  }

  Given("error happens"){

    val inMemoryDatabase : InMemoryDatabase = mockk(relaxUnitFun = true)

    val usecase = FetchNewsUseCase(
      newsApi = mockk {
        coEvery { getBy(any()) } throws AppError.Unknown
      },
      mapper = ArticleDtoToEntityMapper(),
      inMemoryDatabase = inMemoryDatabase
    )

    When("ask for data"){

      val result = kotlin.runCatching { usecase.fetchData("asd") }

      Then("propagate error"){
        result.exceptionOrNull() shouldBe AppError.Unknown
      }
      Then("do not update db"){
        verify(exactly = 0){
          inMemoryDatabase.update(any())
        }
      }
    }
  }
})