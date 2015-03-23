package week4

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{ Seconds, StreamingContext }
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.SparkContext

object PeopleTest {
  case class Person(name: String, age: Int)
  def main(args: Array[String]) {
    println("2222222222222222")
    val conf = new SparkConf();
    conf.setAppName("SparkSQL");
    conf.setMaster("local")
    val sc = new SparkContext(conf)

    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    import sqlContext._

    val people = sc.textFile("people.txt").map(_.split(",")).map(p => Person(p(0), p(1).trim.toInt))
    people.registerAsTable("people")
    val teenagers = sql("SELECT name FROM people WHERE age >= 13 AND age <= 19")
    teenagers.map(t => "Name: " + t(0)).collect().foreach(println)
  }
}