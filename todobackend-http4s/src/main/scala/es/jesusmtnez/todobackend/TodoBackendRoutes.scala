package es.jesusmtnez.todobackend

import buildinfo.BuildInfo
import cats.effect.*
import cats.syntax.all.*
import es.jesusmtnez.todobackend.domain.*
import org.http4s.HttpRoutes
import org.http4s.circe.*
import org.http4s.dsl.Http4sDsl

object TodoBackendRoutes:

  def about[F[_]: Sync](): HttpRoutes[F] =
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F]:
      case GET -> Root / "about" =>
        Ok(BuildInfo.toString)

  def todo[F[_]: Concurrent](repository: TodoRepository[F]): HttpRoutes[F] =
    val dsl = new Http4sDsl[F] {}
    import dsl._

    import org.http4s.circe.CirceEntityCodec.*

    HttpRoutes.of[F]:
      case GET -> Root =>
        for {
          items <- repository.getAll()
          response <- Ok(items)
        } yield response

      case req @ POST -> Root =>
        for {
          item <- req.as[TodoRequest]
          createdItem <- repository.create(item.title)
          response <- Ok(createdItem)
        } yield response
