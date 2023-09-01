package es.jesusmtnez.todobackend.domain

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

final case class TodoRequest(
    title: String
)

object TodoRequest:
  given Codec[TodoRequest] = deriveCodec
