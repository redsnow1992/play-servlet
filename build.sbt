name := """play-servlet"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"
scalacOptions ++= Seq("-feature", "-deprecation", "-unchecked")

resolvers += Resolver.typesafeRepo("releases")
resolvers += "maven2" at "http://repo1.maven.org/maven2/"
