val scalaV = "3.6.3"

ThisBuild / organization := "es.jesusmtnez"
ThisBuild / organizationName := "JesusMtnez"
ThisBuild / startYear := Some(2023)
ThisBuild / licenses := Seq(License.MIT)
ThisBuild / scalaVersion := scalaV

lazy val common = project
  .in(file("common"))
  .settings(name := "todobackend-common")

val http4sV = "1.0.0-M44"
val circeV = "0.14.10"
val log4catsV = "2.7.0"
val logbackV = "1.5.17"
val munitV = "1.1.0"
val munitCE3V = "2.0.0"

lazy val `todobackend-http4s` = project
  .in(file("todobackend-http4s"))
  .settings(
    name := "todobackend-http4s",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-ember-client" % http4sV,
      "org.http4s" %% "http4s-ember-server" % http4sV,
      "org.http4s" %% "http4s-dsl" % http4sV,
      "org.http4s" %% "http4s-circe" % http4sV,
      "io.circe" %% "circe-generic" % circeV,
      "org.typelevel" %% "log4cats-slf4j" % log4catsV,
      "ch.qos.logback" % "logback-classic" % logbackV,
      "org.scalameta" %% "munit" % munitV % Test,
      "org.typelevel" %% "munit-cats-effect" % munitCE3V % Test
    )
  )
  .dependsOn(common)
  .enablePlugins(BuildInfoPlugin)

val zioHttpV = "3.0.1"

lazy val `todobackend-zio-http` = project
  .in(file("todobackend-zio-http"))
  .settings(
    name := "todobackend-zio-http",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio-http" % zioHttpV
    )
  )
  .dependsOn(common)
  .enablePlugins(BuildInfoPlugin)

lazy val root = project
  .in(file("."))
  .settings(publish / skip := true)
  .aggregate(
    common,
    `todobackend-http4s`,
    `todobackend-zio-http`
  )

addCommandAlias("fmt", "all scalafmtSbt scalafmt Test/scalafmt")
