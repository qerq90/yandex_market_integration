package repository.yandex

import client.yandex.YandexClient
import client.yandex.model.detailed_offer.{DeliveryRegion, Order}
import client.yandex.model.region.Region
import model.user.User
import model.yandex.warehouse.Mapping.{
  RegionCountryDistrictTag,
  WarehouseMapping
}
import model.yandex.{DetailedOrder, OfferMapping}
import zio.{Task, ZIO}

import scala.annotation.tailrec

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

  override def getDetailedOrders(
      user: User,
      orders: List[Long]
  ): Task[List[DetailedOrder]] =
    for {
      orderInfos <- client
        .getDetailedOrderInfo(user.token, user.campaignId, orders)
        .map(_.result.orders)

      detailedOrders <- ZIO.foreachPar(orderInfos)(makeDetailedOrder(user, _))

    } yield detailedOrders

  private def makeDetailedOrder(user: User, order: Order): Task[DetailedOrder] =
    client
      .getRegionInfo(user.token, order.deliveryRegion.id)
      .map(regionResp =>
        DetailedOrder(
          order.id,
          isLocal(
            WarehouseMapping(order.items.head.warehouse.id),
            regionResp.regions.head
          )
        )
      )

  @tailrec
  private def isLocal(warehouseRegionId: Int, region: Region): Boolean =
    region.`type` match {
      case RegionCountryDistrictTag => warehouseRegionId == region.id
      case _ =>
        region.parent match {
          case Some(parent) => isLocal(warehouseRegionId, parent)
          case None         => false
        }
    }

}
