import client.telegram.TelegramClient
import client.telegram.config.TelegramConfig
import dao.BaseDao
import dao.config.PostgresConfig
import dao.order.OrderDao
import dao.user.UserDao
import server.Server
import server.config.ServerConfig
import service.notification.NotificationService
import service.order.OrderService
import task.notification.NotificationTask
import zio._
import zio.logging.backend.SLF4J

object Main extends ZIOAppDefault {

  override val bootstrap: ZLayer[ZIOAppArgs, Nothing, Unit] =
    Runtime.removeDefaultLoggers >>> SLF4J.slf4j

  private type Env = Server with NotificationTask

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
      NotificationTask.live
    )

  private val program =
    ZIO.serviceWithZIO[Server](_.run()) <&> ZIO
      .serviceWithZIO[NotificationTask](_.run)

  override def run: ZIO[ZIOAppArgs with Scope, Any, Any] =
    program.provideLayer(env)
}
