package service
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.common.serialization.StringDeserializer
import model._

import java.util.Properties

class KafkaProducerService {


      val props: Properties = new Properties()
      props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
      props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
      props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])

      val producer = new KafkaProducer[String, String](props)
      val topic = "peaceWatcherReport"

      def reportLoop(kafkaproducer : KafkaProducer[String, String]) {

        val report = Reports.createRandomReport()
        val record = new ProducerRecord[String, String](topic, report.peaceWatcherId.toString,Reports.stringifyReport(report) )
        val metadata=producer.send(record)
        printf(s"sent record(key=%s value=%s) " +
        "meta(partition=%d, offset=%d)\n",
        record.key(), record.value(), 
        metadata.get().partition(),
        metadata.get().offset())
    
        Thread.sleep(5000)
        reportLoop(kafkaproducer)

      }

}