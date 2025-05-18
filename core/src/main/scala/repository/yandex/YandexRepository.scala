package repository.yandex

import client.yandex.YandexClient
import model.user.User
import model.yandex.{DetailedOrder, OfferMapping}
import zio.{Task, ZLayer}

trait YandexRepository {
  def getOfferMapping(user: User): Task[List[OfferMapping]]
  def getDetailedOrders(user: User, orders: List[Long]): Task[List[DetailedOrder]]
}

object YandexRepository {
  val live: ZLayer[YandexClient, Nothing, YandexRepository] =
    ZLayer.fromFunction(new YandexRepositoryLive(_))
}
