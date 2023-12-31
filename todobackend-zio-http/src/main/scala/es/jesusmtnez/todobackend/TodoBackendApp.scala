package es.jesusmtnez.todobackend

import zio._
import zio.http._

object TodoBackendApp extends ZIOAppDefault:

  private val routes = TodoBackendRoutes.about ++ TodoBackendRoutes.todo

  override val run =
    Server
      .serve(routes.toHttpApp @@ Middleware.debug)
      .provide(Server.default)
