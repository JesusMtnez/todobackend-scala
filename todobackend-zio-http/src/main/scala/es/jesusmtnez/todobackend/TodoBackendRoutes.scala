package es.jesusmtnez.todobackend

import buildinfo.BuildInfo
import zio._
import zio.http._

object TodoBackendRoutes:
  val about: HttpApp[Any, Nothing] = Http.collect[Request]:
    case Method.GET -> Root / "about" => Response.text(BuildInfo.toString)

  val todo: HttpApp[Any, Nothing] = Http.empty
