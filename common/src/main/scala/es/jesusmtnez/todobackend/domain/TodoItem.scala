package es.jesusmtnez.todobackend.domain

import io.circe.*
import io.circe.generic.semiauto.*

import java.util.UUID

final case class TodoItem(
    title: String,
    completed: Boolean,
    url: String
)

object TodoItem:
  given Codec[TodoItem] = deriveCodec

  private val baseUrl: String = "http://localhost:8080"

  def uncompleted(id: UUID, title: String): TodoItem =
    TodoItem(title, completed = false, url = s"$baseUrl/$id")
