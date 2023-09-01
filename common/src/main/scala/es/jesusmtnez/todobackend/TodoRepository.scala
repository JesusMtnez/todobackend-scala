package es.jesusmtnez.todobackend

import es.jesusmtnez.todobackend.domain.TodoItem

trait TodoRepository[F[_]] {
  def create(title: String): F[Option[TodoItem]]
  def delete(id: String): F[Unit]
  def deleteAll(): F[Unit]
  def getAll(): F[List[TodoItem]]
  def getById(id: String): F[Option[TodoItem]]
}
