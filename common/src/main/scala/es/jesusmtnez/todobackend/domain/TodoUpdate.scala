package es.jesusmtnez.todobackend.domain

final case class TodoUpdate(
    title: Option[String] = None,
    completed: Option[Boolean] = None,
    order: Option[Int] = None
)
