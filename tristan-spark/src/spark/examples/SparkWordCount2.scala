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
 * local run
 */
object SparkWordCount2 {
    val conf = new SparkConf();
    conf.setAppName("SparkWordCount");
    conf.setMaster("local")
    val sc = new SparkContext(conf);
    
  def main(args: Array[String]) {

    t0
  }
  
  //分开来写
   def t0(){
    val rdd = sc.textFile("README.md");
    val rdd2 = rdd.flatMap(_.split(" "));
    val rdd3 = rdd2.map((_, 1));
    val rdd4 = rdd3.reduceByKey(_ + _);
    val rdd5 = rdd4.map(x => (x._2, x._1));
    rdd5.take(10).foreach(println)
  }
   
  //去掉_的用法 
  def t1(){
    val rdd = sc.textFile("README.md");
    val rdd2 = rdd.flatMap(s=>s.split(" "));
    val rdd3 = rdd2.map(s=>(s,1));
    val rdd4 = rdd3.reduceByKey((s1,s2)=>s1+s2);
    val rdd5 = rdd4.map(s=>(s._2,s._1));
  }
  
  //parallelize Array
  def t2(){
    val rdd1 = sc.parallelize(Array("this is a war ", "this is a war2"));
    val rdd2 = rdd1.flatMap(s=>s.split(" "));
    val rdd3 = rdd2.map(s=>(s,1));
    val rdd4 = rdd3.reduceByKey((s1,s2)=>s1+s2);
    val rdd5 = rdd4.map(s=>(s._2,s._1));
  }
  
  //reduce
   def t3(){
    val rdd1 = sc.parallelize(Array("this is a war ", "this is a war2","this is a war3. welcome"));
	val lineLengths = rdd1.map(s => s.length)  //Array(14, 14, 23)
	val totalLength = lineLengths.reduce((a, b) => a + b)  //Int = 51
    
  }
   
  //join
  def t4(){
    var table1 = sc.parallelize(Array("k1,this", "k2,is","k3,a","k4,war"));
	var table2 = sc.parallelize(Array("k1,this", "k2,is","k3,a","k4,war2"));
	var pairs = table1.map{x =>
	  var pos = x.indexOf(',')
	  (x.substring(0, pos), x.substring(pos + 1))
	}
	var pairs2 = table2.map{x =>
	  var pos = x.indexOf(',')
	  (x.substring(0, pos), x.substring(pos + 1))
	}
	var result = pairs.join(pairs2)
	result.take(100)
  }
  
  
  //groupByKey
   def t5(){
    val rdd1 = sc.parallelize(Array("this is a war ", "this is a war2", "this is a war3"));
    val rdd2 = rdd1.flatMap(s=>s.split(" "));
    
    val rdd3 = rdd2.map(s=>(s,1));
    val rdd4 = rdd3.groupByKey;  //Array((this,ArrayBuffer(1, 1, 1)), (is,ArrayBuffer(1, 1, 1)), (war2,ArrayBuffer(1)), (war3,ArrayBuffer(1)), (a,ArrayBuffer(1, 1, 1)), (war,ArrayBuffer(1)))
  }
    
  //union
  def t6(){
    val rdd = sc.parallelize(List(('a',1),('a',2)))
    val rdd2 = sc.parallelize(List(('b',1),('b',2)))
    val res3 =  rdd union rdd2
    res3.collect
    
    
  }
   
  //replace flatMap
  def t7(){
    val rdd = sc.textFile("README.md");
    val rdd2 = rdd.map(_.split(" "));
    val rdd3 = rdd2.map((_, 1));
    val rdd4 = rdd3.reduceByKey(_ + _);
    val rdd5 = rdd4.map(x => (x._2, x._1));
  }
  //filter
  
  
  //case
  def t8(){
    val rdd1 = sc.parallelize(Array("this is a war ", "this is a war2"));
    val rdd2 = rdd1.flatMap(s=>s.split(" "));
    val rdd3 = rdd2.map(_.split(" "));
    
     val rdd4 = rdd2.map{case(s)=>
       var t = s+"t"
       (t,1)};
    
    
  }
  
  
  
  
  //
  //
  //
  //
  //
  //
  //
}