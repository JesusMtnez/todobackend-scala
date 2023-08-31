val scalaV = "3.3.0"

ThisBuild / organization := "es.jesusmtnez"
ThisBuild / organizationName := "JesusMtnez"
ThisBuild / startYear := Some(2023)
ThisBuild / licenses := Seq(License.MIT)
ThisBuild / scalaVersion := scalaV

val catsV = "2.10.0"
val catsEffectV = "3.5.1"
val http4sV = "1.0.0-M40"
val logbackV = "1.2.6"
val munitV = "0.7.29"
val munitCE3V = "1.0.7"

lazy val `todobackend-http4s` = project
  .in(file("todobackend-http4s"))
  .settings(
    name := "todobackend-http4s",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % catsV,
      "org.typelevel" %% "cats-effect" % catsEffectV,
      "org.http4s" %% "http4s-ember-client" % http4sV,
      "org.http4s" %% "http4s-ember-server" % http4sV,
      "org.http4s" %% "http4s-dsl" % http4sV,
      "org.http4s" %% "http4s-circe" % http4sV,
      "ch.qos.logback" % "logback-classic" % logbackV,
      "org.scalameta" %% "munit" % munitV % Test,
      "org.typelevel" %% "munit-cats-effect-3" % munitCE3V % Test
    )
  )
  .enablePlugins(BuildInfoPlugin)

lazy val root = project
  .in(file("."))
  .settings(publish / skip := true)
  .aggregate(
    `todobackend-http4s`
  )

addCommandAlias("fmt", "all scalafmtSbt scalafmt Test/scalafmt")
