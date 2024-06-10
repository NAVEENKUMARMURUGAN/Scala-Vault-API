name := "file-upload-service"

version := "0.1"

scalaVersion := "2.13.13"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.4.1",
  "com.softwaremill.sttp.client3" %% "core" % "3.8.3",
  "com.softwaremill.sttp.client3" %% "circe" % "3.8.3",
  "org.scalatest" %% "scalatest" % "3.2.9" % Test,
  "io.circe" %% "circe-parser" % "0.14.1",
  "io.circe" %% "circe-generic" % "0.14.1",
  "com.typesafe.akka" %% "akka-http" % "10.5.3",
  "com.typesafe.akka" %% "akka-stream" % "2.8.5"
)


resolvers += Resolver.sonatypeRepo("public")