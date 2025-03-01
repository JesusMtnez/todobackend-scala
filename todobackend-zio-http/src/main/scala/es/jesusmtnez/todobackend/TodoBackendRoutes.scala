package es.jesusmtnez.todobackend

import buildinfo.BuildInfo
import zio.*
import zio.http.*
import zio.schema.*
import zio.schema.codec.JsonCodec.schemaBasedBinaryCodec
import es.jesusmtnez.todobackend.domain.*
import java.util.UUID

object TodoBackendRoutes:

  implicit val todoItemSchema: Schema[TodoItem] = DeriveSchema.gen[TodoItem]
  implicit val todoRequestSchema: Schema[TodoRequest] =
    DeriveSchema.gen[TodoRequest]
  implicit val todoUpdateSchema: Schema[TodoUpdate] =
    DeriveSchema.gen[TodoUpdate]

  val about: Routes[Any, Nothing] = Routes(
    Method.GET / "about" -> handler { Response.text(BuildInfo.toString) }
  )

  val todo: Routes[TodoRepository, Response] = Routes(
    Method.GET / Root -> handler {
      TodoRepository
        .getAll()
        .mapBoth(
          _ => Response.internalServerError("Failed to retrieve TODO items"),
          items => Response(body = Body.from(items))
        )
    },
    Method.GET / Root / uuid("id") -> handler { (id: UUID, _: Request) =>
      TodoRepository
        .getById(id)
        .mapBoth(
          _ => Response.internalServerError(s"Failed to retrieve item $id"),
          {
            case Some(item) => Response(body = Body.from(item))
            case None       => Response.notFound(s"Item $id not found!")
          }
        )
    },
    Method.POST / Root -> handler { (req: Request) =>
      for {
        item <- req.body.to[TodoRequest].orElseFail(Response.badRequest)
        response <-
          TodoRepository
            .create(item.title, item.order)
            .mapBoth(
              _ =>
                Response.internalServerError(s"Failed to create item: $item"),
              {
                case Some(created) => Response(body = Body.from(created))
                case None =>
                  Response
                    .internalServerError(s"Item $item could not be created")
              }
            )
      } yield response
    },
    Method.PATCH / Root / uuid("id") -> handler { (id: UUID, req: Request) =>
      for {
        update <- req.body.to[TodoUpdate].orElseFail(Response.badRequest)
        response <-
          TodoRepository
            .update(id, update.title, update.completed, update.order)
            .mapBoth(
              _ => Response.internalServerError(s"Failed to update item $id"),
              {
                case Some(updated) => Response(body = Body.from(updated))
                case None =>
                  Response.internalServerError(s"Item $id could not be updated")
              }
            )
      } yield response
    },
    Method.DELETE / Root -> handler {
      TodoRepository
        .deleteAll()
        .mapBoth(
          _ => Response.internalServerError("Error trying to delete all items"),
          _ => Response(status = Status.NoContent)
        )
    },
    Method.DELETE / Root / uuid("id") -> handler { (id: UUID, _: Request) =>
      TodoRepository
        .delete(id)
        .mapBoth(
          _ => Response.internalServerError(s"Error trying to delete item $id"),
          _ => Response(status = Status.NoContent)
        )
    }
  )
