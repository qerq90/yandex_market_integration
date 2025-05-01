package services.notification

import dao.user.UserDao
import model.order.Order
import model.user.User
import zio.{Task, ZIO}

class NotificationServiceLive(userDao: UserDao) extends NotificationService {

  override def notifyUsers(orders: List[Order]): Task[Unit] = {
    val campaignIds = orders.map(_.campaignId).distinct

    for {
      users <- userDao.getUsers(campaignIds)
      _     <- ZIO.foreachParDiscard(users)(sendNotification(_, orders))
    } yield ()
  }

  private def sendNotification(user: User, orders: List[Order]): Task[Unit] = {
    val ordersToNotify = orders.filter(_.campaignId == user.campaignId)
    ??? // use telegram client ot send notification
  }

}
