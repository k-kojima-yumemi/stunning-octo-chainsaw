ThisBuild / scalaVersion := "2.13.10"
ThisBuild / organization := "jp.co.yumemi.koma"

lazy val pro = (project in file("."))
  .settings(
    name := "compile-check"
  )

Compile / packageBin / mainClass := Some("jp.co.yumemi.koma.CompileCheckMain")
Compile / run / mainClass := Some("jp.co.yumemi.koma.CompileCheckMain")
