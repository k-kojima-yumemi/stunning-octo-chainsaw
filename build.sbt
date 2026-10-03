ThisBuild / organization := "jp.co.yumemi.koma"

val scala2Version = "2.13.18"
val scala3LtsVersion = "3.3.8"
val scala3Version = "3.9.0"

ThisBuild / scalaVersion := scala2Version
ThisBuild / crossScalaVersions := Seq(scala2Version, scala3LtsVersion, scala3Version)

lazy val pro = (project in file("."))
  .settings(
    name := "compile-check",
    compileOrder := CompileOrder.Mixed,
    // Java 25 is required for module import declarations (JEP 511) and flexible constructor bodies (JEP 513)
    javacOptions ++= Seq("--release", "25"),
  )

Compile / packageBin / mainClass := Some("jp.co.yumemi.koma.CompileCheckMain")
Compile / run / mainClass := Some("jp.co.yumemi.koma.CompileCheckMain")
