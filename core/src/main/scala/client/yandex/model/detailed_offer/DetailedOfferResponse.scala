package client.yandex.model.detailed_offer

import io.circe.generic.semiauto._
import io.circe.Decoder

// Root level
case class DetailedOfferResponse(status: String, result: Result)

object DetailedOfferResponse {
  implicit val decoder: Decoder[DetailedOfferResponse] =
    deriveDecoder[DetailedOfferResponse]
}

// Result containing orders and paging
case class Result(orders: List[Order], paging: Paging)
object Result {
  implicit val decoder: Decoder[Result] = deriveDecoder[Result]
}

// Paging information
case class Paging(nextPageToken: Option[String])
object Paging {
  implicit val decoder: Decoder[Paging] = deriveDecoder[Paging]
}

// Order details
case class Order(
  id: Long,
  deliveryRegion: DeliveryRegion,
  items: List[Item],
  initialItems: Option[List[Item]]
)
object Order {
  implicit val decoder: Decoder[Order] = deriveDecoder[Order]
}

// Delivery region
case class DeliveryRegion(id: Long, name: String)
object DeliveryRegion {
  implicit val decoder: Decoder[DeliveryRegion] = deriveDecoder[DeliveryRegion]
}

// Item details
case class Item(
  offerName: String,
  count: Int,
  warehouse: Warehouse
)
object Item {
  implicit val decoder: Decoder[Item] = deriveDecoder[Item]
}

// Warehouse details
case class Warehouse(id: Int, name: String)
object Warehouse {
  implicit val decoder: Decoder[Warehouse] = deriveDecoder[Warehouse]
}
