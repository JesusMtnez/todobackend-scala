package es.jesusmtnez.todobackend

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple:
  val run = TodoBackendServer.run[IO]
