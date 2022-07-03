import java.io.File

object NativeCounter extends Counter {

  val dictionary = {
    val source = scala.io.Source.fromFile(getClass.getResource("/stopwords/stopwords.txt").getPath)
    val lines = try source.mkString finally source.close()
    lines.toLowerCase().split("\\P{L}+").filter(_.nonEmpty)
  }

  override def countWords(limit: Int = 10): List[(String, Int)] = {
    val files = readFiles()
    val fileCount = files.length
    val counts: List[(String, Int)] = files.zipWithIndex.flatMap((x) => count(x._1, x._2, fileCount)).groupBy(_._1).mapValues(_.map(_._2).sum).toList
    counts.sortWith(_._2 > _._2).take(limit)
  }

  override def countWordsByLanguage(limit: Int = 10): Map[String, List[(String, Int)]] = {
    val files = readFiles()
    val languages = files.map(x => x.getPath.split("/").dropRight(1).last).distinct
    languages.map(l => {
      val filesForLanguage = files.filter(_.getPath.split("/").dropRight(1).last.contains(l))
      val filesCount = filesForLanguage.length
      (l, filesForLanguage.zipWithIndex.flatMap((x) => count(x._1, x._2, filesCount)).groupBy(_._1).mapValues(_.map(_._2).sum).toList.sortWith(_._2 > _._2).take(limit))
    }).toMap
  }

  def getRecursiveListFiles(f: File): Array[File] = {
    val these = f.listFiles
    these ++ these.filter(_.isDirectory).flatMap(getRecursiveListFiles)
  }

  def readFiles(): Array[File] = {
    getRecursiveListFiles(new File(getClass.getResource("/texts").getPath)).filter(p => p.isFile && p.getPath.contains(".txt"))
  }

  def count(file: File, i: Int, files: Int): List[(String, Int)] = {
    val source = scala.io.Source.fromFile(file)
    val content = try source.mkString finally source.close()
    val result = content.toLowerCase().split("\\P{L}+")
      .filter(_.nonEmpty)
      .filter(!dictionary.contains(_))
      .foldLeft(Map.empty[String, Int]) {
        (count, word) => count + (word -> (count.getOrElse(word, 0) + 1))
      }.toList.sortWith(_._2 > _._2)
    println("" + (i + 1) + "/" + files)
    result
  }

}
