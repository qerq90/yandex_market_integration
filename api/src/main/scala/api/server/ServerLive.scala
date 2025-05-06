package api.server

import api.server.config.ServerConfig
import io.circe.Json
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import service.order.OrderService
import zio.interop.catz._
import zio.{Task, UIO, ZIO}

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

final class ServerLive(config: ServerConfig, orderService: OrderService)
    extends Server {

  private val dsl = Http4sDsl[Task]

  import dsl._

  private val routes: HttpRoutes[Task] = HttpRoutes.of[Task] {
    case req @ POST -> Root / "notification" =>
      for {
        json   <- req.as[Json]
        n_type <- ZIO.fromEither(json.hcursor.get[String]("notificationType"))
        _ <- n_type match {
          case "PING" => ZIO.unit
          case _      => orderService.saveOrder(json)
        }
        jsonResp = Json.fromFields(
          List(
            ("name", "santexserv".asJson),
            (
              "time",
              ZonedDateTime
                .now(java.time.ZoneId.of("UTC"))
                .format(DateTimeFormatter.ISO_INSTANT)
                .asJson
            ),
            ("version", "1.0.0".asJson)
          )
        )
        resp <- Ok(jsonResp)
      } yield resp
  }

  private val httpApp: HttpApp[Task] = routes.orNotFound

  override def run(): UIO[Unit] =
    EmberServerBuilder
      .default[Task]
      .withHost(config.host)
      .withPort(config.port)
      .withHttpApp(httpApp)
      .build
      .useForever
      .orDie

}
