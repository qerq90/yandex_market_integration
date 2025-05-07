package dao.user

import cats.data.NonEmptyList
import dao.BaseDao
import doobie.Fragments
import doobie.implicits._
import model.user.User
import zio.{Task, ZIO}

class UserDaoLive(dao: BaseDao) extends UserDao {

  override def getUsers(campaignIds: List[Long]): Task[List[User]] =
    NonEmptyList.fromList(campaignIds) match {
      case None => ZIO.succeed(List())
      case Some(ids) =>
        dao.query(
          (sql"select * from users where " ++ Fragments
            .in(fr"campaign_id", ids))
            .query[User]
            .to[List]
        )
    }

  override def saveUser(user: User): Task[Unit] =
    dao
      .query(
        sql"insert into users (telegram_id, campaign_id, token) values (${user.telegramId}, ${user.campaignId}, ${user.token})".update.run
      )
      .unit
}
