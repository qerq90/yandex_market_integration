package model.order

import cats.implicits._
import doobie.util.meta.Meta
import doobie.postgres.circe.json.implicits._
import io.circe.generic.semiauto._
import io.circe.syntax._
import io.circe._
import model.order.OrderData.Item

case class OrderData(
  isLocal: Option[Boolean],
  items: List[Item]
)

object OrderData {
  implicit val encoder: Encoder[OrderData] = deriveEncoder[OrderData]
  implicit val decoder: Decoder[OrderData] = deriveDecoder[OrderData]

  case class Item(
    count: Int,
    offerId: String,
    name: Option[String] = None
  )

  object Item {
    implicit val encoder: Encoder[Item] = deriveEncoder[Item]
    implicit val decoder: Decoder[Item] = deriveDecoder[Item]
  }

  implicit val meta: Meta[OrderData] = new Meta(
    jsonGet.temap(_.as[OrderData].leftMap(_.message)),
    jsonPut.tcontramap[OrderData](_.asJson)
  )
}
