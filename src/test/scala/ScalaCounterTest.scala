import org.scalatest.flatspec.AnyFlatSpec

class ScalaCounterTest extends AnyFlatSpec{

  "The result" should "not include any stop or empty word for all words" in {
    val result = ScalaCounter.countWords(10, "src/test/resources/texts", "src/test/resources/stopwords.txt")
    val words = result.flatMap(_._1)
    assert(words.contains("und") === false)
    assert(words.contains("and") === false)
    assert(words.contains("") === false)
  }

  "The result" should "include correct word count for all words" in {
    val result = ScalaCounter.countWords(10, "src/test/resources/texts", "src/test/resources/stopwords.txt")
    assert(result.exists((entry) => entry._1 === "ich" && entry._2 === 3))
    assert(result.exists((entry) => entry._1 === "me" && entry._2 === 3))
    assert(result.exists((entry) => entry._1 === "are" && entry._2 === 1))
  }

  "The result" should "not include any stop or empty word for words by language" in {
    val result = ScalaCounter.countWordsByLanguage(10, "src/test/resources/texts", "src/test/resources/stopwords.txt")
    val wordsGerman = result("German").flatMap(_._1)
    val wordsEnglish = result("English").flatMap(_._1)
    assert(wordsGerman.contains("und") === false)
    assert(wordsEnglish.contains("and") === false)
    assert(wordsGerman.contains("") === false)
    assert(wordsEnglish.contains("") === false)
  }

  "The result" should "include correct word count for words by language" in {
    val result = ScalaCounter.countWordsByLanguage(10, "src/test/resources/texts", "src/test/resources/stopwords.txt")
    assert(result("German").exists((entry) => entry._1 === "ich" && entry._2 === 3))
    assert(result("English").exists((entry) => entry._1 === "me" && entry._2 === 3))
    assert(result("English").exists((entry) => entry._1 === "are" && entry._2 === 1))
  }
}
