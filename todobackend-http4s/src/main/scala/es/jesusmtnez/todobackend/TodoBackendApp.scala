package es.jesusmtnez.todobackend

import cats.effect.*
import cats.implicits.*
import fs2.io.net.Network
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.*
import org.http4s.implicits.*
import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.slf4j.Slf4jFactory
import org.http4s.Method
import org.typelevel.ci.CIString

object TodoBackendApp extends IOApp.Simple:
  override val run = startServer[IO]

  def startServer[F[_]: Async: Network]: F[Unit] =
    implicit val loggerFactory: LoggerFactory[F] = Slf4jFactory.create[F]

    val cors =
      CORS.policy.withAllowOriginAll
        .withAllowMethodsIn(
          Set(
            Method.GET,
            Method.POST,
            Method.DELETE,
            Method.OPTIONS,
            Method.PATCH
          )
        )
        .withAllowHeadersIn(Set(CIString("content-type")))
    for {
      repository <- TodoRepository.inMemory[F]()
      routes = TodoBackendRoutes.about[F]()
        <+> TodoBackendRoutes.todo[F](repository)

      httpApp <- cors(Logger.httpApp(true, true)(routes.orNotFound))
      _ <- EmberServerBuilder.default[F].withHttpApp(httpApp).build.useForever
    } yield ()
