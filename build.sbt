name := """playground"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

EclipseKeys.eclipseOutput := Some("target/scala-2.11/classes")
EclipseKeys.withSource := true
EclipseKeys.withJavadoc := true

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test)
  
  val akkaVersion = "2.4.16"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-agent" % akkaVersion,
  "com.typesafe.akka" %% "akka-camel" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-sharding" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "com.typesafe.akka" %% "akka-contrib" % akkaVersion,
  "com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-osgi" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-tck" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-distributed-data-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-typed-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-query-experimental" % akkaVersion)

val akkaHttpVersion = "10.0.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-jackson" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-xml" % akkaHttpVersion)

val camelVersion = "2.18.0"

libraryDependencies ++= Seq(
  "org.apache.camel" % "camel-core" % camelVersion,
  "org.apache.camel" % "camel-scala" % camelVersion,
  "org.apache.camel" % "camel-http4" % camelVersion,
  "org.apache.camel" % "camel-jetty" % camelVersion,
  "org.apache.camel" % "camel-quartz" % camelVersion,
  "org.apache.camel" % "camel-test" % camelVersion % "test",
  "org.apache.httpcomponents" % "httpmime" % "4.5.1")

val circeVersion = "0.6.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-parser" % circeVersion,
  "io.circe" %% "circe-optics" % circeVersion)
  
libraryDependencies += "com.sparkjava" % "spark-core" % "2.5.4"

libraryDependencies += "org.apache.httpcomponents" % "fluent-hc" % "4.5.3"
