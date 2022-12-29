package tech.michalik.news

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tech.michalik.news.app.details.NewsFullPageViewModel
import tech.michalik.news.app.displayable.ArticleEntityToDisplayableMapper
import tech.michalik.news.app.list.NewsListViewModel
import tech.michalik.news.data.ErrorMapper
import tech.michalik.news.data.InMemoryDatabase
import tech.michalik.news.data.NewsApi
import tech.michalik.news.data.RemoteNewsApi
import tech.michalik.news.domain.ArticleDtoToEntityMapper
import tech.michalik.news.domain.FetchNewsItemUseCase
import tech.michalik.news.domain.FetchNewsUseCase
import tech.michalik.news.domain.ResolveDefaultSearchQueryUseCase

/**
 * Created by jaroslawmichalik
 */
val appModule = module {
  single {
    InMemoryDatabase()
  }

  single {
    FetchNewsItemUseCase(get())
  }

  single {
    FetchNewsUseCase(get(), get(), get())
  }

  single<NewsApi> {
    RemoteNewsApi(
      baseUrl = BuildConfig.BASE_URL,
      apiKey = BuildConfig.API_KEY,
      enableLogging = BuildConfig.ENABLE_NETWORK_LOGGING,
      errorMapper = get()
    )
  }

  single {
    ErrorMapper()
  }

  single {
    ArticleDtoToEntityMapper()
  }

  single {
    ArticleEntityToDisplayableMapper()
  }

  single {
    ResolveDefaultSearchQueryUseCase()
  }

  viewModel {
    NewsListViewModel(get(), get(), get(), get())
  }

  viewModel { parameters ->
    NewsFullPageViewModel(newsId = parameters.get(), get(), get())
  }

  single<DispatcherFacade> {
    AndroidDispatcherFacade()
  }

}