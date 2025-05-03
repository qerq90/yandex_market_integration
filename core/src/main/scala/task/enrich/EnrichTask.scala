package task.enrich

import dao.order.OrderDao
import dao.user.UserDao
import model.order.Order
import model.order.Status.{Created, Failed}
import model.user.User
import repository.yandex.YandexRepository
import task.Scheduler
import zio.{durationInt, Duration, Task, ZIO, ZLayer}

class EnrichTask(repo: YandexRepository, orderDao: OrderDao, userDao: UserDao)
    extends Scheduler {

  override val interval: Duration = 10.seconds

  override def task: Task[Unit] =
    for {
      orders <- orderDao.getOrdersByStatus(Created)
      _      <- ZIO.log(s"Orders to enrich: ${orders}")
      _ <- ZIO.when(orders.nonEmpty) {
        val campaignIds        = orders.map(_.campaignId).distinct
        val campaignIdToOrders = orders.groupBy(_.campaignId)
        for {
          users <- userDao.getUsers(campaignIds)

          _ <- ZIO.foreachParDiscard(campaignIdToOrders) {
            case (campaignId, ordersToEnrich) =>
              enrichOrders(
                users.find(_.campaignId == campaignId),
                ordersToEnrich
              )
          }
        } yield ()
      }
    } yield ()

  private def enrichOrders(
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

object EnrichTask {
  val live: ZLayer[
    YandexRepository with OrderDao with UserDao,
    Nothing,
    EnrichTask
  ] = ZLayer.fromFunction(new EnrichTask(_, _, _))
}
