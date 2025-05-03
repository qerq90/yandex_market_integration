package repository.yandex

import client.yandex.YandexClient
import model.user.User
import model.yandex.OfferMapping
import zio.{Task, ZIO}

class YandexRepositoryLive(client: YandexClient) extends YandexRepository {
  override def getOfferMapping(user: User): Task[List[OfferMapping]] =
    for {
      businessIds <- client
        .getBusinessIds(user.token)
        .map(_.campaigns.map(_.business.id))

      offerResponses <- ZIO.foreachPar(businessIds)(
        client.getOffers(_, user.token)
      )
      resp = offerResponses.flatMap(
        _.result.offerMappings.map(offer =>
          OfferMapping(offer.offer.offerId, offer.offer.name)
        )
      )
    } yield resp

}
