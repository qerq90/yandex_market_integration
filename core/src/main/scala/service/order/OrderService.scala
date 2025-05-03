package service.order

import dao.order.OrderDao
import dao.user.UserDao
import io.circe.Json
import model.order.Order
import repository.yandex.YandexRepository
import zio.{Task, ZLayer}

trait OrderService {
  def saveOrder(order: Json): Task[Unit]
  def enrichOrders(orders: List[Order]): Task[Unit]
}

object OrderService {
  val live: ZLayer[
    OrderDao with UserDao with YandexRepository,
    Nothing,
    OrderService
  ] =
    ZLayer.fromFunction(new OrderServiceLive(_, _, _))
}
