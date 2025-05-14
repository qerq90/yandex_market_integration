package dao.order

import cats.data.NonEmptyList
import dao.BaseDao
import doobie.{Fragments, Update}
import doobie.implicits._
import doobie.postgres.implicits._
import io.circe.Json
import io.circe.syntax._
import model.order.{Order, Status}
import zio.{Task, ZIO}

class OrderDaoLive(dao: BaseDao) extends OrderDao {

  override def saveOrder(order: Order): Task[Unit] =
    dao
      .query(
        sql"insert into orders(campaign_id, order_id, status, created_at, data) values (${order.campaignId}, ${order.orderId}, ${order.status.entryName}, ${order.createdAt}, ${order.data.asJson.noSpaces}::jsonb)".update.run
      )
      .unit

  override def getOrder(campaignId: Long, orderId: Long): Task[Option[Order]] =
    dao.query(
      sql"select * from orders where campaign_id = $campaignId and order_id = $orderId"
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
    NonEmptyList.fromList(orders.map(_.orderId)) match {
      case None => ZIO.unit
      case Some(ids) =>
        dao
          .query(
            (sql"update orders set status = ${status.entryName} where " ++ Fragments
              .in(fr"order_id", ids)).update.run
          )
          .unit
    }

  override def enrichOrders(enrichedOrders: List[Order]): Task[Unit] =
    dao
      .query(
        Update[(String, Long)](
          s"update orders set data = ?::jsonb, status = '${Status.Enriched.entryName}' where order_id = ?"
        )
          .updateMany(
            enrichedOrders.map(order =>
              (order.data.asJson.noSpaces, order.orderId)
            )
          )
      )
      .unit

}
