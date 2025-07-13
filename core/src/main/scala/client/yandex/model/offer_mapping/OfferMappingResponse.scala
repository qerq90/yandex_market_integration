package client.yandex.model.offer_mapping

import io.circe._
import io.circe.generic.semiauto._

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Основная структура ответа
case class OfferMappingResponse(
  status: String,
  result: OfferMappingResult
)
object OfferMappingResponse {
  implicit val decoder: Decoder[OfferMappingResponse] =
    deriveDecoder[OfferMappingResponse]
  implicit val encoder: Encoder[OfferMappingResponse] =
    deriveEncoder[OfferMappingResponse]
}

// Результат с пагинацией и списком маппингов офферов
case class OfferMappingResult(
  paging: Paging,
  offerMappings: List[OfferMapping]
)
object OfferMappingResult {
  implicit val decoder: Decoder[OfferMappingResult] = Decoder.instance { c =>
    for {
      paging <- c.get[Paging]("paging")
      offerMappings <- c.getOrElse[List[OfferMapping]]("offerMappings")(
        List.empty
      )
    } yield OfferMappingResult(paging, offerMappings)
  }
  implicit val encoder: Encoder[OfferMappingResult] =
    deriveEncoder[OfferMappingResult]
}

// Пагинация
case class Paging(
  nextPageToken: Option[String],
  prevPageToken: Option[String]
)
object Paging {
  implicit val decoder: Decoder[Paging] =
    deriveDecoder[Paging]
  implicit val encoder: Encoder[Paging] =
    deriveEncoder[Paging]
}

// Маппинг оффера
case class OfferMapping(
  offer: Offer,
  mapping: Mapping
)
object OfferMapping {
  implicit val decoder: Decoder[OfferMapping] = deriveDecoder[OfferMapping]
  implicit val encoder: Encoder[OfferMapping] = deriveEncoder[OfferMapping]
}

// Оффер
case class Offer(
  offerId: String,
  name: String,
  marketCategoryId: Option[Long],
  pictures: List[String],
  videos: List[String],
  manuals: List[Manual],
  vendor: String,
  barcodes: List[String],
  description: String,
  manufacturerCountries: List[String],
  weightDimensions: WeightDimensions,
  tags: List[String],
  shelfLife: Option[TimePeriod],
  lifeTime: Option[TimePeriod],
  guaranteePeriod: Option[TimePeriod],
  commodityCodes: List[CommodityCode],
  certificates: List[String],
  boxCount: Option[Int],
  condition: Option[Condition],
  downloadable: Option[Boolean],
  adult: Option[Boolean],
  age: Option[Age],
  basicPrice: Price,
  purchasePrice: Option[Price],
  additionalExpenses: Option[Price],
  cofinancePrice: Option[Price],
  cardStatus: String,
  campaigns: List[OfferCampaign],
  sellingPrograms: List[SellingProgram],
  mediaFiles: MediaFiles,
  archived: Option[Boolean]
)
object Offer {
  implicit val decoder: Decoder[Offer] = Decoder.instance { c =>
    for {
      offerId          <- c.get[String]("offerId")
      name             <- c.get[String]("name")
      marketCategoryId <- c.get[Option[Long]]("marketCategoryId")
      pictures         <- c.getOrElse[List[String]]("pictures")(List.empty)
      videos           <- c.getOrElse[List[String]]("videos")(List.empty)
      manuals          <- c.getOrElse[List[Manual]]("manuals")(List.empty)
      vendor           <- c.get[String]("vendor")
      barcodes         <- c.getOrElse[List[String]]("barcodes")(List.empty)
      description      <- c.get[String]("description")
      manufacturerCountries <- c.getOrElse[List[String]](
        "manufacturerCountries"
      )(List.empty)
      weightDimensions <- c.get[WeightDimensions]("weightDimensions")
      tags             <- c.getOrElse[List[String]]("tags")(List.empty)
      shelfLife        <- c.get[Option[TimePeriod]]("shelfLife")
      lifeTime         <- c.get[Option[TimePeriod]]("lifeTime")
      guaranteePeriod  <- c.get[Option[TimePeriod]]("guaranteePeriod")
      commodityCodes <- c.getOrElse[List[CommodityCode]]("commodityCodes")(
        List.empty
      )
      certificates  <- c.getOrElse[List[String]]("certificates")(List.empty)
      boxCount      <- c.get[Option[Int]]("boxCount")
      condition     <- c.get[Option[Condition]]("condition")
      downloadable  <- c.get[Option[Boolean]]("downloadable")
      adult         <- c.get[Option[Boolean]]("adult")
      age           <- c.get[Option[Age]]("age")
      basicPrice    <- c.get[Price]("basicPrice")
      purchasePrice <- c.get[Option[Price]]("purchasePrice")
      additionalExpenses <- c.get[Option[Price]](
        "additionalExpenses"
      )
      cofinancePrice <- c.get[Option[Price]]("cofinancePrice")
      cardStatus     <- c.get[String]("cardStatus")
      campaigns <- c.getOrElse[List[OfferCampaign]]("campaigns")(List.empty)
      sellingPrograms <- c.getOrElse[List[SellingProgram]]("sellingPrograms")(
        List.empty
      )
      mediaFiles <- c.get[MediaFiles]("mediaFiles")
      archived   <- c.get[Option[Boolean]]("archived")
    } yield Offer(
      offerId,
      name,
      marketCategoryId,
      pictures,
      videos,
      manuals,
      vendor,
      barcodes,
      description,
      manufacturerCountries,
      weightDimensions,
      tags,
      shelfLife,
      lifeTime,
      guaranteePeriod,
      commodityCodes,
      certificates,
      boxCount,
      condition,
      downloadable,
      adult,
      age,
      basicPrice,
      purchasePrice,
      additionalExpenses,
      cofinancePrice,
      cardStatus,
      campaigns,
      sellingPrograms,
      mediaFiles,
      archived
    )
  }
  implicit val encoder: Encoder[Offer] = deriveEncoder[Offer]
}

