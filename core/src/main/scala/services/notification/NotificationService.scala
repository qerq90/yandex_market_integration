package services.notification

import zio.UIO

import java.util.UUID

trait NotificationService {
  def notify(userId: UUID): UIO[Unit]
}
