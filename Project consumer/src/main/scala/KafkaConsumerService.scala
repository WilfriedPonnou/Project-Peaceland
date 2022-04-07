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

object KafkaConsumerService extends App{

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


  val reportData= df.selectExpr("CAST(value AS STRING)")

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

}