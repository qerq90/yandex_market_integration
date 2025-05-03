package client.yandex

import client.Client
import client.yandex.model.{Campaigns, OfferMappingResponse}
import zio.{Task, ZLayer}

trait YandexClient {
  def getOffers(businessId: Int, token: String): Task[OfferMappingResponse]

  def getBusinessIds(token: String): Task[Campaigns]
}

object YandexClient {
  val live: ZLayer[Any, Throwable, YandexClient] =
    ZLayer.fromZIO(Client.make) >>> ZLayer.fromFunction(new YandexClientLive(_))
}
