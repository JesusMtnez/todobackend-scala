package es.jesusmtnez.todobackend

import org.apache.pekko
import pekko.actor.typed.ActorSystem
import pekko.actor.typed.scaladsl.Behaviors
import pekko.http.scaladsl.Http
import pekko.http.scaladsl.server.Directives._
// import scala.io.StdIn
import org.apache.pekko.http.cors.scaladsl.CorsDirectives._
import org.apache.pekko.http.cors.scaladsl.settings.CorsSettings
import org.apache.pekko.http.cors.scaladsl.model.*
import org.apache.pekko.http.scaladsl.model.HttpMethods
import com.typesafe.config.ConfigFactory

object TodoBackendApp {
  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem(Behaviors.empty, "my-system")
    implicit val executionContext = system.executionContext

    val corsSettings = CorsSettings(ConfigFactory.load(getClass.getClassLoader))
      .withAllowedOrigins(HttpOriginMatcher.*)
      .withAllowedMethods(
        Seq(
          HttpMethods.GET,
          HttpMethods.POST,
          HttpMethods.DELETE,
          HttpMethods.OPTIONS,
          HttpMethods.PATCH
        )
      )
      .withAllowedHeaders(HttpHeaderRange("Content-Type"))

    val repository = TodoRepository.inMemoryRepoistory()
    val (host, port) = "localhost" -> 8080
    val bindingFuture = Http()
      .newServerAt(host, port)
      .bindFlow(cors(corsSettings) {
        concat(
          TodoBackendRoutes.about,
          TodoBackendRoutes.todo(repository)
        )
      })

    bindingFuture.failed.foreach { ex =>
      system.log.error(s"Failed to bind to $host:$port!", ex)
    }
  }
}
