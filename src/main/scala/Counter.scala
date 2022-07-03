trait Counter {
  def countWords(limit: Int) : List[(String, Int)]
  def countWordsByLanguage(limit: Int) : Map[String, List[(String, Int)]]
}
