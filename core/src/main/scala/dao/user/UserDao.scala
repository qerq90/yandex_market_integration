package dao.user

import dao.BaseDao
import model.user.User
import zio.{Task, ZLayer}

trait UserDao {
  def getUsers(campaignIds: List[Int]): Task[List[User]]
  def saveUser(user: User): Task[Unit]
}

object UserDao {
  val live: ZLayer[BaseDao, Nothing, UserDaoLive] =
    ZLayer.fromFunction(new UserDaoLive(_))
}
