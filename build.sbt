scalaVersion := "2.13.12"
name := "hello-server"
organization := "com.cloudfarms"
version := "1.0"

val zioConfigVersion = "4.0.1"

enablePlugins(JavaAppPackaging)

libraryDependencies ++= Seq(
  "dev.zio" %% "zio-http" % "3.0.0-RC4",
  "dev.zio" %% "zio-config"          % zioConfigVersion,
  "dev.zio" %% "zio-config-magnolia" % zioConfigVersion,
  "dev.zio" %% "zio-config-typesafe" % zioConfigVersion,
  "dev.zio" %% "zio-config-refined"  % zioConfigVersion,
)
