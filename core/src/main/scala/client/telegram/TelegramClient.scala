package client.telegram

import com.bot4s.telegram.clients.SttpClient
import model.config.TelegramConfig
import org.asynchttpclient.Dsl.asyncHttpClient
import sttp.client3.SttpBackend
import sttp.client3.asynchttpclient.zio.{
  AsyncHttpClientZioBackend,
  ZioWebSocketsStreams
}
import zio.interop.catz._
import zio.{Task, ZIO, ZLayer}

trait TelegramClient {
  def sendMessage(telegramId: Int, message: String): Task[Unit]
}

object TelegramClient {
  val live: ZLayer[TelegramConfig, Nothing, TelegramClient] = {
    implicit val sttpBackend: SttpBackend[Task, ZioWebSocketsStreams] =
      AsyncHttpClientZioBackend.usingClient(
        zio.Runtime.default,
        asyncHttpClient()
      )

    ZLayer.fromZIO(for {
      config <- ZIO.service[TelegramConfig]
      sttpClient = new SttpClient[Task](config.token, config.host)
    } yield new TelegramClientLive(sttpClient))
  }
}
