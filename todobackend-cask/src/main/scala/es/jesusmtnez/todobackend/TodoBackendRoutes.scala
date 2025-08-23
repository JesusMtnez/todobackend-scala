package es.jesusmtnez.todobackend

import buildinfo.BuildInfo
import cask.*
import es.jesusmtnez.todobackend.domain.*
import upickle.default.*

import java.util.UUID

object TodoBackendRoutes {

  case class AboutRoutes() extends cask.Routes {
    @cask.get("/about")
    def about() = BuildInfo.toString

    initialize()
  }

  case class TodoRoutes(repo: TodoRepository) extends cask.Routes {

    implicit val todoItemWriter: Writer[TodoItem] =
      upickle.default.macroW[TodoItem]
    implicit val todoRequestReader: Reader[TodoRequest] =
      upickle.default.macroR[TodoRequest]
    implicit val todoUpdateReader: Reader[TodoUpdate] =
      upickle.default.macroR[TodoUpdate]

    private val corsHeaders = Seq(
      "Access-Control-Allow-Origin" -> "*",
      "Access-Control-Allow-Headers" -> "Content-Type",
      "Access-Control-Allow-Methods" -> "GET,POST,DELETE,OPTIONS,PATCH"
    )

    private val corsResponse = Response((), 204, corsHeaders)

    @cask.options("/")
    def rootCors() = corsResponse

    @cask.options("/:id")
    @annotation.nowarn
    def pathCors(id: String) = corsResponse

    @cask.get("/")
    def getAll() = {
      Response(
        upickle.default.write(repo.getAll()),
        200,
        corsHeaders
      )
    }

    @cask.get("/:id")
    def getById(id: String) = {
      repo
        .getById(UUID.fromString(id))
        .fold(
          cask.model.Response(s"Item $id could not be found", 404, corsHeaders)
        )(item => cask.Response(upickle.default.write(item), 200, corsHeaders))
    }

    @cask.post("/")
    def create(req: Request) = {
      val item = upickle.default.read[TodoRequest](req.data)
      repo
        .create(item.title, item.order)
        .fold(
          cask.Response(s"Item $item could not be created", 500, corsHeaders)
        )(created =>
          cask.Response(upickle.default.write(created), 201, corsHeaders)
        )
    }

    @cask.patch("/:id")
    def update(id: String, req: Request) = {
      val update = upickle.default.read[TodoUpdate](req.data)
      repo
        .update(
          UUID.fromString(id),
          update.title,
          update.completed,
          update.order
        )
        .fold(
          cask.Response(s"Item $id could not be updated", 500, corsHeaders)
        )(item => cask.Response(upickle.default.write(item), 200, corsHeaders))
    }

    @cask.delete("/")
    def deleteAll() = {
      repo.deleteAll()
      Response((), 204, corsHeaders)
    }

    @cask.delete("/:id")
    def delete(id: String) = {
      repo.delete(UUID.fromString(id))
      Response((), 204, corsHeaders)
    }

    initialize()
  }
}
