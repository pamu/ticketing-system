name := "ticketing-system"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.3-M1",
  "org.webjars" % "bootstrap" % "2.3.1",
  "org.webjars" % "requirejs" % "2.1.11-1",
  //"org.postgresql" % "postgresql" % "9.4-1200-jdbc41",
  "com.typesafe.slick" %% "slick" % "3.0.0",
  "com.h2database" % "h2" % "1.4.187"
  //"mysql" % "mysql-connector-java" % "5.1.35"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
