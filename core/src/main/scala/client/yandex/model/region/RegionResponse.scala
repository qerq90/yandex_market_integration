package client.yandex.model.region

import io.circe.{Decoder, HCursor}
import io.circe.generic.semiauto._

// Root level
case class RegionResponse(regions: List[Region])
object Response {
  implicit val decoder: Decoder[RegionResponse] = deriveDecoder[RegionResponse]
}

// Region details (recursive)
case class Region(
  id: Int,
  name: String,
  `type`: String,
  parent: Option[Region],
  children: List[Option[Region]]
)
object Region {
  implicit val decoder: Decoder[Region] = deriveDecoder[Region]
}
