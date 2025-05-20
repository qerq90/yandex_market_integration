package model.order

import model.order.OrderData.Item
import model.yandex.OrderCreated

import java.time.{LocalDateTime, ZoneId, ZonedDateTime}
import scala.util.Try

case class Order(
  campaignId: Long,
  orderId: Long,
  status: Status,
  createdAt: LocalDateTime,
  data: OrderData
)

object Order {
  def fromOrderCreated(orderCreated: OrderCreated): Option[Order] =
    Try(ZonedDateTime.parse(orderCreated.createdAt)).toOption.map(createdAt =>
      Order(
        orderId = orderCreated.orderId,
        campaignId = orderCreated.campaignId,
        status = Status.Created,
        createdAt = createdAt
          .withZoneSameInstant(ZoneId.of("Europe/Moscow"))
          .toLocalDateTime,
        data = OrderData(
          None,
          orderCreated.items.map(item => Item(item.count, item.offerId))
        )
      )
    )
}
