package tech.michalik.news.data

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import tech.michalik.news.domain.AppError
import java.io.File

/**
 * Created by jaroslawmichalik
 */
class RemoteNewsApiTest : StringSpec({
  "it should map json to dtos with no errors"{
    val data = getJson("response.json")

    val server = MockWebServer()
    val newsApi = RemoteNewsApi(
      baseUrl = server.url("/").toString(),
      apiKey = "",
      enableLogging = false,
      errorMapper = ErrorMapper()
    )

    server.use {
      it.enqueue(
        MockResponse()
          .setResponseCode(200)
          .setBody(data)
      )
      val responseDto = newsApi.getBy("asd")

      responseDto.articles shouldHaveSize 100
    }
  }

  "it should map errors to app error"{
    val data = getJson("error.json")

    val response = MockResponse()
      .setResponseCode(500)
      .setBody(data)

    val server = MockWebServer()
    val newsApi = RemoteNewsApi(
      baseUrl = server.url("/").toString(),
      apiKey = "",
      enableLogging = false,
      errorMapper = ErrorMapper()
    )

    server.use {
      it.enqueue(response)
      val error = shouldThrow<AppError> {
        newsApi.getBy("asd")
      }

      error shouldBe AppError.NetworkError(
        httpCode = 500,
        code = "rateLimited",
        message = "You have made too many requests recently. Developer accounts are limited to 100 requests over a 24 hour period (50 requests available every 12 hours). Please upgrade to a paid plan if you need more requests."
      )
    }
  }

  "it should add api key X-Header to requests"{
    val data = getJson("response.json")

    val apiKey = "asd123"

    val server = MockWebServer()
    val newsApi = RemoteNewsApi(
      baseUrl = server.url("/").toString(),
      apiKey = apiKey,
      enableLogging = false,
      errorMapper = ErrorMapper()
    )

    server.use {
      it.enqueue(
        MockResponse()
          .setResponseCode(200)
          .setBody(data)
      )
      newsApi.getBy("asd")

      it.takeRequest().getHeader("X-Api-Key") shouldBe apiKey
    }
  }
})

fun Any.getJson(path: String): String {
  // Load the JSON response
  val uri = this.javaClass.classLoader!!.getResource(path)
  val file = File(uri.path)
  return String(file.readBytes())
}