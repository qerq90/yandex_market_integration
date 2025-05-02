package server

import server.config.ServerConfig
import service.order.OrderService
import zio._

trait Server {
  def run(): UIO[Unit]
}

object Server {
  val live: ZLayer[ServerConfig with OrderService, Nothing, Server] =
    ZLayer {
      for {
        config       <- ZIO.service[ServerConfig]
        orderService <- ZIO.service[OrderService]
      } yield new ServerLive(config, orderService)
    }
}
