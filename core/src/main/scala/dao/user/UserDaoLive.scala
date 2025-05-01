package dao.user

import dao.BaseDao
import doobie.implicits._
import model.user.User
import zio.Task

class UserDaoLive(dao: BaseDao) extends UserDao {

  override def getUsers(campaignId: Int): Task[List[User]] =
    dao.query(
      sql"select * from users where campaignId = $campaignId"
        .query[User]
        .to[List]
    )

  override def saveUser(user: User): Task[Unit] =
    dao
      .query(
        sql"insert into users (telegramId, campaignId) values (${user.telegramId}, ${user.campaignId})".update.run
      )
      .unit
}
