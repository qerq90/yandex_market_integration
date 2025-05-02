package task.notification

import dao.order.OrderDao
import model.order.Status
import service.notification.NotificationService
import task.Scheduler
import zio.{durationInt, Duration, Task, ZLayer}

class NotificationTask(
  notificationService: NotificationService,
  orderDao: OrderDao
) extends Scheduler {

  override val interval: Duration = 10.seconds

  override def task: Task[Unit] =
    for {
      orders <- orderDao.getOrdersByStatus(Status.Enriched)
      _      <- notificationService.notifyUsers(orders)
      _      <- orderDao.changeStatus(orders, Status.Finished)
    } yield ()
}

object NotificationTask {
  val live
      : ZLayer[NotificationService with OrderDao, Nothing, NotificationTask] =
    ZLayer.fromFunction(new NotificationTask(_, _))
}
