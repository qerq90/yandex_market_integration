import Dependencies.*

lazy val root = (project in file("."))
  .aggregate(api)

lazy val api = (project in file("api"))
  .settings(
    standartSettings,
    libraryDependencies ++= apiDependencies,
    Compile / mainClass         := Some("api.Main"),
    name                        := "yandex-integration",
    version                     := "0.1.0",
    Docker / packageName        := "yandex-integration-bot",
    Docker / version            := "latest",
    Docker / daemonUser         := "daemon",
    Docker / dockerExposedPorts := Seq(8080),
    Docker / dockerBaseImage    := "openjdk:17-alpine",
    Docker / dockerLabels := Map(
      "maintainer"  -> "qerq90",
      "description" -> "Yandex Integration Bot"
    )
  )
  .dependsOn(core)

lazy val core = (project in file("core"))
  .settings(standartSettings, libraryDependencies ++= coreDependencies)

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Ywarn-unused"
)

lazy val standartSettings = Seq(
  scalaVersion := "2.13.16"
)

enablePlugins(JavaAppPackaging, DockerPlugin)

resolvers ++= Seq(
  Resolver.mavenLocal,
  "Maven Central" at "https://repo1.maven.org/maven2/",
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/",
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)
