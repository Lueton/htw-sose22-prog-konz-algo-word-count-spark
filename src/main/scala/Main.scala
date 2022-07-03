import org.apache.hadoop.shaded.org.apache.commons.io.FileUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import java.io.File




object Main {
  def main(args: Array[String]): Unit = {
    println("Hello world!")
  }
  val outputPath = "output/spark_output"
  FileUtils.deleteQuietly(new File(outputPath))
  val conf: SparkConf = new SparkConf()
    .setAppName("WordCounter")
    .setMaster("local[*]")
  val sc = new SparkContext(conf)
  val file: RDD[String] = sc.textFile("texts/Dutch")
  val map: RDD[(String, Int)] = CounterUtils.countWords(file)
  map.saveAsTextFile(outputPath)
  sc.stop()
}