package dao.order

import dao.BaseDao
import doobie.implicits._
import doobie.postgres.implicits._
import io.circe.syntax._
import model.order.Order
import zio.Task

class OrderDaoLive(dao: BaseDao) extends OrderDao {

  override def saveOrder(order: Order): Task[Unit] =
    dao
      .query(
        sql"insert into orders(campaignId, orderId, status, createdAt, data) values (${order.campaignId}, ${order.orderId}, ${order.status}, ${order.createdAt}, ${order.data.asJson.noSpaces}::jsonb)".update.run
      )
      .unit

  override def getOrder(campaignId: Int, orderId: Int): Task[Option[Order]] =
    dao.query(
      sql"select * from orders where campaignId = $campaignId and orderId = $orderId"
        .query[Order]
        .option
    )

  override def getOrdersByStatus(
      campaignId: Int,
      status: String
  ): Task[List[Order]] =
    dao.query(
      sql"select * from orders where campaignId = $campaignId and status = $status"
        .query[Order]
        .to[List]
    )
}
