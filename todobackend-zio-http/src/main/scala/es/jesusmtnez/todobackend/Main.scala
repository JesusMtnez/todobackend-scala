package es.jesusmtnez.todobackend

import buildinfo.BuildInfo
import zio._
import zio.http._

object Main extends ZIOAppDefault:
  val app: HttpApp[Any, Nothing] = Http.collect[Request]:
    case Method.GET -> Root / "about" => Response.text(BuildInfo.toString)

  override val run =
    Server.serve(app @@ RequestHandlerMiddlewares.debug).provide(Server.default)
