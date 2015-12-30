import sbt._
import Keys._
import java.io.File

object Build extends Build {
  val defaultPlay2Version = "2.4.3"
  val play2Version = sys.props.get("play2.version").filterNot(_.isEmpty).getOrElse(defaultPlay2Version)

  val playDependency = "com.typesafe.play" %% "play-server" % play2Version % "provided->default" exclude ("javax.servlet", "servlet-api")

  lazy val root = Project(id = "play-servlet",
    base = file("."),
    settings = Seq(
      libraryDependencies += playDependency,
      libraryDependencies += "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided->default"
    )
  )
  //
  // Servlet implementations
  //
  // lazy val play2WarCoreCommon = project(id = "play-servlet-common",
  //   base = file("core/common"),
  //   settings = commonSettings ++ mavenSettings ++ Seq(
  //     libraryDependencies += playDependency,
  //     libraryDependencies += "javax.servlet" % "servlet-api" % "2.5" % "provided->default"))

  // lazy val play2WarCoreservlet31 = project(id = "play2-war-core-servlet31",
  //   base = file("core/servlet31"),
  //   settings = commonSettings ++ mavenSettings ++ Seq(
  //     libraryDependencies += playDependency,
  //     libraryDependencies += "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided->default")) dependsOn play2WarCoreCommon

  //
  // Integration tests
  //
  // lazy val play2WarIntegrationTests = project(id = "integration-tests",
  //   base = file("integration-tests"),
  //   settings = commonSettings ++ mavenSettings ++ Seq(
  //     sbtPlugin := false,
  //     publishArtifact := false,

  //     libraryDependencies += "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test",
  //     libraryDependencies += "junit" % "junit" % "4.10" % "test",
  //     libraryDependencies += "org.codehaus.cargo" % "cargo-core-uberjar" % "1.4.13" % "test",
  //     libraryDependencies += "net.sourceforge.htmlunit" % "htmlunit" % "2.13" % "test",

  //     parallelExecution in Test := false,
  //     testOptions in Test += Tests.Argument("-oD"),
  //     testOptions in Test += Tests.Argument("-Dwar.servlet31=" + servlet31SampleWarPath),
  //     testOptions in Test += Tests.Argument("-Dwar.servlet30=" + servlet30SampleWarPath),
  //     testOptions in Test += Tests.Argument("-Dwar.servlet25=" + servlet25SampleWarPath),
  //     testListeners <<= target.map(t => Seq(new eu.henkelmann.sbt.JUnitXmlTestsListener(t.getAbsolutePath)))))

  //
  // Settings
  //
  // def commonSettings = buildSettings ++ Seq(ScalastylePlugin.Settings: _*) ++ Seq(
  //     javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
  //     scalacOptions ++= Seq("-unchecked", "-deprecation"),
  //     publishArtifact in Test := false)

  // object BuildSettings {

  //   val buildOrganization = "com.github.play2war"
  //   val defaultPlay2Version = "2.4.0"
  //   val play2Version = sys.props.get("play2.version").filterNot(_.isEmpty).getOrElse(defaultPlay2Version)
  //   val defaultBuildVersion = "1.4-beta2-SNAPSHOT"
  //   val buildVersion = sys.props.get("play2war.version").filterNot(_.isEmpty).getOrElse(defaultBuildVersion)
  //   val buildScalaVersion210 = "2.10.5"
  //   val buildScalaVersion211 = "2.11.6"
  //   val buildScalaVersion = sys.props.get("play2war.sbt.scala211").map(p => buildScalaVersion211).getOrElse(buildScalaVersion210)
  //   val buildScalaVersionForSbt = "2.10.5"
  //   val buildScalaVersionForSbtBinaryCompatible = CrossVersion.binaryScalaVersion(buildScalaVersionForSbt)
  //   val buildSbtVersion   = "0.13.8"
  //   val buildSbtVersionBinaryCompatible = "0.13"

  //   val buildSettings = Defaults.defaultSettings ++ Seq(
  //     resolvers           += Resolver.typesafeRepo("releases"),
  //     organization        := buildOrganization,
  //     version             := buildVersion,
  //     scalaVersion        := buildScalaVersion,
  //     scalaBinaryVersion  := CrossVersion.binaryScalaVersion(buildScalaVersion),
  //     checksums in update := Nil)

  // }

  // def commonIvyMavenSettings: Seq[Setting[_]] = Seq(
  //   licenses := Seq("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  //   homepage := Some(url("https://github.com/play2war/play2-war-plugin"))
  // )

  // def ivySettings = commonIvyMavenSettings ++ Seq(
  //   publishMavenStyle := false,
  //   bintrayReleaseOnPublish := false,
  //   bintrayRepository := "sbt-plugins",
  //   bintrayOrganization := Some("play2war")
  // )

  // def propOr(name: String, value: String): String =
  //   (sys.props get name) orElse
  //     (sys.env get name) getOrElse
  //     value

  // object Generators {

  //   val Play2WarVersion = { dir: File =>
  //     val file = dir / "Play2WarVersion.scala"
  //     IO.write(file,
  //       """|package com.github.play2war.plugin
  //                  |
  //                  |object Play2WarVersion {
  //                  |    val current = "%s"
  //                  |}
  //               """.stripMargin.format(BuildSettings.buildVersion))
  //     Seq(file)
  //   }

  // }

  // def project(id: String, base: File, settings: Seq[Def.Setting[_]] = Nil) =  
  //   Project(id, base, settings = settings)
}
