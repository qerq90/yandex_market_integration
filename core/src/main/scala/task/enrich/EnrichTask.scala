package task.enrich

import dao.order.OrderDao
import dao.user.UserDao
import model.order.Order
import model.order.Status.{Created, Failed}
import model.user.User
import repository.yandex.YandexRepository
import service.order.OrderService
import task.Scheduler
import zio.{durationInt, Duration, Task, ZIO, ZLayer}

class EnrichTask(
  orderDao: OrderDao,
  orderService: OrderService
) extends Scheduler {

  override val interval: Duration = 10.seconds

  override def task: Task[Unit] =
    for {
      orders <- orderDao.getOrdersByStatus(Created)
      _      <- ZIO.log(s"Orders to enrich: $orders")
      _      <- ZIO.when(orders.nonEmpty)(orderService.enrichOrders(orders))
    } yield ()
}

object EnrichTask {
  val live: ZLayer[
    OrderDao with OrderService,
    Nothing,
    EnrichTask
  ] = ZLayer.fromFunction(new EnrichTask(_, _))
}
