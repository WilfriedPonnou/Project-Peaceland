package model
import scala.util.Random
import faker._

case class Citizen(citizenName: String, citizenPeacescore: Int){
    val name = citizenName
    val peacescore = citizenPeacescore

    override def toString() : String = {
    
      val str_to_return = "" + name + ":" + peacescore
      str_to_return

    }
}
object Citizen{
    def createRandomCitizen() = {

      val name = Name.name
      
      val start = 0
      val end = 100
      val numberRandom = new scala.util.Random
      val peacescore = start + numberRandom.nextInt(( end - start ) + 1 )

      Citizen(name, peacescore)

    }
  
}