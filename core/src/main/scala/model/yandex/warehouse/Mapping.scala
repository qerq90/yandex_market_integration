package model.yandex.warehouse

object Mapping {
  val RegionCountryDistrictTag = "COUNTRY_DISTRICT"
  val WarehouseMapping: Map[Int, Int] = Map(
    304 -> 3,  // Moscow KGT			-> Central Federal District
    305 -> 26, // Rostov 2 			-> South Federal District
    147 -> 26, // Rostov 1 			-> South Federal District
    308 -> 3,  // Moscow (NO KGT) 2 	-> Central Federal District
    172 -> 3,  // Moscow (NO KGT) 1   -> Central Federal District
    300 -> 52, // Ekaterinburg 		-> Ural Federal District
    301 -> 17, // Leningrad 			-> North West Federal District
    302 -> 40  // Samara 				-> Privolgskii Federal District
  )
}
