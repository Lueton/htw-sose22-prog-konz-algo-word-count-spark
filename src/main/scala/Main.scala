import java.util.concurrent.TimeUnit




object Main {
  def main(args: Array[String]): Unit = {

    val t0 = System.nanoTime
    val resultAllNative = NativeCounter.countWords(1010, "src/main/resources/texts", "src/main/resources/stopwords/stopwords.txt")
    println("Result (native-all): " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime - t0) + "s")
    resultAllNative.zipWithIndex.foreach { case (item, index) =>
      println("" + (index + 1) + ". " + item._1 + " (" + item._2 + "x)")
    }

    val t1 = System.nanoTime
    val resultsByLanguageNative = NativeCounter.countWordsByLanguage(10, "src/main/resources/texts", "src/main/resources/stopwords/stopwords.txt")
    println("Result (native-by-language): " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime - t1) + "s")
    resultsByLanguageNative.foreach(x => {
      println(x._1)
      x._2.zipWithIndex.foreach { case (item, index) =>
        println("" + (index + 1) + ". " + item._1 + " (" + item._2 + "x)")
      }
      println("")
    })

   val t2 = System.nanoTime
    val resultsAll = ScalaCounter.countWords(10, "src/main/resources/texts", "src/main/resources/stopwords/stopwords.txt")
    println("Result (scala-all): " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime - t2) + "s")
    resultsAll.zipWithIndex.foreach { case (item, index) =>
      println("" + (index + 1) + ". " + item._1 + " (" + item._2 + "x)")
    }

    val t3 = System.nanoTime
    val resultsByLanguage = ScalaCounter.countWordsByLanguage(10, "src/main/resources/texts", "src/main/resources/stopwords/stopwords.txt")
    println("Result (scala-by-language): " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime - t3) + "s")
    resultsByLanguage.foreach(x => {
      println(x._1)
      x._2.zipWithIndex.foreach { case (item, index) =>
        println("" + (index + 1) + ". " + item._1 + " (" + item._2 + "x)")
      }
      println("")
    })

    println("Result (native-all): " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime - t0) + "s")
    println("Result (native-by-language): " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime - t1) + "s")
    println("Result (scala-all): " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime - t2) + "s")
    println("Result (scala-by-language): " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime - t3) + "s")
  }
}