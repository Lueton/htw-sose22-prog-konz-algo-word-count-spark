import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object ScalaCounter extends Counter {
  val sc: SparkContext = new SparkContext(new SparkConf().setAppName("ScalaCounter").setMaster("local[*]"))

  override def countWords(limit: Int = 10): List[(String, Int)] = {
    count(readFiles(), limit)
  }

  override def countWordsByLanguage(limit: Int = 10): Map[String, List[(String, Int)]] = {
    val textFiles = readFiles().map(x => (x._1.split("/").dropRight(1).last, x._2)).cache()
    val languages = textFiles.map(x => x._1).distinct.collect
    languages.map(l => (l, count(textFiles.filter(_._1.contains(l)), limit))).toMap
  }

  def count(files: RDD[(String, String)], limit: Int): List[(String, Int)] = {
    val stopWords: Broadcast[Set[String]] = sc.broadcast(sc.textFile(getClass.getResource("/stopwords/stopwords.txt").getPath).flatMap(x => x.split("\\P{L}+")).collect.toSet)
    val words = files.flatMap(x => x._2.toLowerCase().split("\\P{L}+"))
    val filteredWords = words.filter(!stopWords.value.contains(_))
    val wordsWithCount = filteredWords.map(x => (x, 1)).reduceByKey(_ + _)
    val sortedWordsWithCount = wordsWithCount.sortBy(_._2, ascending = false)
    sortedWordsWithCount.take(limit).toList
  }

  def readFiles () : RDD[(String, String)] = {
    val files = sc.wholeTextFiles(getClass.getResource("/texts").getPath + "/*")
    files.filter((file) => file._1.contains(".txt")).cache()
  }
}
