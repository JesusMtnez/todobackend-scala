package es.jesusmtnez.todobackend.domain

final case class TodoRequest(
    title: String,
    order: Option[Int]
)
