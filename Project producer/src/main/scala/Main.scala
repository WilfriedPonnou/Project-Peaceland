import model._
import service._
import java.util.Properties

object Main {
    val kafka = new KafkaProducerService 

    def main(args: Array[String]) : Unit = {
        kafka.reportLoop(kafka.producer)
    }
}

