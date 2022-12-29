package tech.michalik.news

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AndroidDispatcherFacade: DispatcherFacade{
  override val io: CoroutineDispatcher
    get() = Dispatchers.IO
  override val main: CoroutineDispatcher
    get() = Dispatchers.Main

}