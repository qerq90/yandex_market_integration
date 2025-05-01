package services.order

import dao.order.OrderDao
import io.circe.Json
import model.order.Order
import model.yandex.OrderCreated
import zio.{Task, ZIO}

class OrderServiceLive(orderDao: OrderDao) extends OrderService {

  override def saveOrder(orderJson: Json): Task[Unit] =
    for {
      orderCreated <- ZIO.fromEither(orderJson.as[OrderCreated])
      order <- ZIO
        .fromOption(Order.fromOrderCreated(orderCreated))
        .orElseFail(new Throwable("Can't transform orderCreated to order"))
      _ <- orderDao.saveOrder(order)
    } yield ()
}
