package client.yandex

import client.Client
import client.yandex.model.detailed_offer.DetailedOfferResponse
import client.yandex.model.campaigns.Campaigns
import client.yandex.model.offer_mapping.OfferMappingResponse
import client.yandex.model.region.RegionResponse
import zio.{Task, ZLayer}

trait YandexClient {
  def getOffers(businessId: Int, token: String): Task[OfferMappingResponse]

  def getBusinessIds(token: String): Task[Campaigns]
  def getDetailedOrderInfo(
      token: String,
      campaignId: Long,
      orderIds: List[Long]
  ): Task[DetailedOfferResponse]

  def getRegionInfo(token: String, regionId: Long): Task[RegionResponse]
}

object YandexClient {
  val live: ZLayer[Any, Throwable, YandexClient] =
    ZLayer.fromZIO(Client.make) >>> ZLayer.fromFunction(new YandexClientLive(_))
}
