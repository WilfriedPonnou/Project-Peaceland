package model
import scala.util.Random
import model.Citizen
import faker._
import java.time.LocalDateTime

case class Reports(peaceWatcherId: Int, lat: Double, lon: Double, data: List[Citizen], words: List[String], date: String){}

object Reports{ 
  def createRandomReport() = {

    val r = new Random();
    val id = r.nextInt(124)
    val latitude = (-90) + (90 - (-90)) * r.nextDouble();
    val longitude = (-180) + (180 - (-180)) * r.nextDouble();
    val nbCitizen = 2 + r.nextInt(10)
    val citizens = List.fill(nbCitizen)(Citizen.createRandomCitizen())
    val nbWord = 2 + r.nextInt(10)
    val wordsHeards = Lorem.words(nbWord)
    val arrayOfDay = Array("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su")
    val randomDayOfTheWeek = r.nextInt(7)
    val dayOfTheWeek = arrayOfDay(randomDayOfTheWeek)
    val randomHour = r.nextInt(24)
    val hour = randomHour.toString
    val strDay = dayOfTheWeek + ":" + hour

    Reports(id, latitude, longitude, citizens, wordsHeards, strDay)

  }
  def stringifyReport(r: Reports) : String ={
    val citizens = r.data.mkString(",")
    val words = r.words.mkString(",")
    val stringReport = r.peaceWatcherId + ";" + r.lat + ";" + r.lon + ";" + citizens + ";" + words + ";" + r.date
    stringReport
  }
  def stringToReport(s: String): Reports = {
    val reportAttributes = s.split(";")
    Reports(reportAttributes(0).toInt, reportAttributes(1).toDouble, reportAttributes(2).toDouble, 
    reportAttributes(3).split(",").toList.map(citizenData=>Citizen.stringToCitizen(citizenData)),
    reportAttributes(4).split(",").toList,
    reportAttributes(5))
  }

}