package es.jesusmtnez.todobackend.domain

import io.circe.*
import io.circe.generic.semiauto.*

final case class TodoItem(
    title: String,
    completed: Boolean
)

object TodoItem:
  given Codec[TodoItem] = deriveCodec

  def uncompleted(title: String): TodoItem =
    TodoItem(title, completed = false)
