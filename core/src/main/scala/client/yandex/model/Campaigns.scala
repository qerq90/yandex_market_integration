package client.yandex.model

import io.circe._
import io.circe.generic.semiauto._

case class Campaigns(
  campaigns: List[Campaign]
)

object Campaigns {
  implicit val decoder: Decoder[Campaigns] = deriveDecoder[Campaigns]
  implicit val encoder: Encoder[Campaigns] = deriveEncoder[Campaigns]
}

case class Campaign(
  business: Business,
  clientId: Int,
  domain: String,
  id: Int,
  placementType: String
)

object Campaign {
  implicit val decoder: Decoder[Campaign] = deriveDecoder[Campaign]
  implicit val encoder: Encoder[Campaign] = deriveEncoder[Campaign]
}

case class Business(
  id: Int,
  name: String
)

object Business {
  implicit val decoder: Decoder[Business] = deriveDecoder[Business]
  implicit val encoder: Encoder[Business] = deriveEncoder[Business]
}
