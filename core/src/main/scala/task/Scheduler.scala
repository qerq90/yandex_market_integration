package task

import zio.{Duration, Schedule, Task, ZIO}

abstract class Scheduler {
  val interval: Duration

  def task: Task[Unit]
  def run: Task[Unit] =
    task
      .catchAll(err =>
        ZIO.logError(s"Error while running task: ${err.getMessage}")
      )
      .repeat(Schedule.fixed(interval))
      .unit
}
