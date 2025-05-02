package service.notification

import client.telegram.TelegramClient
import dao.user.UserDao
import model.order.Order
import model.user.User
import zio.{Task, ZIO}

import java.time.format.DateTimeFormatter

class NotificationServiceLive(userDao: UserDao, telegramClient: TelegramClient)
    extends NotificationService {

  override def notifyUsers(orders: List[Order]): Task[Unit] = {
    val campaignIds = orders.map(_.campaignId).distinct

    for {
      users <- userDao.getUsers(campaignIds)
      _     <- ZIO.foreachParDiscard(users)(sendNotification(_, orders))
    } yield ()
  }

  private def sendNotification(user: User, orders: List[Order]): Task[Unit] = {
    val ordersToNotify = orders.filter(_.campaignId == user.campaignId)
    telegramClient.sendMessage(
      user.telegramId,
      createOrderNotification(ordersToNotify)
    )
  }

  private def createOrderNotification(orders: List[Order]): String = {
    if (orders.isEmpty) {
      return "Нет новых заказов."
    }

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    val orderMessages = orders.map { order =>
      val itemsSummary = order.data.items
        .map(item => s"- ${item.count} x ${item.offerId}")
        .mkString("\n")

      s"""
         |Заказ #${order.orderId}
         |Создан: ${order.createdAt.format(formatter)}
         |Товары:
         |$itemsSummary
         |""".stripMargin
    }

    s"""
       |Поступили новые заказы:
       |${"-" * 30}
       |${orderMessages.mkString(s"${"-" * 30}\n")}
       |${"-" * 30}
       |Всего заказов: ${orders.length}
       |""".stripMargin
  }

}
