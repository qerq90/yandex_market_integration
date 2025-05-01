package model.order

import io.circe.Decoder
import io.circe.generic.semiauto._

import java.time.LocalDateTime

case class Order(
  orderId: Int,
  campaignId: Int,
  createdAt: LocalDateTime,
  data: OrderData
)

object Order {
  implicit val decoder: Decoder[Order] = deriveDecoder[Order]
}
