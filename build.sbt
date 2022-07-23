ThisBuild / version := "1.0.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "htw-sose22-prog-konz-algo-word-count-spark"
  )

// https://mvnrepository.com/artifact/org.apache.spark/spark-core
libraryDependencies += "org.apache.spark" %% "spark-core" % "3.2.1"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.3.0-SNAP3"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.3.0-SNAP3" % Test

