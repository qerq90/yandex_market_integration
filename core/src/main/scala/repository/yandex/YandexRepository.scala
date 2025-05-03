package repository.yandex

import client.yandex.YandexClient
import model.user.User
import model.yandex.OfferMapping
import zio.{Task, ZLayer}

trait YandexRepository {
  def getOfferMapping(user: User): Task[List[OfferMapping]]
}

object YandexRepository {
  val live: ZLayer[YandexClient, Nothing, YandexRepository] =
    ZLayer.fromFunction(new YandexRepositoryLive(_))
}
