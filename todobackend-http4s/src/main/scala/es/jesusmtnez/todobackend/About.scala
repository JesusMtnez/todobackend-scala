package es.jesusmtnez.todobackend

import buildinfo.BuildInfo
import cats.effect.Sync
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object About:
  def routes[F[_]: Sync](): HttpRoutes[F] =
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F]:
      case GET -> Root / "about" =>
        Ok(BuildInfo.toString)
