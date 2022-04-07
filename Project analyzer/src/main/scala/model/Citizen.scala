package model
import scala.util.Random
import faker._

case class Citizen(citizenName: String, citizenPeacescore: Int){
    val name = citizenName
    val peacescore = citizenPeacescore

}
object Citizen{
    def createRandomCitizen() = {
      
      val start = 0
      val end = 100
      val r = new Random
      val peacescore = start + r.nextInt(( end - start ) + 1 )

      Citizen(Name.name, peacescore)

    }
    def stringToCitizen(s: String) : Citizen = {

      val citizenAttributes= s.split(":")

      Citizen(citizenAttributes(0), citizenAttributes(1).toInt)

    }
  
}