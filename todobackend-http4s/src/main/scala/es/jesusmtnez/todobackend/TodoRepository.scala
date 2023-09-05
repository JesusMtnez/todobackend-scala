package es.jesusmtnez.todobackend

import cats.effect.{Ref, Sync}
import cats.effect.std.UUIDGen
import cats.syntax.all.*
import es.jesusmtnez.todobackend.domain.TodoItem

import java.util.UUID

trait TodoRepository[F[_]] {
  def create(title: String, order: Option[Int]): F[Option[TodoItem]]
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

object TodoRepository:

  def inMemory[F[_]: Sync](): F[TodoRepository[F]] =
    Ref[F]
      .of(Map.empty[UUID, TodoItem])
      .map(new InMemory[F](_))

  private final case class InMemory[F[_]: Sync](
      private val store: Ref[F, Map[UUID, TodoItem]]
  ) extends TodoRepository[F]:

    override def create(
        title: String,
        order: Option[Int]
    ): F[Option[TodoItem]] =
      for {
        id <- UUIDGen.randomUUID
        result <- store
          .updateAndGet { state =>
            state ++ Map(
              id -> TodoItem.uncompleted(id, title, order.getOrElse(0))
            )
          }
          .map(_.get(id))
      } yield result

    override def delete(id: UUID): F[Unit] =
      store.update(_.removed(id))

    override def deleteAll(): F[Unit] =
      store.set(Map.empty[UUID, TodoItem])

    override def getById(id: UUID): F[Option[TodoItem]] =
      store.get.map(_.get(id))

    override def getAll(): F[List[TodoItem]] =
      store.get.map(_.values.toList)

    override def update(
        id: UUID,
        title: Option[String],
        completed: Option[Boolean],
        order: Option[Int]
    ): F[Option[TodoItem]] =
      store
        .updateAndGet { items =>
          items
            .get(id)
            .map { todo =>
              val newTitle = title.getOrElse(todo.title)
              val newCompleted = completed.getOrElse(todo.completed)
              val newOrder = order.getOrElse(todo.order)
              todo.copy(
                title = newTitle,
                completed = newCompleted,
                order = newOrder
              )
            }
            .fold(items)(item => items.removed(id) + (id -> item))
        }
        .map(_.get(id))
