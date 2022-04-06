name := "Consumer"

version := "1.0"

scalaVersion := "2.12.15"
val sparkVersion = "3.2.1"

// https://mvnrepository.com/artifact/org.apache.spark/spark-streaming-kafka-0-10
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkVersion,
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "com.github.pjfanning" %% "scala-faker" % "0.5.3"
)// verified in maven repo

dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-core" % "2.10.5"
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.10.5"
dependencyOverrides += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.10.5" 
useCoursier := false