organization := "com.typesafe.training"

name := "eventStream"

version := "3.0.0"

libraryDependencies ++= Dependencies.eventStream

scalacOptions ++= List(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-language:_",
  "-target:jvm-1.6",
  "-encoding", "UTF-8"
)

retrieveManaged := true

parallelExecution in Test := false

//initialCommands in console := "import com.typesafe.training.scalatrain._"

