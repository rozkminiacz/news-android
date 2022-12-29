package tech.michalik.news.app.list

import timber.log.Timber

class PrintlnTree : Timber.Tree() {
  override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
    println(message)
  }
}