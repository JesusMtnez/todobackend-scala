package es.jesusmtnez.todobackend

import buildinfo.BuildInfo
import cats.effect.*
import cats.syntax.all.*
import es.jesusmtnez.todobackend.domain.*
import io.circe.*
import io.circe.generic.semiauto.*
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

    implicit val todoItemCodec: Codec[TodoItem] = deriveCodec
    implicit val todoRequestDecoder: Decoder[TodoRequest] = deriveDecoder
    implicit val todoUpdateDecoder: Decoder[TodoUpdate] = deriveDecoder

    import org.http4s.circe.CirceEntityCodec.*

    HttpRoutes.of[F]:
      case GET -> Root =>
        for {
          items <- repository.getAll()
          response <- Ok(items)
        } yield response

      case GET -> Root / UUIDVar(id) =>
        for {
          item <- repository.getById(id)
          response <- Ok(item)
        } yield response

      case req @ POST -> Root =>
        for {
          item <- req.as[TodoRequest]
          createdItem <- repository.create(item.title, item.order)
          response <- Ok(createdItem)
        } yield response

      case req @ PATCH -> Root / UUIDVar(id) =>
        for {
          update <- req.as[TodoUpdate]
          updatedItem <- repository.update(
            id,
            update.title,
            update.completed,
            update.order
          )
          response <- Ok(updatedItem)
        } yield response

      case DELETE -> Root =>
        for {
          _ <- repository.deleteAll()
          response <- NoContent()
        } yield response

      case DELETE -> Root / UUIDVar(id) =>
        for {
          _ <- repository.delete(id)
          response <- NoContent()
        } yield response
