package es.jesusmtnez.todobackend.domain

import java.util.UUID

final case class TodoItem(
    title: String,
    completed: Boolean,
    order: Int,
    url: String
)

object TodoItem:
  private val baseUrl: String = "http://localhost:8080"

  def uncompleted(id: UUID, title: String, order: Int): TodoItem =
    TodoItem(title, completed = false, order, url = s"$baseUrl/$id")
