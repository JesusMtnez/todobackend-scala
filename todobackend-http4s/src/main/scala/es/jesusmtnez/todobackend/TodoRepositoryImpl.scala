package es.jesusmtnez.todobackend

import cats.effect.{Ref, Sync}
import cats.syntax.all.*
import es.jesusmtnez.todobackend.domain.TodoItem

object TodoRepositoryImpl {
  private final case class InMemory[F[_]: Sync](
      private val store: Ref[F, Map[String, TodoItem]]
  ) extends TodoRepository[F] {

    override def create(title: String): F[Option[TodoItem]] =
      val id = java.util.UUID.randomUUID().toString()
      store
        .updateAndGet { state =>
          state ++ Map(id -> TodoItem.uncompleted(title))
        }
        .map(_.get(id))

    override def delete(id: String): F[Unit] =
      store.update(_.removed(id))

    override def deleteAll(): F[Unit] =
      store.set(Map.empty[String, TodoItem])

    override def getById(id: String): F[Option[TodoItem]] =
      store.get.map(_.get(id))

    override def getAll(): F[List[TodoItem]] =
      store.get.map(_.values.toList)
  }

  def inMemory[F[_]: Sync](): F[TodoRepository[F]] =
    Ref[F]
      .of(Map.empty[String, TodoItem])
      .map(
        new InMemory[F](_)
      )
}
