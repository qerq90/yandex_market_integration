package services.order

import dao.order.OrderDao
import io.circe.Json
import model.order.Order
import zio.{Task, ZIO}

class OrderServiceLive(orderDao: OrderDao) extends OrderService {

  override def saveOrder(orderJson: Json): Task[Unit] =
    for {
      order <- ZIO.fromEither(orderJson.as[Order])
      _     <- orderDao.saveOrder(order)
    } yield ()
}
