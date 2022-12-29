package tech.michalik.news

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherFacade{
  val io: CoroutineDispatcher
  val main: CoroutineDispatcher
}