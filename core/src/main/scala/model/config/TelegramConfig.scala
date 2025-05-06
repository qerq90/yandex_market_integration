package model.config

import pureconfig.generic.auto._
import pureconfig.ConfigSource
import zio.{ZIO, ZLayer}

case class TelegramConfig(
  token: String,
  host: String
)

object TelegramConfig {
  private val loadConfig =
    ZIO.attempt(
      ConfigSource.default.at("telegram").loadOrThrow[TelegramConfig]
    )

  val live: ZLayer[Any, Throwable, TelegramConfig] =
    ZLayer.fromZIO(loadConfig)
}
