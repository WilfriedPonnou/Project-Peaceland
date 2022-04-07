
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import model._
import org.apache.log4j.{Level, Logger}
import java.util._


object Analyzer {

  def main(args: Array[String]): Unit = {

    val rootLogger = Logger.getRootLogger()
    rootLogger.setLevel(Level.ERROR)
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.spark-project").setLevel(Level.WARN)

    val spark = SparkSession.builder()
      .appName("Analyzer")
      .master("local[2]")
      .getOrCreate()


    val reportDF = spark.read
      .option("delimiter",";")
      .csv("/mnt/c/Users/PONNOU Wilfried/Desktop/Project consumer/src/main/resources/peaceWatcherReportData/*")
      .withColumnRenamed("_c0","peaceWatcherId")
      .withColumnRenamed("_c1","lat")
      .withColumnRenamed("_c2","lon")
      .withColumnRenamed("_c3","data")
      .withColumnRenamed("_c4","words")
      .withColumnRenamed("_c5","date")
    reportDF.show()

    val reportConatingCitizensDF = reportDF.withColumn("listCitizen",split(col("data"),","))
    val citizensDF = reportConatingCitizensDF.select(col("peaceWatcherId"),col("listCitizen"))
      .select(col("peaceWatcherId"),explode(col("listCitizen")))
      .withColumn("peaceScore",col("col").substr(-2,2))
      .withColumnRenamed("col","citizenData")
      .withColumn("citizenName",expr("substring(citizenData, 1, length(citizenData)-3)"))
      .withColumn("peaceScore", regexp_replace(col("peaceScore"), ":", "")).withColumn("peaceScore",col("peaceScore").cast("integer"))

    val days=reportDF.select(from_unixtime(col("date"),"yyyy-MM-dd HH:mm:ss").as("day"))
      .withColumn("dayOfWeek", date_format(col("day"), "E"))
      .select("dayOfWeek")

    //days.show()
    //citizensDF.show()

    println("\n--------------------------STARTING ANALYSIS----------------------- \n")
    reportDF.select(countDistinct("peaceWatcherId").alias("How much distinct distinct peaceWatchers are there in service?")).show()

    println("\n ---------- Which days has most reports ? ------------------ \n")
    days.groupBy(col("dayOfWeek")).agg(count("dayOfWeek").as("NbOfReports")).orderBy(desc("NbOfReports")).show(1)

    println("\n ---------- Worst citizens ? ------------------ \n")
    citizensDF.orderBy(col("peaceScore")).select(col("peaceScore"),col("citizenName")).show(5)

    println("\n ---------- Best citizens ? ------------------ \n")
    citizensDF.orderBy(desc("peaceScore")).select(col("peaceScore"),col("citizenName")).show(5)




    spark.close
  }
}
 
  