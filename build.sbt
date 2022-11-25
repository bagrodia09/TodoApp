ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

val zioQuillVersion = "4.6.0"
val zioVersion = "2.0.4"
val postgresVersion = "42.5.0"
val flywayVersion = "9.8.2"
val zioJsonVersion = "0.3.0-RC11"
val zioConfigVersion = "3.0.2"

name := "TodoApp"

lazy val zio = (project in file("zio"))
  .settings(
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % zioVersion,
      "io.d11" %% "zhttp" % "2.0.0-RC10",
      "dev.zio" %% "zio-test" % zioVersion,
      "io.getquill" %% "quill-jdbc-zio" % zioQuillVersion,
      "org.postgresql" % "postgresql" % postgresVersion,
      "org.flywaydb" % "flyway-core" % flywayVersion,
      "dev.zio" %% "zio-json" % zioJsonVersion,
      "dev.zio" %% "zio-config" % zioConfigVersion,
      "dev.zio" %% "zio-config-typesafe" % zioConfigVersion,
      "dev.zio" %% "zio-config-magnolia" % zioConfigVersion,
      "dev.zio" %% "zio-logging" % "2.1.4",
    )
  )

lazy val akka = (project in file("akka"))
  .settings()