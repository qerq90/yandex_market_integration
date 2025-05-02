package model.order

import io.circe.Decoder
import io.circe.generic.semiauto._
import model.order.OrderData.Item
import model.yandex.OrderCreated

import java.time.LocalDateTime
import scala.util.Try

case class Order(
  campaignId: Int,
  orderId: Int,
  status: Status,
  createdAt: LocalDateTime,
  data: OrderData
)

object Order {
  def fromOrderCreated(orderCreated: OrderCreated): Option[Order] =
    Try(LocalDateTime.parse(orderCreated.createdAt)).toOption.map(createdAt =>
      Order(
        orderId = orderCreated.orderId,
        campaignId = orderCreated.campaignId,
        status = Status.Created,
        createdAt = createdAt,
        data = OrderData(
          orderCreated.items.map(item => Item(item.count, item.offerId))
        )
      )
    )
}
