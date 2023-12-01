// Advent of Code 2023, Day 01.
// By Sebastian Raaphorst, 2023.

package day01

private fun processDigits(lines: List<String>): Int =
    lines.sumOf { line ->
        val firstDigit = line.firstOrNull(Char::isDigit)?.digitToInt() ?: 0
        val lastDigit = line.lastOrNull(Char::isDigit)?.digitToInt() ?: 0
        10 * firstDigit + lastDigit
    }

fun answer1(input: String): Int =
    processDigits(input.trimIndent().lines())

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
    val lines = input.trimIndent().lines()

    // Find the first digit in each line.
    fun auxForward(remain: String): Int =
        if (remain.first().isDigit()) remain.first().digitToInt()
        else {
            val word = replaceWords.find { w -> remain.startsWith(w.first) }
            word?.second ?: auxForward(remain.drop(1))
        }

    // Find the last digit in each line.
    fun auxBackward(remain: String): Int =
        if (remain.last().isDigit()) remain.last().digitToInt()
        else {
            val word = replaceWords.find { w -> remain.endsWith(w.first) }
            word?.second ?: auxBackward(remain.dropLast(1))
        }

    return lines.sumOf { line -> 10 * auxForward(line) + auxBackward(line) }
}


fun main() {
    val input = object {}.javaClass.getResource("/day01/20231201")!!.readText()
    println("--- Day1: Trebuchet! ---")

    // Answer 1: 55621
    println("Part 1: ${answer1(input)}")

    // Answer 2: 53592.
    print("Part 2: ${answer2(input)}")
}
