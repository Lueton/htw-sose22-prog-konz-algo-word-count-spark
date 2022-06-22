import java.util.concurrent.TimeUnit

object Main {
  def main(args: Array[String]): Unit = {

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

    val t2 = System.nanoTime
    val resultsAll = ScalaCounter.countWords();
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
}




/*
var linesRdd = context.textFile("/Users/lueton/IdeaProjects/htw-sose22-prog-konz-algo-word-count-spark/texts/German/Casanovas Heimfahrt - Arthur Schnitzler.txt")
var words1 = linesRdd.filter(x => x.nonEmpty).flatMap(x => x.toLowerCase().split("\\P{L}+"))
words1.take(100).foreach(f => println(f))
var wordsKv  = words1.filter(!broadcastStopWords.value.contains(_)).map(x => (x, 1)).reduceByKey(_ + _)
var output1 = wordsKv
println(output1.sortBy(_._2, ascending = false).take(10).mkString("Array(", ", ", ")"))
context.stop()
*/