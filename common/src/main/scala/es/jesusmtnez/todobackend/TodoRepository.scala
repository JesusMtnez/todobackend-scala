package es.jesusmtnez.todobackend

import es.jesusmtnez.todobackend.domain.TodoItem

import java.util.UUID

trait TodoRepository[F[_]] {
  def create(title: String): F[Option[TodoItem]]
  def delete(id: UUID): F[Unit]
  def deleteAll(): F[Unit]
  def getAll(): F[List[TodoItem]]
  def getById(id: UUID): F[Option[TodoItem]]
  def update(
      id: UUID,
      title: Option[String],
      completed: Option[Boolean],
      order: Option[Int]
  ): F[Option[TodoItem]]
}
