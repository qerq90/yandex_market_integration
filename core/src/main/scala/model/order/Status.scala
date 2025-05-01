package model.order

import enumeratum._

sealed trait Status extends EnumEntry

object Status extends Enum[Status] with DoobieEnum[Status] {
  val values = findValues

  case object Created  extends Status
  case object Enriched extends Status
  case object Finished extends Status

}
