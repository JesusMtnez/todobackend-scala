package es.jesusmtnez.todobackend

import es.jesusmtnez.todobackend.domain.TodoItem

import java.util.UUID

trait TodoRepository {
  def create(title: String, order: Option[Int]): Option[TodoItem]
  def delete(id: UUID): Unit
  def deleteAll(): Unit
  def getAll(): List[TodoItem]
  def getById(id: UUID): Option[TodoItem]
  def update(
      id: UUID,
      title: Option[String],
      completed: Option[Boolean],
      order: Option[Int]
  ): Option[TodoItem]
}

object TodoRepository {
  def inMemoryRepoistory(): TodoRepository =
    new TodoRepository {

      private var todos = Map.empty[UUID, TodoItem]

      override def create(
          title: String,
          order: Option[Int]
      ): Option[TodoItem] = {
        val id = UUID.randomUUID()
        todos = todos ++ Map(
          id -> TodoItem.uncompleted(id, title, order.getOrElse(0))
        )
        todos.get(id)
      }

      override def delete(id: UUID): Unit =
        todos = todos.removed(id)

      override def deleteAll(): Unit =
        todos = Map.empty[UUID, TodoItem]

      override def getById(id: UUID): Option[TodoItem] =
        todos.get(id)

      override def getAll(): List[TodoItem] =
        todos.values.toList

      override def update(
          id: UUID,
          title: Option[String],
          completed: Option[Boolean],
          order: Option[Int]
      ): Option[TodoItem] = {
        todos = todos
          .get(id)
          .map { todo =>
            todo.copy(
              title = title.getOrElse(todo.title),
              completed = completed.getOrElse(todo.completed),
              order = order.getOrElse(todo.order)
            )
          }
          .fold(todos)(updated => todos.removed(id) ++ Map(id -> updated))

        todos.get(id)
      }
    }
}
