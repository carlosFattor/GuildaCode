name := """GuildaCode"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "javax.mail" % "mail" % "1.4.1",
  "com.typesafe.play" %% "play-mailer" % "2.4.0"
)
