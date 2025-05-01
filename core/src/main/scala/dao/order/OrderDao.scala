package dao.order

import dao.BaseDao
import model.order.Order
import zio.{Task, ZLayer}

import java.util.UUID

trait OrderDao {
  def saveOrder(order: Order): Task[Unit]
  def getOrder(campaignId: Int, orderId: Int): Task[Option[Order]]
}

object OrderDao {
  val live: ZLayer[BaseDao, Nothing, OrderDaoLive] =
    ZLayer.fromFunction(new OrderDaoLive(_))
}
