package task.notification

import dao.order.OrderDao
import model.order.Status
import model.order.Status.Finished
import service.notification.NotificationService
import task.Scheduler
import zio.{durationInt, Duration, Task, ZIO, ZLayer}

class NotificationTask(
  notificationService: NotificationService,
  orderDao: OrderDao
) extends Scheduler {

  override val interval: Duration = 10.seconds

  override def task: Task[Unit] =
    for {
      orders <- orderDao.getOrdersByStatus(Status.Enriched)
      _      <- ZIO.log(s"Orders to process: $orders")
      _ <- ZIO.when(orders.nonEmpty)(
        notificationService
          .notifyUsers(orders) *> orderDao.changeStatus(orders, Finished)
      )
    } yield ()
}

object NotificationTask {
  val live
      : ZLayer[NotificationService with OrderDao, Nothing, NotificationTask] =
    ZLayer.fromFunction(new NotificationTask(_, _))
}
