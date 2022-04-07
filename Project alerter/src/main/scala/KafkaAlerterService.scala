import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.{SparkConf, SparkContext, sql}
//import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.types.{StructType,StructField}
import model._
import org.apache.log4j.{Level, Logger}


object KafkaAlerterService extends App{
  
  val rootLogger = Logger.getRootLogger()
  rootLogger.setLevel(Level.ERROR)
  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
  Logger.getLogger("org.spark-project").setLevel(Level.WARN)
  
  val session = SparkSession.builder()
                           .appName("RDDalerter")
                           .master("local[2]")
                           .getOrCreate()


  val ssc = new StreamingContext(session.sparkContext, Seconds(5))
  val kafkaParams= Map[String,Object](
    "bootstrap.servers"-> "localhost:9092",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> "myconsumergroup",
    "auto.offset.reset" -> "earliest",
    "enable.auto.commit" -> (true: java.lang.Boolean)

  )
  
  val topics = Array("peaceWatcherReport")
  val stream = KafkaUtils.createDirectStream[String,String](
    ssc,
    PreferConsistent,
    Subscribe[String,String](topics,kafkaParams)
  )

  val badCitizens=stream.map{record => (record.key(), record.value)}
                        .map(records=>records._2)
                        .map(record=>Reports.stringToReport(record))
                        .map(reports=>(reports.lat,reports.lon,reports.data.filter(_.citizenPeacescore<20)))
                        .filter(_._3.nonEmpty)

  println("\n\n")
  println("   #    #       ####### ######  #######  #####  ")
  println("  # #   #       #       #     #    #    #     # ") 
  println(" #   #  #       #       #     #    #    #       ") 
  println("#     # #       #####   ######     #     #####  ")
  println("####### #       #       #   #      #          # ")
  println("#     # #       #       #    #     #    #     # ")
  println("#     # ####### ####### #     #    #     #####  ")
  println("\n\n")
  badCitizens.foreachRDD(_.foreach(record=> 
                                  println("\n ALERT!: °°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°° \n"
                                  + " citizens : " + record._3.toString 
                                  + " have bad peacescore ! They Are at " + record._1 + "and" +record._2
                                  +"\n °°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°")
                                  )
  )

  ssc.start() 
  ssc.awaitTermination()
  

}