package dao.order

import dao.BaseDao
import model.order.{Order, Status}
import zio.{Task, ZLayer}

trait OrderDao {
  def saveOrder(order: Order): Task[Unit]
  def getOrder(campaignId: Int, orderId: Int): Task[Option[Order]]
  def getOrdersByStatus(status: Status): Task[List[Order]]
  def changeStatus(orders: List[Order], status: Status): Task[Unit]
  def enrichOrders(enrichedOrders: List[Order]): Task[Unit]
}

object OrderDao {
  val live: ZLayer[BaseDao, Nothing, OrderDao] =
    ZLayer.fromFunction(new OrderDaoLive(_))
}
