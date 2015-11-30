resolvers += "spray repo" at "http://repo.spray.io" // not needed for sbt >= 0.12

addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.1")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.0-M1")