// Руководство
case class Manual(
  url: String,
  title: String
)
object Manual {
  implicit val decoder: Decoder[Manual] = deriveDecoder[Manual]
  implicit val encoder: Encoder[Manual] = deriveEncoder[Manual]
}

// Размеры и вес
case class WeightDimensions(
  length: Double,
  width: Double,
  height: Double,
  weight: Double
)
object WeightDimensions {
  implicit val decoder: Decoder[WeightDimensions] =
    deriveDecoder[WeightDimensions]
  implicit val encoder: Encoder[WeightDimensions] =
    deriveEncoder[WeightDimensions]
}

// Период времени (срок годности, жизни, гарантии)
case class TimePeriod(
  timePeriod: Int,
  timeUnit: String,
  comment: Option[String]
)
object TimePeriod {
  implicit val decoder: Decoder[TimePeriod] = deriveDecoder[TimePeriod]
  implicit val encoder: Encoder[TimePeriod] = deriveEncoder[TimePeriod]
}

// Код товара
case class CommodityCode(
  code: String,
  `type`: String
)
object CommodityCode {
  implicit val decoder: Decoder[CommodityCode] = deriveDecoder[CommodityCode]
  implicit val encoder: Encoder[CommodityCode] = deriveEncoder[CommodityCode]
}

// Состояние товара
case class Condition(
  `type`: String,
  quality: String,
  reason: String
)
object Condition {
  implicit val decoder: Decoder[Condition] = deriveDecoder[Condition]
  implicit val encoder: Encoder[Condition] = deriveEncoder[Condition]
}

// Возраст
case class Age(
  value: Int,
  ageUnit: String
)
object Age {
  implicit val decoder: Decoder[Age] = deriveDecoder[Age]
  implicit val encoder: Encoder[Age] = deriveEncoder[Age]
}

// Параметр
case class Param(
  name: String,
  value: String
)
object Param {
  implicit val decoder: Decoder[Param] = deriveDecoder[Param]
  implicit val encoder: Encoder[Param] = deriveEncoder[Param]
}

// Цена
case class Price(
  value: Double,
  currencyId: String,
  discountBase: Option[Double],
  updatedAt: LocalDateTime
)
object Price {
  implicit val localDateTimeDecoder: Decoder[LocalDateTime] =
    Decoder.decodeString.emap { str =>
      try
        Right(LocalDateTime.parse(str, DateTimeFormatter.ISO_ZONED_DATE_TIME))
      catch {
        case e: Exception =>
          Left(s"Failed to parse LocalDateTime: ${e.getMessage}")
      }
    }
  implicit val localDateTimeEncoder: Encoder[LocalDateTime] =
    Encoder.encodeString.contramap(
      _.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    )

  implicit val decoder: Decoder[Price] =
    deriveDecoder[Price]
  implicit val encoder: Encoder[Price] =
    deriveEncoder[Price]
}

// Кампания
case class OfferCampaign(
  campaignId: Long,
  status: String
)

object OfferCampaign {
  implicit val decoder: Decoder[OfferCampaign] = deriveDecoder[OfferCampaign]
  implicit val encoder: Encoder[OfferCampaign] = deriveEncoder[OfferCampaign]
}

// Программа продаж
case class SellingProgram(
  sellingProgram: String,
  status: String
)
object SellingProgram {
  implicit val decoder: Decoder[SellingProgram] = deriveDecoder[SellingProgram]
  implicit val encoder: Encoder[SellingProgram] = deriveEncoder[SellingProgram]
}

// Медиафайлы
case class MediaFiles(
  firstVideoAsCover: Boolean,
  videos: List[MediaFile],
  pictures: List[MediaFile],
  manuals: List[MediaFile]
)
object MediaFiles {
  implicit val decoder: Decoder[MediaFiles] = Decoder.instance { c =>
    for {
      firstVideoAsCover <- c.get[Boolean]("firstVideoAsCover")
      videos            <- c.getOrElse[List[MediaFile]]("videos")(List.empty)
      pictures          <- c.getOrElse[List[MediaFile]]("pictures")(List.empty)
      manuals           <- c.getOrElse[List[MediaFile]]("manuals")(List.empty)
    } yield MediaFiles(firstVideoAsCover, videos, pictures, manuals)
  }
  implicit val encoder: Encoder[MediaFiles] = deriveEncoder[MediaFiles]
}

// Медиафайл
case class MediaFile(
  url: String,
  title: Option[String],
  uploadState: String
)
object MediaFile {
  implicit val decoder: Decoder[MediaFile] = deriveDecoder[MediaFile]
  implicit val encoder: Encoder[MediaFile] = deriveEncoder[MediaFile]
}

// Маппинг
case class Mapping(
  marketSku: Long,
  marketSkuName: String,
  marketModelId: Long,
  marketModelName: String,
  marketCategoryId: Long,
  marketCategoryName: String
)
object Mapping {
  implicit val decoder: Decoder[Mapping] = deriveDecoder[Mapping]
  implicit val encoder: Encoder[Mapping] = deriveEncoder[Mapping]
}
