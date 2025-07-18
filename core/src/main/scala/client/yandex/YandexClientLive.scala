package client.yandex

import client.yandex.model.campaigns.Campaigns
import client.yandex.model.detailed_offer.DetailedOfferResponse
import client.yandex.model.offer_mapping.OfferMappingResponse
import client.yandex.model.region.RegionResponse
import io.circe.generic.auto._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.client.Client
import org.http4s.implicits.http4sLiteralsSyntax
import org.http4s.{Header, Headers, Method, Request}
import org.typelevel.ci.CIStringSyntax
import zio.{Task, ZIO}
import zio.interop.catz._

class YandexClientLive(client: Client[Task]) extends YandexClient {

  private val uri = uri"https://api.partner.market.yandex.ru"

  override def getOffers(
      businessId: Int,
      token: String
  ): Task[OfferMappingResponse] = {
    val request = Request[Task](
      method = Method.POST,
      uri = uri / "businesses" / businessId / "offer-mappings",
      headers = Headers(
        Header.Raw(ci"Api-Key", token)
      )
    )

    client
      .expect[OfferMappingResponse](request)
      .tapError(err => ZIO.logError(s"ERROR: ${err.getCause}"))
  }

  override def getBusinessIds(
      token: String
  ): Task[Campaigns] = {
    val request = Request[Task](
      method = Method.GET,
      uri = uri / "campaigns",
      headers = Headers(
        Header.Raw(ci"Api-Key", token)
      )
    )

    client.expect[Campaigns](request)
  }

  override def getDetailedOrderInfo(
      token: String,
      campaigns: Long,
      orderIds: List[Long]
  ): Task[DetailedOfferResponse] = {
    case class GetDetailedOrderRequest(orders: List[Long])

    val request = Request[Task](
      method = Method.POST,
      uri = uri / "campaigns" / campaigns / "stats" / "orders",
      headers = Headers(
        Header.Raw(ci"Api-Key", token)
      )
    ).withEntity(GetDetailedOrderRequest(orderIds))

    client.expect[DetailedOfferResponse](request)
  }

  override def getRegionInfo(
      token: String,
      regionId: Long
  ): Task[RegionResponse] = {
    val request = Request[Task](
      method = Method.GET,
      uri = uri / "regions" / regionId,
      headers = Headers(
        Header.Raw(ci"Api-Key", token)
      )
    )

    client.expect[RegionResponse](request)
  }

}
