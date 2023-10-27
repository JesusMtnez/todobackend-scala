package es.jesusmtnez.todobackend.domain

final case class TodoUpdate(
    title: Option[String],
    completed: Option[Boolean],
    order: Option[Int]
)
