import sbt.*

object Dependencies {
  private val http4s =
    List(
      "http4s-ember-server",
      "http4s-dsl",
    )
    .map("org.http4s" %% _ % "0.23.16")

  private val ip4s = List("com.comcast" %% "ip4s-core" % "3.5.0")

  private val pureconfig =
    List(
        "pureconfig",
        "pureconfig-ip4s",
        "pureconfig-http4s"
    )
    .map("com.github.pureconfig" %% _ % "0.17.7")

  private val zio = List(
    "dev.zio" %% "zio" % "2.0.22",
    "dev.zio" %% "zio-interop-cats" % "23.1.0.2"
  )

  val apiDependencies: List[ModuleID] = pureconfig ++ ip4s
  val coreDependencies: List[ModuleID] = zio ++ http4s
}

