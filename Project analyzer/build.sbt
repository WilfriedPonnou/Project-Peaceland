name := "Analyzer"

version := "1.0"

scalaVersion := "2.12.15"
val sparkVersion = "3.2.1"


libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "com.github.pjfanning" %% "scala-faker" % "0.5.3"
)

useCoursier:=false