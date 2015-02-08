package spark.examples


import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{HColumnDescriptor, HTableDescriptor, HBaseConfiguration}
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.rdd.PairRDDFunctions
import org.apache.spark.util.Vector
import scala.collection.immutable.IndexedSeq
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf


/**
 * spark-submit --class spark.examples.SparkWordCount --name wc SparkWordCount.jar
 * 
 */
object SparkWordCount {
    
   val conf = new SparkConf()
    conf.setAppName("SparkWordCount")
    val sc = new SparkContext(conf)
   
  def main(args: Array[String]) {
    
    val rdd = sc.textFile("/in/README.md")

    println("\n xxxxxxxxxxxxxxx2")
    
    rdd.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _).map(x => (x._2, x._1)).sortByKey(false).map(x => (x._2, x._1)).collect.foreach(println)
  }
  
}