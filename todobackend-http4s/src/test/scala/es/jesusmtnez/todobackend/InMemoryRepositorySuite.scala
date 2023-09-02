package es.jesusmtnez.todobackend

import cats.effect.IO
import munit.CatsEffectSuite
import es.jesusmtnez.todobackend.domain.TodoItem

class InMemoryRepositorySuite extends CatsEffectSuite {

  val repository = TodoRepositoryImpl.inMemory[IO]()

  test("getAll should return empty when there are no items"):
    for {
      repo <- repository
      result <- repo.getAll()
    } yield assert(result.isEmpty)

  test("getAll should return a value when there is 1 item"):
    val expected = TodoItem.uncompleted("my title")

    for {
      repo <- repository
      _ <- repo.create("my title")
      result <- repo.getAll()
    } yield assertEquals(result, List(expected))

  test("create adds a new item"):
    for {
      repo <- repository
      _ <- repo.deleteAll()
      _ <- repo.create("title")
      _ <- repo.create("title 2")
      result <- repo.getAll()
    } yield assertEquals(result.length, 2)

}
