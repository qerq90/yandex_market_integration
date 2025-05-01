package server

import server.config.ServerConfig
import zio.{Task, ZIO}
import zio.interop.catz._
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.ember.server.EmberServerBuilder

final class ServerLive(config: ServerConfig)
    extends Server {

  private val dsl = Http4sDsl[Task]
  import dsl._

  private val routes: HttpRoutes[Task] = HttpRoutes.of[Task] {
    case GET -> Root / "counter" => Ok(ZIO.attempt(1.toString))
  }

  private val httpApp: HttpApp[Task] = routes.orNotFound

  override def run(): ZIO[Any, Throwable, Unit] =
    EmberServerBuilder
      .default[Task]
      .withHost(config.host)
      .withPort(config.port)
      .withHttpApp(httpApp)
      .build
      .useForever
}
