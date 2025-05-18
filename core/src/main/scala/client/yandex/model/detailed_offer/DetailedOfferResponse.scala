package client.yandex.model.detailed_offer

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}

import java.time.ZonedDateTime
import scala.util.Try

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
  creationDate: String,
  statusUpdateDate: ZonedDateTime,
  status: String,
  partnerOrderId: String,
  paymentType: String,
  fake: Boolean,
  deliveryRegion: DeliveryRegion,
  items: List[Item],
  initialItems: Option[List[Item]],
  payments: List[Payment],
  commissions: List[Commission],
  subsidies: List[Subsidy],
  currency: String
)
object Order {
  implicit val decoder: Decoder[Order] = deriveDecoder[Order]
  implicit val zonedDateTimeDecoder: Decoder[ZonedDateTime] =
    Decoder.decodeString.emap(str =>
      Try(ZonedDateTime.parse(str)).toEither.left.map(_.getMessage)
    )
  implicit val zonedDateTimeEncoder: Encoder[ZonedDateTime] =
    Encoder.encodeString.contramap[ZonedDateTime](_.toString)
}

// Delivery region
case class DeliveryRegion(id: Long, name: String)
object DeliveryRegion {
  implicit val decoder: Decoder[DeliveryRegion] = deriveDecoder[DeliveryRegion]
}

// Item details
case class Item(
  offerName: String,
  marketSku: Long,
  shopSku: String,
  count: Int,
  prices: List[Price],
  warehouse: Warehouse,
  details: List[ItemDetail],
  cisList: List[String],
  initialCount: Option[Int],
  bidFee: Option[Int],
  cofinanceThreshold: Option[Int],
  cofinanceValue: Option[Int]
)
object Item {
  implicit val decoder: Decoder[Item] = deriveDecoder[Item]
}

// Price details
case class Price(`type`: String, costPerItem: Int, total: Int)
object Price {
  implicit val decoder: Decoder[Price] = deriveDecoder[Price]
}

// Warehouse details
case class Warehouse(id: Int, name: String)
object Warehouse {
  implicit val decoder: Decoder[Warehouse] = deriveDecoder[Warehouse]
}

// Item detail
case class ItemDetail(
  itemStatus: String,
  itemCount: Int,
  updateDate: String,
  stockType: String
)
object ItemDetail {
  implicit val decoder: Decoder[ItemDetail] = deriveDecoder[ItemDetail]
}

// Payment details
case class Payment(
  id: String,
  date: String,
  `type`: String,
  source: String,
  total: Int,
  paymentOrder: Option[PaymentOrder]
)
object Payment {
  implicit val decoder: Decoder[Payment] = deriveDecoder[Payment]
}

// Payment order
case class PaymentOrder(id: String, date: String)
object PaymentOrder {
  implicit val decoder: Decoder[PaymentOrder] = deriveDecoder[PaymentOrder]
}

// Commission details
case class Commission(`type`: String, actual: Double)
object Commission {
  implicit val decoder: Decoder[Commission] = deriveDecoder[Commission]
}

// Subsidy details
case class Subsidy(operationType: String, `type`: String, amount: Int)
object Subsidy {
  implicit val decoder: Decoder[Subsidy] = deriveDecoder[Subsidy]
}
