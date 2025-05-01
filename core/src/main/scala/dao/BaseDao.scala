package dao

import cats.implicits._
import doobie.ConnectionIO
import doobie.hikari.{Config, HikariTransactor}
import doobie.implicits._
import doobie.util.transactor.Transactor
import zio.interop.catz._
import zio.{Task, ZIO, ZLayer}

class BaseDao(xa: Transactor[Task]) {
  def query[T](query: ConnectionIO[T]): Task[T] = query.transact(xa)
}

object BaseDao {
  val live: ZLayer[PostgresConfig, Throwable, BaseDao] = ZLayer.scoped {
    for {
      config <- ZIO.service[PostgresConfig]

      daoconfig = Config(
        jdbcUrl = config.jdbcUrl,
        username = config.username.some,
        password = config.password.some
      )

      xa <- HikariTransactor.fromConfig[Task](daoconfig).toScopedZIO
    } yield new BaseDao(xa)
  }
}
