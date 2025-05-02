package client.telegram

import com.bot4s.telegram.clients.SttpClient
import com.bot4s.telegram.methods.SendMessage
import zio.Task

class TelegramClientLive(client: SttpClient[Task]) extends TelegramClient {

  override def sendMessage(telegramId: Int, message: String): Task[Unit] =
    client(
      SendMessage(
        chatId = telegramId,
        text = message
      )
    ).unit
}
