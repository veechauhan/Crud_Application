name := """PlayFrameworkCRUD"""
organization := "com.knoldus"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.2"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.github.plippe.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.github.plippe.binders._"

libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "42.2.12",
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0",
)

//libraryDependencies ++= Seq(
//  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % "test"
//)
libraryDependencies += specs2 % Test
libraryDependencies += jdbc % Test
