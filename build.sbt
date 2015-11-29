val sprayV = "1.3.1"

lazy val root = (project in file(".")).
  settings(
    name := "akicalculator",
    version := "0.1",
    scalaVersion := "2.11.7",
    resolvers += "spary repo" at "http://repo.spray.io",
    libraryDependencies ++= Seq (
    	"org.scalatest"      % "scalatest_2.11" % "2.2.4" % "test",
    	"com.typesafe.akka"  %% "akka-actor"    % "2.3.6",
    	"io.spray" 		     %% "spray-json"    % sprayV,
        "io.spray"           %% "spray-can"     % sprayV,
	    "io.spray"           %% "spray-routing" % sprayV,
    	"io.spray"           %% "spray-testkit" % sprayV  % "test",
    	 "commons-codec"     % "commons-codec"  % "1.6"
    )
  )

Revolver.settings