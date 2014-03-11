name := "nplusone"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  "com.github.tototoshi" %% "slick-joda-mapper" % "1.0.1",
  "com.typesafe.play" %% "play-slick" % "0.6.0.1",
  "mysql" % "mysql-connector-java" % "5.1.27",
  "ws.securesocial" %% "securesocial" % "2.1.3"
)

resolvers += Resolver.sonatypeRepo("releases")

play.Project.playScalaSettings
