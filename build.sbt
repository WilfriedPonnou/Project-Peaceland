name := "Projet-Peaceland"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "org.scalatest" % "scalatest_2.13" % "3.1.0" % "test"
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "3.1.0"
libraryDependencies += "org.apache.kafka" %% "kafka" % "3.1.0"

// https://mvnrepository.com/artifact/com.github.stevenchen3/scala-faker
libraryDependencies += "com.github.pjfanning" %% "scala-faker" % "0.5.3"

// https://mvnrepository.com/artifact/commons-io/commons-io
libraryDependencies += "commons-io" % "commons-io" % "2.4"

// https://mvnrepository.com/artifact/org.apache.spark/spark-core
//libraryDependencies += "org.apache.spark" %% "spark-core" % "3.1.1"

// https://github.com/tototoshi/scala-csv
libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.3.8"