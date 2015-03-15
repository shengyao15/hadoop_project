package spark.examples

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext.rddToPairRDDFunctions

/**
 * scala learning
 */
object Tristan01 {

  val conf = new SparkConf();
  conf.setAppName("SparkWordCount");
  conf.setMaster("spark://hadoop1:7077");
  val sc = new SparkContext(conf);

  def main(args: Array[String]) {

    val a = sc.parallelize(List(1, 2, 1, 3))
    val b = a.map((_, "b"))
    val c = a.map((_, "c"))
    b.foreach(println)
    b.cogroup(c).collect.foreach(println)

    //parallelize()

    //    println(add(3))
    //
    //    println(max(45, 30))
    //
    //    greet()
    //    greets()
    //    pair()
  }

  private var sum = 100

  def add(b: Int): Int = {
    sum += b
    sum
  }

  def max(x: Int, y: Int): Int = {
    if (x > y) {
      x
    } else {
      y
    }
  }

  def greet() = println("hello,world")

  def greets() = {
    val greetStrings = new Array[String](4)
    greetStrings(0) = "this"
    greetStrings(1) = " is"
    greetStrings(2) = " a"
    greetStrings(3) = " war!\n"

    for (i <- 0 to 3) {
      print(greetStrings(i))
    }

    greetStrings.foreach(print)
  }

  def pair() = {
    val pair = (1, "name", "address", "phone")
    println(pair._1)
    println(pair._2)

  }

  def test1() {
    val rdd = sc.textFile("c:/README.md");
    val rdd2 = rdd.flatMap(_.split(" "));
    val rdd3 = rdd2.map((_, 1));
    val rdd4 = rdd3.reduceByKey(_ + _);
    val rdd5 = rdd4.map(x => (x._2, x._1));
    rdd5.foreach(println)
  }

  //distinct
  def parallelize() {
    val rdd = sc.parallelize(Array("Gnu", "Cat", "dog", "Rat", "Rat", "Cat", "Cat", "dog"))
    rdd.distinct.foreach(println)
  }
}