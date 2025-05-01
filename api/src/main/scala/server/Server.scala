package server

import server.config.ServerConfig
import server.ServerLive
import zio._

trait Server {
  def run(): ZIO[Any, Throwable, Unit]
}

object Server {
  val live: ZLayer[ServerConfig, Nothing, Server] =
    ZLayer {
      for {
        config         <- ZIO.service[ServerConfig]
      } yield new ServerLive(config)
    }
}
