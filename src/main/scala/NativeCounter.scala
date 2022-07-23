import java.io.File

object NativeCounter extends Counter {

  def getStopwords(stopWordsFile: String): Array[String] = {
    val source = scala.io.Source.fromFile(stopWordsFile)
    val lines = try source.mkString finally source.close()
    lines.toLowerCase().split("\\P{L}+").filter(_.nonEmpty)
  }

  override def countWords(limit: Int = 10, filesPath: String, stopWordsFile: String): List[(String, Int)] = {
    val files = readFiles(filesPath)
    val fileCount = files.length
    val counts: List[(String, Int)] = countInFiles(files, fileCount, stopWordsFile)
    counts.sortWith(_._2 > _._2).take(limit)
  }

  override def countWordsByLanguage(limit: Int = 10, filesPath: String, stopWordsFile: String): Map[String, List[(String, Int)]] = {
    val files = readFiles(filesPath)
    val languages = files.map(x => x.getPath.split("/").dropRight(1).last).distinct
    languages.map(l => {
      val filesForLanguage = files.filter(_.getPath.split("/").dropRight(1).last.contains(l))
      val filesCount = filesForLanguage.length
      (l, countInFiles(filesForLanguage, filesCount, stopWordsFile).sortWith(_._2 > _._2).take(limit))
    }).toMap
  }

  def countInFiles(array: Array[File], filesCount: Int, stopWordsFile: String): List[(String, Int)] = array.zipWithIndex
    .flatMap(x => count(x._1, x._2, filesCount, stopWordsFile)).groupBy(_._1).transform((x, y) => y.map(_._2).sum).toList

  def getRecursiveListFiles(f: File): Array[File] = {
    val these = f.listFiles
    these ++ these.filter(_.isDirectory).flatMap(getRecursiveListFiles)
  }

  def readFiles(filesPath: String): Array[File] = {
    getRecursiveListFiles(new File(filesPath)).filter(p => p.isFile && p.getPath.contains(".txt"))
  }

  def count(file: File, i: Int, files: Int, stopwordPath: String): List[(String, Int)] = {
    val source = scala.io.Source.fromFile(file)
    val content = try source.mkString finally source.close()
    val stopwords = getStopwords(stopwordPath)
    val result = content.toLowerCase().split("\\P{L}+")
      .filter(_.nonEmpty)
      .filter(!stopwords.contains(_))
      .foldLeft(Map.empty[String, Int]) {
        (count, word) => count + (word -> (count.getOrElse(word, 0) + 1))
      }.toList.sortWith(_._2 > _._2)
    println("" + (i + 1) + "/" + files)
    result
  }

}
