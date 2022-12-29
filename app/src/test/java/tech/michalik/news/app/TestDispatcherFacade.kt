package tech.michalik.news.app

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import tech.michalik.news.DispatcherFacade

class TestDispatcherFacade(
  override val io: CoroutineDispatcher = UnconfinedTestDispatcher(),
  override val main: CoroutineDispatcher = UnconfinedTestDispatcher()
) : DispatcherFacade