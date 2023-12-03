// Advent of Code 2023, Day 01.
// By Sebastian Raaphorst, 2023.

package day01

fun answer1(input: String): Int =
    input.lines().sumOf { line ->
        val firstDigit = line.firstOrNull(Char::isDigit)?.digitToInt() ?: 0
        val lastDigit = line.lastOrNull(Char::isDigit)?.digitToInt() ?: 0
        10 * firstDigit + lastDigit
    }

private val replaceWords = listOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9
)

fun answer2(input: String): Int {
    // A generic function to process a line forwards or backwards to get the "digit."
    // There can be overlaps, e.g. twone, so we have to be careful to get the right answer.
    // 4twone should give 41 (4tw1) and not 42 (42ne), hence the bidirectional processing.
    fun aux(s: String,
            extractor: (String) -> Char,
            matcher: (String, String) -> Boolean,
            dropper: (String, Int) -> String): Int =
        if (extractor(s).isDigit()) extractor(s).digitToInt()
        else {
            val word = replaceWords.find { w -> matcher(s, w.first) }
            word?.second ?: aux(dropper(s, 1), extractor, matcher, dropper)
        }

    val forward = { s: String -> aux(s, String::first, String::startsWith, String::drop) }
    val backward = { s: String -> aux(s, String::last, String::endsWith, String::dropLast) }
    return input.lines().sumOf { line -> 10 * forward(line) + backward(line) }
}

fun main() {
    val input = object {}.javaClass.getResource("/day01.txt")!!.readText()

    println("--- Day 1: Trebuchet! ---")

    // Answer 1: 55621
    println("Part 1: ${answer1(input)}")

    // Answer 2: 53592
    print("Part 2: ${answer2(input)}")
}
