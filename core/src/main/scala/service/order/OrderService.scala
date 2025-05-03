package service.order

import dao.order.OrderDao
import io.circe.Json
import model.order.Order
import zio.{Task, ZLayer}

trait OrderService {
  def saveOrder(order: Json): Task[Unit]
  def enrichOrder(order: Order): Task[Order]
}

object OrderService {
  val live: ZLayer[OrderDao, Nothing, OrderService] =
    ZLayer.fromFunction(new OrderServiceLive(_))
}
