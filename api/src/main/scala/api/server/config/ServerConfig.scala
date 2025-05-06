package api.server.config

import zio._
import pureconfig._
import pureconfig.generic.auto._
import pureconfig.module.ip4s._
import com.comcast.ip4s.{Host, Port}

case class ServerConfig(host: Host, port: Port)

object ServerConfig {
  private val loadConfig =
    ZIO.attempt(ConfigSource.default.at("server").loadOrThrow[ServerConfig])

  val live: ZLayer[Any, Throwable, ServerConfig] =
    ZLayer.fromZIO(loadConfig)
}
