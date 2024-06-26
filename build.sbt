ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.13"

val akkaVersion = "2.8.3"
val akkaHttpVersion = "10.5.2"
val scalaTestVersion = "3.2.16"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.github.pureconfig" %% "pureconfig" % "0.17.0"
)

lazy val root = (project in file("."))
  .settings(
    name := "anastasiia.application"
  )
