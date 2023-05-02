ThisBuild / scalaVersion := "2.13.10"
ThisBuild / organization := "jp.co.yumemi.koma"

lazy val pro = (project in file("."))
  .settings(
    name := "compile-check"
  )

