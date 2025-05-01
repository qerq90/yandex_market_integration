package model.yandex

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import model.yandex.OrderCreated.Item

case class OrderCreated(
  campaignId: Int,
  createdAt: String,
  items: List[Item],
  orderId: Int
)

object OrderCreated {
  implicit val decoder: Decoder[OrderCreated] = deriveDecoder[OrderCreated]

  case class Item(
    count: Int,
    offerId: String
  )

  object Item {
    implicit val decoder: Decoder[Item] = deriveDecoder[Item]
  }
}
