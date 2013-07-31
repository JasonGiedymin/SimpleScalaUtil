name := "Simple-Scala-Util"

version := "0.2.0"

scalaVersion := "2.10.0"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "OSS Sonatype Public" at "https://oss.sonatype.org/content/groups/public/"

libraryDependencies ++= Seq(
      "com.novus" %% "salat" % "1.9.2-SNAPSHOT", // snapshot with 2.1x support
      "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test",
      "com.yammer.metrics" % "metrics-core" % "2.2.0",
      "com.yammer.metrics" % "metrics-scala_2.9.1" % "2.2.0"
)

// For Eclipse sources otherwise would have to do 'sbt update classifiers'
// which doesn't always (if at anytime) really work
EclipseKeys.withSource := true
