lazy val root = (project in file(".")).
  settings(
    name := "akicalculator",
    version := "0.1",
    scalaVersion := "2.11.7"
    libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0" % "test"
  )