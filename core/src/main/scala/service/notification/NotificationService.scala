package service.notification

import client.telegram.TelegramClient
import dao.user.UserDao
import model.order.Order
import zio.{Task, ZLayer}

trait NotificationService {
  def notifyUsers(orders: List[Order]): Task[Unit]
}

object NotificationService {
  val live: ZLayer[UserDao with TelegramClient, Nothing, NotificationService] =
    ZLayer.fromFunction(new NotificationServiceLive(_, _))
}
