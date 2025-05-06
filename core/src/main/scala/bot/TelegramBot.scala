package bot

import canoe.api._
import canoe.syntax._
import dao.user.UserDao
import model.config.TelegramConfig
import zio._
import model.user.User
import zio.interop.catz._

class TelegramBot(config: TelegramConfig, userDao: UserDao) {

  def inputInt[F[_]: TelegramClient]: Scenario[F, Int] =
    for {
      possibleInt <- Scenario.expect(textMessage)
      campaignId <- possibleInt.text.toIntOption match {
        case Some(id) => Scenario.pure[F](id)
        case None =>
          for {
            _   <- Scenario.eval(possibleInt.reply("Введите число"))
            res <- inputInt
          } yield res
      }
    } yield campaignId

  // Сценарий диалога
  def scenario(implicit client: TelegramClient[Task]): Scenario[Task, Unit] =
    for {
      chat       <- Scenario.expect(command("start").chat)
      _          <- Scenario.eval(chat.send("Привет! Какой у вас campaignId?"))
      campaignId <- inputInt
      _ <- Scenario.eval(
        chat.send("Отлично! Теперь предоставьте token c read правами")
      )
      // need to check token
      tokenMsg <- Scenario.expect(textMessage)
      token = tokenMsg.text
      user  = User(campaignId, tokenMsg.chat.id.toInt, token)
      res <- Scenario
        .eval(userDao.saveUser(user))
        .attempt
      _ <- res match {
        case Left(err) =>
          Scenario.eval(
            chat.send(
              s"Произошла ошибка при сохранении ваших данных, попробуйте еще раз"
            )
          )
        case Right(_) =>
          Scenario.eval(
            tokenMsg.reply(
              s"Спасибо! Данные сохранены. Ожидайте нотификаций"
            )
          )
      }
    } yield ()

  def run: Task[Unit] =
    TelegramClient
      .global[Task](config.token)
      .use { implicit client: TelegramClient[Task] =>
        Bot.polling[Task].follow(scenario).compile.drain
      }
      .tapError(err => ZIO.logError(err.getMessage))
      .ignore
}

object TelegramBot {
  val live: ZLayer[TelegramConfig with UserDao, Nothing, TelegramBot] =
    ZLayer.fromFunction(new TelegramBot(_, _))
}
