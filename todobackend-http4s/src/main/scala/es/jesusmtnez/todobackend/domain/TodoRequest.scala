package es.jesusmtnez.todobackend.domain

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class TodoRequest(
    title: String,
    order: Option[Int]
)

object TodoRequest:
  given Decoder[TodoRequest] = deriveDecoder
