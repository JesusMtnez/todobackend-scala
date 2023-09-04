package es.jesusmtnez.todobackend.domain

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class TodoUpdate(
    title: Option[String],
    completed: Option[Boolean],
    order: Option[Int]
)

object TodoUpdate:
  given Decoder[TodoUpdate] = deriveDecoder
