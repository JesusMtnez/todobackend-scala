package es.jesusmtnez.todobackend

import buildinfo.BuildInfo
import io.circe.{Codec, Decoder}
import io.circe.generic.semiauto.*
import org.apache.pekko.http.scaladsl.server.Directives._
import org.apache.pekko.http.scaladsl.model.*
import org.apache.pekko.http.scaladsl.server.Route
import es.jesusmtnez.todobackend.domain.*
import com.github.pjfanning.pekkohttpcirce.FailFastCirceSupport.*
import org.apache.pekko.http.scaladsl.marshalling.Marshalling.*

object TodoBackendRoutes {

  val about: Route =
    path("about") {
      get {
        complete(HttpResponse(200, entity = HttpEntity(BuildInfo.toString)))
      }
    }

  def todo(repo: TodoRepository): Route = {

    implicit val todoItemCodec: Codec[TodoItem] = deriveCodec
    implicit val todoRequestDecoder: Decoder[TodoRequest] = deriveDecoder
    implicit val todoUpdateDecoder: Decoder[TodoUpdate] = deriveDecoder
    concat(
      pathEndOrSingleSlash {
        concat(
          get {
            complete {
              repo.getAll().toArray
            }
          },
          post {
            decodeRequest {
              entity(as[TodoRequest]) { item =>
                complete(repo.create(item.title, item.order))
              }
            }
          },
          delete {
            complete {
              repo.deleteAll()
              HttpResponse(204)
            }
          }
        )
      },
      path(JavaUUID) { id =>
        concat(
          get {
            complete(repo.getById(id))
          },
          patch {
            decodeRequest {
              entity(as[TodoUpdate]) { update =>
                complete(
                  repo.update(id, update.title, update.completed, update.order)
                )
              }
            }
          },
          delete {
            complete {
              repo.delete(id)
              HttpResponse(204)
            }
          }
        )
      }
    )
  }
}
