package task

import zio.{Duration, Schedule, Task, UIO, ZIO}

abstract class Scheduler {
  val interval: Duration

  def task: Task[Unit]
  def run: UIO[Unit] =
    task
      .catchAll(err =>
        ZIO.logError(s"Error while running task: ${err.getMessage}")
      )
      .repeat(Schedule.fixed(interval))
      .unit
}
