package es.jesusmtnez.todobackend

import buildinfo.BuildInfo
import zio._
import zio.http._

object TodoBackendRoutes:
  val about: Routes[Any, Nothing] = Routes(
    Method.GET / "about" -> Handler.from(Response.text(BuildInfo.toString))
  )

  val todo: Routes[Any, Nothing] = Routes.empty
