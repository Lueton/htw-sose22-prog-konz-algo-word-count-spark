trait Counter {
  def countWords(limit: Int, filesPath: String, stopwordPath: String) : List[(String, Int)]
  def countWordsByLanguage(limit: Int, filesPath: String, stopwordPath: String) : Map[String, List[(String, Int)]]
}
