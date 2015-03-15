package week5

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.StreamingContext._


object HdfsWordCount {
  
  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("HdfsWordCount").setMaster("local[2]")
    // Create the context
    val ssc = new StreamingContext(sparkConf, Seconds(20))

    val lines = ssc.textFileStream("e://tmp")
    val words = lines.flatMap(_.split(" "))
    val wordCounts = words.map(x => (x, 1)).reduceByKey(_ + _)
    wordCounts.print()
    ssc.start()
    ssc.awaitTermination()
  }
}