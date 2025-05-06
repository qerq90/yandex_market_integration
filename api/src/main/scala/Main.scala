import bot.TelegramBot
import client.telegram.TelegramClient
import client.yandex.YandexClient
import dao.BaseDao
import dao.config.PostgresConfig
import dao.order.OrderDao
import dao.user.UserDao
import model.config.TelegramConfig
import repository.yandex.YandexRepository
import server.Server
import server.config.ServerConfig
import service.notification.NotificationService
import service.order.OrderService
import task.enrich.EnrichTask
import task.notification.NotificationTask
import zio._
import zio.logging.backend.SLF4J

object Main extends ZIOAppDefault {

  override val bootstrap: ZLayer[ZIOAppArgs, Nothing, Unit] =
    Runtime.removeDefaultLoggers >>> SLF4J.slf4j

  private type Env = Server
    with NotificationTask
    with EnrichTask
    with TelegramBot

  private val env =
    ZLayer.make[Env](
      PostgresConfig.live,
      BaseDao.live,
      OrderDao.live,
      OrderService.live,
      ServerConfig.live,
      Server.live,
      TelegramConfig.live,
      TelegramClient.live,
      UserDao.live,
      NotificationService.live,
      NotificationTask.live,
      YandexClient.live,
      YandexRepository.live,
      EnrichTask.live,
      TelegramBot.live
    )

  private val program =
    ZIO.serviceWithZIO[Server](_.run()) <&> ZIO
      .serviceWithZIO[NotificationTask](_.run) <&> ZIO
      .serviceWithZIO[EnrichTask](_.run) <&> ZIO
      .serviceWithZIO[TelegramBot](_.run)

  override def run: ZIO[ZIOAppArgs with Scope, Any, Any] =
    program.provideLayer(env)
}
