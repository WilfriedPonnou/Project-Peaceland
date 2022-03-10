package model
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.common.serialization.StringDeserializer

import java.util.Properties

object ReportProducerTest extends App {

    val props:Properties = new Properties()
    props.put("bootstrap.servers","localhost:9092")
    props.put("key.serializer",
              "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer",
              "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)
    val topic = "peaceWatcherReport"

    def reportLoop(kafkaproducer : KafkaProducer[String, String]) {
    
      val report = Reports.createRandomReport()
      val record = new ProducerRecord[String, String](topic, report.peaceWatcherId.toString, report.toString)
      producer.send(record)
      Thread.sleep(5000)
      reportLoop(kafkaproducer)

    }

    try {

      reportLoop(producer)
    
    }
    catch {
      
      case e:Exception => e.printStackTrace()
    
    }
    finally {
      
      producer.close()

    }

}