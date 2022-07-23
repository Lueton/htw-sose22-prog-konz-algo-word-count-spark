import org.apache.hadoop.shaded.org.apache.commons.io.FileUtils
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import java.io.File

object ScalaCounter extends Counter {
  val sc: SparkContext = new SparkContext(new SparkConf().setAppName("ScalaCounter").setMaster("local[1]"))

  override def countWords(limit: Int = 10, filesPath: String, stopWordsFile: String): List[(String, Int)] = {
    val outputPath = "output/allTexts"
    FileUtils.deleteQuietly(new File(outputPath))
    val counted = count(readFiles(filesPath), limit, stopWordsFile).cache()
    counted.repartition(1).saveAsTextFile(outputPath)
    counted.collect().toList
  }

  override def countWordsByLanguage(limit: Int = 10, filesPath: String, stopwordPath: String): Map[String, List[(String, Int)]] = {
    val outputPath = "output/sortedByLanguage"
    FileUtils.deleteQuietly(new File(outputPath))
    val textFiles = readFiles(filesPath).map(x => (x._1.split("/").dropRight(1).last, x._2)).cache()
    val languages = textFiles.map(x => x._1).distinct.collect()
    val mappedLanguages = languages.map(l => (l, count(textFiles.filter(_._1.contains(l)), limit, stopwordPath).collect().toList)).toMap
    sc.parallelize(mappedLanguages.toSeq).repartition(1).saveAsTextFile(outputPath)
    mappedLanguages
  }

  def count(files: RDD[(String, String)], limit: Int, stopWordsFile: String): RDD[(String, Int)] = {
    val stopWords: Broadcast[Set[String]] = sc.broadcast(sc.textFile(stopWordsFile).flatMap(x => x.split("\\P{L}+")).collect.toSet)
    val words = files.flatMap(x => x._2.toLowerCase().split("\\P{L}+"))
    val filteredWords = words.filter(!stopWords.value.contains(_))
    val wordsWithCount = filteredWords.map(x => (x, 1)).reduceByKey(_ + _)
    val sortedWordsWithCount = wordsWithCount.sortBy(_._2, ascending = false)
    sc.parallelize(sortedWordsWithCount.take(limit))
  }

  def readFiles (filesPaths: String) : RDD[(String, String)] = {
    val files = sc.wholeTextFiles(filesPaths + "/*")
    files.filter(file => file._1.contains(".txt")) //.cache() makes every computation faster after the first run (falsifies time measuring)
  }
}
