package es.jesusmtnez.todobackend

import zio.*
import es.jesusmtnez.todobackend.domain.TodoItem

import java.util.UUID

trait TodoRepository:
  def create(title: String, order: Option[Int]): Task[Option[TodoItem]]
  def delete(id: UUID): Task[Unit]
  def deleteAll(): Task[Unit]
  def getAll(): Task[List[TodoItem]]
  def getById(id: UUID): Task[Option[TodoItem]]
  def update(
      id: UUID,
      title: Option[String],
      completed: Option[Boolean],
      order: Option[Int]
  ): Task[Option[TodoItem]]

final case class InMemoryRepository(
    private val store: Ref[Map[UUID, TodoItem]]
) extends TodoRepository:

  override def create(
      title: String,
      order: Option[Int]
  ): UIO[Option[TodoItem]] = {
    for {
      id <- ZIO.succeedUnsafe(_ => UUID.randomUUID())
      result <- store
        .updateAndGet { state =>
          state ++ Map(
            id -> TodoItem.uncompleted(id, title, order.getOrElse(0))
          )
        }
        .map(_.get(id))
    } yield result
  }

  override def delete(id: UUID): UIO[Unit] =
    store.update(_.removed(id))

  override def deleteAll(): UIO[Unit] =
    store.set(Map.empty[UUID, TodoItem])

  override def getById(id: UUID): UIO[Option[TodoItem]] =
    store.get.map(_.get(id))

  override def getAll(): UIO[List[TodoItem]] =
    store.get.map(_.values.toList)

  override def update(
      id: UUID,
      title: Option[String],
      completed: Option[Boolean],
      order: Option[Int]
  ): UIO[Option[TodoItem]] =
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

object InMemoryRepository:
  val layer: ZLayer[Any, Nothing, TodoRepository] =
    ZLayer.fromZIO(
      Ref.make(Map.empty[UUID, TodoItem]).map(InMemoryRepository(_))
    )
