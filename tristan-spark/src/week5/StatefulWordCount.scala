package week5

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.StreamingContext._

object StatefulWordCount {
  def main(args: Array[String]) {
        Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
    val updateFunc = (values: Seq[Int], state: Option[Int]) => {
      val currentCount = values.foldLeft(0)(_ + _)
      val previousCount = state.getOrElse(0)
      Some(currentCount + previousCount)
    }

    val conf = new SparkConf().setAppName("StatefulWordCount").setMaster("local[2]")
    val sc = new SparkContext(conf)

    //创建StreamingContext
    val ssc = new StreamingContext(sc, Seconds(5))
    ssc.checkpoint("aa")

    //获取数据
    val lines = ssc.socketTextStream("localhost",9999)
    val words = lines.flatMap(_.split(","))
    val wordCounts = words.map(x => (x, 1))

    //使用updateStateByKey来更新状态
    val stateDstream = wordCounts.updateStateByKey[Int](updateFunc)
    stateDstream.print()
    ssc.start()
    ssc.awaitTermination()
  }
}