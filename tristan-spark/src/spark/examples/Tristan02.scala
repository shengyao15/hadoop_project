package spark.examples

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext.rddToPairRDDFunctions
import java.util.Hashtable
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import java.util.ArrayList

/**
 * hbase  
 * learn according to CollaborativeFilterByUserLocation.scala
 * 
 */

//spark-submit --driver-class-path "/usr/hbase/hbase-0.96.2-hadoop2/lib/*"  --class spark.examples.Tristan02 --name Tristan02
object Tristan02 {

  val conf = new SparkConf();
  conf.setAppName("SparkWordCount");
  conf.setMaster("spark://hadoop1:7077");
  val sc = new SparkContext(conf);

  def main(args: Array[String]) {

    val conf = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.quorum", "hadoop1");

    conf.set(TableInputFormat.INPUT_TABLE, "students")
    println("1--------")
    val studentRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])
    println("2--------")
    println(studentRDD)
    println(studentRDD.count())

    val s2 = studentRDD.map {
      case (w, r) =>
        (Bytes.toString(r.getValue("info".getBytes(), "country".getBytes())), Bytes.toString(r.getValue("info".getBytes(), "name".getBytes())))
    }

    val s3 = s2.groupBy(_._1)

    val s4 = s3.map {
      case (k, seq) =>
        val list = new ArrayList[String]()
        seq.foreach {
          case (group, name) => list.add(name)
        }
        (k, list)
    }
    
    s4.collect.foreach(println)

  }

}