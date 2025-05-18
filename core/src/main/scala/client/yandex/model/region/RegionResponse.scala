package client.yandex.model.region

import io.circe.{Decoder, HCursor}
import io.circe.generic.semiauto._

// Root level
case class RegionResponse(regions: List[Region])
object Response {
  implicit val decoder: Decoder[RegionResponse] = deriveDecoder[RegionResponse]
}

// "regions" : [
//    {
//      "id" : 2,
//      "name" : "Санкт-Петербург",
//      "type" : "CITY",
//      "parent" : {
//        "id" : 10174,
//        "name" : "Санкт-Петербург и Ленинградская область",
//        "type" : "REPUBLIC",
//        "parent" : {
//          "id" : 17,
//          "name" : "Северо-Западный федеральный округ",
//          "type" : "COUNTRY_DISTRICT",
//          "parent" : {
//            "id" : 225,
//            "name" : "Россия",
//            "type" : "COUNTRY"
//          }
//        }
//      }
//    }
//  ]
//}

// Region details (recursive)
case class Region(
  id: Int,
  name: String,
  `type`: String,
  parent: Option[Region],
  children: Option[List[Option[Region]]]
)
object Region {
  implicit val decoder: Decoder[Region] = deriveDecoder[Region]
}
