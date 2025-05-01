package model.order

import java.time.LocalDateTime

case class Order(
  orderId: Int,
  campaignId: Int,
  createdAt: LocalDateTime,
  data: OrderData
)
