package dao.order

import dao.BaseDao
import doobie.implicits._
import doobie.postgres.implicits._
import io.circe.syntax._
import model.order.{Order, Status}
import zio.Task

class OrderDaoLive(dao: BaseDao) extends OrderDao {

  override def saveOrder(order: Order): Task[Unit] =
    dao
      .query(
        sql"insert into orders(campaignId, orderId, status, createdAt, data) values (${order.campaignId}, ${order.orderId}, ${order.status.entryName}, ${order.createdAt}, ${order.data.asJson.noSpaces}::jsonb)".update.run
      )
      .unit

  override def getOrder(campaignId: Int, orderId: Int): Task[Option[Order]] =
    dao.query(
      sql"select * from orders where campaignId = $campaignId and orderId = $orderId"
        .query[Order]
        .option
    )

  override def getOrdersByStatus(
      status: Status
  ): Task[List[Order]] =
    dao.query(
      sql"select * from orders where status = ${status.entryName}"
        .query[Order]
        .to[List]
    )

  override def changeStatus(orders: List[Order], status: Status): Task[Unit] =
    dao
      .query(
        sql"update orders where orderId in (${orders.map(_.orderId).mkString(",")}) set status = ${status.entryName}".update.run
      )
      .unit
}
