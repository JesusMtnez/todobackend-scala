package es.jesusmtnez.todobackend

import cats.effect.*
import fs2.io.net.Network
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.Logger
import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.slf4j.Slf4jFactory

object TodoBackendServer {

  def run[F[_]: Async: Network]: F[Nothing] =
    implicit val loggerFactory: LoggerFactory[F] = Slf4jFactory.create[F]
    val httpApp = Logger.httpApp(true, true)(About.routes[F]().orNotFound)
    EmberServerBuilder.default[F].withHttpApp(httpApp).build.useForever

}
