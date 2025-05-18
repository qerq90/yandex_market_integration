package client.yandex.model.region

import io.circe.{Decoder, HCursor}
import io.circe.generic.semiauto._

// Root level
case class RegionResponse(regions: List[Region], paging: Paging)
object Response {
  implicit val decoder: Decoder[RegionResponse] = deriveDecoder[RegionResponse]
}

// Paging information
case class Paging(nextPageToken: String)
object Paging {
  implicit val decoder: Decoder[Paging] = deriveDecoder[Paging]
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
//  implicit val decoder: Decoder[Region] = (c: HCursor) =>
//    for {
//      id        <- c.downField("id").as[Int]
//      name      <- c.downField("name").as[String]
//      typeField <- c.downField("type").as[String]
//      parent    <- c.downField("parent").as[Option[Region]]
//      children  <- c.downField("children").as[List[Option[Region]]]
//    } yield Region(id, name, typeField, parent, children)

  implicit val decoder: Decoder[Region] = deriveDecoder[Region]
}
