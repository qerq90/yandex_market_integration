package services.notification

import model.order.Order
import zio.Task

trait NotificationService {
  def notifyUsers(orders: List[Order]): Task[Unit]
}
