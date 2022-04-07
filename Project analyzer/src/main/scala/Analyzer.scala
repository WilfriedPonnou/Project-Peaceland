
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import model._
import org.apache.spark.sql.types.StructType
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import java.io.File


object Analyzer {

  def main(args: Array[String]): Unit = {
    
    val spark = SparkSession.builder()
                           .appName("Analyzer")
                           .master("local[*]")
                           .getOrCreate()
   /* import spark.implicits._
    val rawDF = spark.sparkContext
      .textFile("/mnt/c/Users/PONNOU Wilfried/Desktop/Project consumer/src/main/resources/peaceWatcherReportData/*")
      //.map(f=>f.split("\n"))
      //.flatMap(f=>f.split("\n"))
      //.map(line=>println(line))
      //.toDF()
    
    val reportRDD: RDD[Reports] = rawDF.foreach(f=>f.map(line=>Reports.stringToReport(line))
    reportRDD.collect()*/*/



    //rawDF.foreach(f=>println(f))
     // .map(_.split("|"))
      //:.map(attributes => Reports(attributes(0), attributes(1).trim.toInt))
      //.toDF()
    /*val userSchema = new StructType().add("peaceWatcherID", "integer")
                                     .add("lat", "double")
                                     .add("lon", "double")
                                     .add("lat", "double")
                                     .add("data", "List[Citizen]")
                                     .add("words", "List[String]")
                                     .add("date", "string")
*/
  val reportDF = spark.read
                      .option("delimiter",";")
                        //.option("inferSchema","true")
                        //.schema(userSchema)
                        //.csv("/mnt/c/Users/PONNOU Wilfried/Desktop/Project consumer/src/main/resources/peaceWatcherReportData/*")
                      .csv("/mnt/c/Users/PONNOU Wilfried/Desktop/Project consumer/src/main/resources/peaceWatcherReportData/part-00000-dbe2120d-2361-48d0-862b-ad077877d46e-c000.csv")
                      .withColumnRenamed("_c0","peaceWatcherId")
                      .withColumnRenamed("_c1","lat")
                      .withColumnRenamed("_c2","lon")
                      .withColumnRenamed("_c3","data")
                      .withColumnRenamed("_c4","words")
                      .withColumnRenamed("_c5","date")
  reportDF.show()
  
  val reportConatingCitizensDF = reportDF.withColumn("listCitizen",split(col("data"),","))
  val citizensDF = reportConatingCitizensDF.select(col("peaceWatcherId"),col("listCitizen"))
  val citizens = citizensDF.select(col("peaceWatcherId"),explode(col("listCitizen")))
  val citizensAndScores = citizens.withColumn("peaceScore",col("col").substr(-2,2)).withColumnRenamed("col","citizenData")
  val finalcitizendf = citizensAndScores.withColumn("citizenName",expr("substring(citizenData, 1, length(citizenData)-3)"))
  val finalcitizendfv2= finalcitizendf.withColumn("peaceScore", regexp_replace(col("peaceScore"), ":", "")).withColumn("peaceScore",col("peaceScore").cast("integer"))

  val days = reportDF.withColumn("day",col("date").substr(1,2))
  days.show()
  finalcitizendfv2.show()

  println("\n--------------------------STARTING ANALYSIS----------------------- \n")
  reportDF.select(countDistinct("peaceWatcherId").alias("How much distinct distinct peaceWatchers are there in service?")).show()
  
  println("\n ---------- Which days has most reports ? ------------------ \n")
  days.groupBy(col("day")).agg(count("day").as("NbOfReports")).orderBy(desc("NbOfReports")).show(1)

  println("\n ---------- Worst citizens ? ------------------ \n")
  finalcitizendfv2.orderBy(col("peaceScore")).show(5)

  println("\n ---------- Best citizens ? ------------------ \n")
  finalcitizendfv2.orderBy(desc("peaceScore")).show(5)



  
  spark.close
  }
}
 
  