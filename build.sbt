val scalaV = "3.6.3"

ThisBuild / organization := "es.jesusmtnez"
ThisBuild / organizationName := "JesusMtnez"
ThisBuild / startYear := Some(2023)
ThisBuild / licenses := Seq(License.MIT)
ThisBuild / scalaVersion := scalaV

lazy val common = project
  .in(file("common"))
  .settings(name := "todobackend-common")

lazy val `todobackend-cask` = project
  .in(file("todobackend-cask"))
  .settings(
    name := "todobackend-cask",
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "cask" % "0.9.7"
    )
  )
  .dependsOn(common)
  .enablePlugins(BuildInfoPlugin)

val http4sV = "1.0.0-M44"

lazy val `todobackend-http4s` = project
  .in(file("todobackend-http4s"))
  .settings(
    name := "todobackend-http4s",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-ember-client" % http4sV,
      "org.http4s" %% "http4s-ember-server" % http4sV,
      "org.http4s" %% "http4s-dsl" % http4sV,
      "org.http4s" %% "http4s-circe" % http4sV,
      "io.circe" %% "circe-generic" % "0.14.10",
      "org.typelevel" %% "log4cats-slf4j" % "2.7.0",
      "ch.qos.logback" % "logback-classic" % "1.5.17",
      "org.scalameta" %% "munit" % "1.1.0" % Test,
      "org.typelevel" %% "munit-cats-effect" % "2.0.0" % Test
    )
  )
  .dependsOn(common)
  .enablePlugins(BuildInfoPlugin)

lazy val `todobackend-pekko-http` = project
  .in(file("todobackend-pekko-http"))
  .settings(
    name := "todobackend-pekko-http",
    libraryDependencies ++= Seq(
      "org.apache.pekko" %% "pekko-actor-typed" % "1.1.2",
      "org.apache.pekko" %% "pekko-stream" % "1.1.2",
      "org.apache.pekko" %% "pekko-http" % "1.1.0",
      "org.apache.pekko" %% "pekko-http-cors" % "1.1.0",
      "com.github.pjfanning" %% "pekko-http-circe" % "3.0.1",
      "io.circe" %% "circe-generic" % "0.14.10"
    )
  )
  .dependsOn(common)
  .enablePlugins(BuildInfoPlugin)

lazy val `todobackend-zio-http` = project
  .in(file("todobackend-zio-http"))
  .settings(
    name := "todobackend-zio-http",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio-http" % "3.0.1"
    )
  )
  .dependsOn(common)
  .enablePlugins(BuildInfoPlugin)

lazy val root = project
  .in(file("."))
  .settings(publish / skip := true)
  .aggregate(
    common,
    `todobackend-cask`,
    `todobackend-http4s`,
    `todobackend-pekko-http`,
    `todobackend-zio-http`
  )

addCommandAlias("fmt", "all scalafmtSbt scalafmt Test/scalafmt")
