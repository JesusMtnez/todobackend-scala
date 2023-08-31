package es.jesusmtnez.todobackend

import cats.effect.*
import cats.implicits.*
import fs2.io.net.Network
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.Logger
import org.http4s.implicits.*
import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.slf4j.Slf4jFactory

object TodoBackendServer {

  def run[F[_]: Async: Network]: F[Nothing] =
    implicit val loggerFactory: LoggerFactory[F] = Slf4jFactory.create[F]

    val routes =
      TodoBackendRoutes.about[F]()
        <+> TodoBackendRoutes.todo[F]()

    val httpApp = Logger.httpApp(true, true)(routes.orNotFound)

    EmberServerBuilder.default[F].withHttpApp(httpApp).build.useForever

}
