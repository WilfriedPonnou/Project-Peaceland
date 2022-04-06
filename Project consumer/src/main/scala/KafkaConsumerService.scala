import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.{SparkConf, SparkContext, sql}
import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.Trigger

//import org.apache.hadoop.shaded.org.eclipse.jetty.websocket.common.frames.DataFrame

object KafkaConsumerService extends App{

  /*val session = SparkSession.builder()
                           .appName("RDDconsumer")
                           .master("local[*]")
                           .getOrCreate()

  val ssc = new StreamingContext(session.sparkContext, Seconds(5))
  val kafkaParams= Map[String,Object](
    "bootstrap.servers"-> "localhost:9092",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> "myconsumergroup",
    "auto.offset.reset" -> "latest",
    "enable.auto.commit" -> (true: java.lang.Boolean)

  )
  
  val topics = Array("peaceWatcherReport")
  val stream = KafkaUtils.createDirectStream[String,String](
    ssc,
    PreferConsistent,
    Subscribe[String,String](topics,kafkaParams)
  )
  //stream.saveAsTextFile("src/main/resources/peaceWatcherReportData")
  stream.map{record => (record.key(), record.value)}.print()
  stream.foreachRDD{ rdd =>
    rdd.saveAsTextFile ("src/main/resources/peaceWatcherReportData");
    }
  ssc.start() // Start the computation
  ssc.awaitTermination()*/
  

val spark = SparkSession.builder()
                           .appName("consumer")
                           .master("local[*]")
                           .getOrCreate()
  val df = spark
  .readStream
  .format("kafka")
  .option("kafka.bootstrap.servers", "localhost:9092")
  .option("subscribe", "peaceWatcherReport")
  .option("startingOffsets","earliest")
  .load()
  
  /*val query = df.writeStream
  .outputMode("append")
  .format("console")
  .start()
  query.awaitTermination()*/
  val reportData= df.selectExpr("CAST(value AS STRING)")
  val batchInterval = 30000
  val query= reportData.writeStream
    .trigger(Trigger.ProcessingTime("30 seconds"))
    .option("header","false")
    .option("delimiter","|")
    .format("csv") 
    .option("checkpointLocation", "src/main/resources/checkpoints")       // can be "orc", "json", "csv", etc.
    .option("path", "src/main/resources/peaceWatcherReportData")
    .start()
  query.awaitTermination()
  spark.close
  //sc.stop()
}