package services.order

import dao.order.OrderDao
import io.circe.Json
import zio.{Task, ZLayer}

trait OrderService {
  def saveOrder(order: Json): Task[Unit]
}

object OrderService {
  val live: ZLayer[OrderDao, Nothing, OrderServiceLive] =
    ZLayer.fromFunction(new OrderServiceLive(_))
}
