import server.Server
import server.config.ServerConfig
import zio._

object Main extends ZIOAppDefault {

  private type Env = Server

  private val env =
    ZLayer.make[Env](
      ServerConfig.live,
      Server.live
    )

  private val program = ZIO.serviceWithZIO[Server](_.run())

  override def run: ZIO[ZIOAppArgs with Scope, Any, Any] =
    program.provideLayer(env)
}
