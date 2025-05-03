package service.order

import dao.order.OrderDao
import dao.user.UserDao
import io.circe.Json
import model.order.Order
import model.order.Status.Failed
import model.user.User
import model.yandex.OrderCreated
import repository.yandex.YandexRepository
import zio.{Task, ZIO}

class OrderServiceLive(
  orderDao: OrderDao,
  userDao: UserDao,
  repo: YandexRepository
) extends OrderService {

  override def saveOrder(orderJson: Json): Task[Unit] =
    for {
      orderCreated <- ZIO.fromEither(orderJson.as[OrderCreated])
      order <- ZIO
        .fromOption(Order.fromOrderCreated(orderCreated))
        .orElseFail(new Throwable("Can't transform orderCreated to order"))
      _ <- orderDao.saveOrder(order)
    } yield ()

  override def enrichOrders(orders: List[Order]): Task[Unit] = {
    val campaignIds        = orders.map(_.campaignId).distinct
    val campaignIdToOrders = orders.groupBy(_.campaignId)
    for {
      users <- userDao.getUsers(campaignIds)

      _ <- ZIO.foreachParDiscard(campaignIdToOrders) {
        case (campaignId, ordersToEnrich) =>
          tryToEnrichOrders(
            users.find(_.campaignId == campaignId),
            ordersToEnrich
          )
      }
    } yield ()
  }

  private def tryToEnrichOrders(
      userOp: Option[User],
      orders: List[Order]
  ): Task[Unit] =
    userOp match {
      case None => orderDao.changeStatus(orders, Failed)
      case Some(user) =>
        for {
          offerMappings <- repo.getOfferMapping(user)
          enrichedOrders = orders.map(order =>
            order.copy(data =
              order.data.copy(items =
                order.data.items.map(item =>
                  item.copy(name =
                    Some(
                      offerMappings
                        .find(_.offerId == item.offerId)
                        .map(_.name)
                        .getOrElse("Имя не найдено")
                    )
                  )
                )
              )
            )
          )

          _ <- orderDao.enrichOrders(enrichedOrders)
        } yield ()
    }

}
