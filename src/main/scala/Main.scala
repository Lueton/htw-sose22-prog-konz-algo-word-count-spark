import org.apache.spark.{SparkConf, SparkContext}

object Main {
  def main(args: Array[String]): Unit = {
    println("Hello world!")
  }
  val conf = new SparkConf()
    .setAppName("WordCounter")
    .setMaster("local[*]")
  val sc = new SparkContext(conf)
  val file = sc.textFile("texts")
  val map = CounterUtils.countWords(file)
  map.saveAsTextFile("output/spark_output")
  sc.stop()
}