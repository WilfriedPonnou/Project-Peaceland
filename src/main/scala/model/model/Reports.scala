package model
import scala.util.Random
import model.Citizen
import faker._

case class Reports(peaceWatcherId: Int, lat: String, lon: String, data: List[Citizen], words: List[String], date: String){}

object Reports{ 
  def createRandomReport() = {
  
    val id = scala.util.Random.nextInt(124)
    val latitude = Address.latitude
    val longitude = Address.longitude
    val nbCitizen = 2 + scala.util.Random.nextInt(10)
    val citizenInArea = List.fill(nbCitizen)(Citizen.createRandomCitizen())
    val nbWord = 2 + scala.util.Random.nextInt(10)
    val wordsHeards = Lorem.words(nbWord)

    val random = new scala.util.Random
    val arrayOfDay = Array("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su")
    val randomDayOfTheWeek = random.nextInt(7)
    val dayOfTheWeek = arrayOfDay(randomDayOfTheWeek)
    val randomHour = random.nextInt(24)
    val hour = randomHour.toString
    val strDay = dayOfTheWeek + ":" + hour
    
    Reports(id, latitude, longitude, citizenInArea, wordsHeards, strDay)

  }
}