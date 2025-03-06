package es.jesusmtnez.todobackend

import TodoBackendRoutes.*

object TodoBackendApp extends cask.Main {

  val repository = TodoRepository.inMemoryRepoistory()

  val allRoutes = Seq(AboutRoutes(), TodoRoutes(repository))
}
