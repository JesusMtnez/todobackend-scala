package es.jesusmtnez.todobackend

import zio._
import zio.http._
import zio.http.Middleware.{CorsConfig, cors}

object TodoBackendApp extends ZIOAppDefault:

  val corsConfig: CorsConfig =
    CorsConfig(
      allowedMethods = Header.AccessControlAllowMethods(
        Method.GET,
        Method.POST,
        Method.DELETE,
        Method.OPTIONS,
        Method.PATCH
      ),
      allowedHeaders = Header.AccessControlAllowHeaders("content-type")
    )

  private val routes =
    (TodoBackendRoutes.about ++ TodoBackendRoutes.todo) @@ cors(corsConfig)

  override val run =
    Server
      .serve(routes @@ Middleware.debug)
      .provide(Server.default, InMemoryRepository.layer)
