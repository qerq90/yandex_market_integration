package client.yandex

import client.yandex.model.{Campaigns, OfferMappingResponse}
import org.http4s.circe.CirceEntityCodec._
import org.http4s.client.Client
import org.http4s.implicits.http4sLiteralsSyntax
import org.http4s.{Header, Headers, Method, Request}
import org.typelevel.ci.CIStringSyntax
import zio.Task
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

    client.expect[OfferMappingResponse](request)
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
}
