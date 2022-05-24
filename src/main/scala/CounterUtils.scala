import org.apache.spark.rdd.RDD

object CounterUtils {

  def countWords(textFile: RDD[String]): RDD[(String, Int)] = {
    textFile.flatMap(x => x.toLowerCase().split("\\P{L}+"))
      .map(x => (x, 1)).reduceByKey(_+_)
  }

}
