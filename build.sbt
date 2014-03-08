name := "nplusone"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  "mysql" % "mysql-connector-java" % "5.1.27",
  "ws.securesocial" %% "securesocial" % "2.1.3",
  "com.typesafe.play" %% "play-slick" % "0.6.0.1"
)

resolvers += Resolver.sonatypeRepo("releases")

play.Project.playScalaSettings
