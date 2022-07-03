import org.apache.hadoop.shaded.org.apache.commons.io.FileUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import java.io.File
import java.util.concurrent.TimeUnit




object Main {
  def main(args: Array[String]): Unit = {

    /*
    val t0 = System.nanoTime
    val resultAllNative = NativeCounter.countWords(10)
    println("Result (native-all): " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime - t0) + "s")
    resultAllNative.zipWithIndex.foreach { case (item, index) =>
      println("" + (index + 1) + ". " + item._1 + " (" + item._2 + "x)")
    }

    val t1 = System.nanoTime
    val resultsByLanguageNative = NativeCounter.countWordsByLanguage()
    println("Result (native-by-language): " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime - t1) + "s")
    resultsByLanguageNative.foreach(x => {
      println(x._1)
      x._2.zipWithIndex.foreach { case (item, index) =>
        println("" + (index + 1) + ". " + item._1 + " (" + item._2 + "x)")
      }
      println("")
    })
*/
    val t2 = System.nanoTime
    val resultsAll = ScalaCounter.countWords()
    println("Result (scala-all): " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime - t2) + "s")
    resultsAll.zipWithIndex.foreach { case (item, index) =>
      println("" + (index + 1) + ". " + item._1 + " (" + item._2 + "x)")
    }

    val t3 = System.nanoTime
    val resultsByLanguage = ScalaCounter.countWordsByLanguage()
    println("Result (scala-by-language): " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime - t3) + "s")
    resultsByLanguage.foreach(x => {
      println(x._1)
      x._2.zipWithIndex.foreach { case (item, index) =>
        println("" + (index + 1) + ". " + item._1 + " (" + item._2 + "x)")
      }
      println("")
    })
  }

  /*
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
  */

}