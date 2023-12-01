// Advent of Code 2023, Day 01.
// By Sebastian Raaphorst, 2023.

package day01

private fun processDigits(lines: List<String>): Int =
    // This could greatly be optimized. We can stop after first digit in either direction.
    lines.sumOf { line ->
        val digits = line.filter(Char::isDigit)
        10 * digits.first().digitToInt() + digits.last().digitToInt()
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
    fun auxFirst(remain: String): Int =
        if (remain.first().isDigit()) remain.first().digitToInt()
        else {
            val word = replaceWords.find { w -> remain.startsWith(w.first) }
            word?.second ?: auxFirst(remain.drop(1))
        }

    // Find the last digit in each line.
    fun auxLast(remain: String): Int =
        if (remain.last().isDigit()) remain.last().digitToInt()
        else {
            val word = replaceWords.find { w -> remain.endsWith(w.first) }
            word?.second ?: auxLast(remain.dropLast(1))
        }

    return lines.sumOf { l -> 10 * auxFirst(l) + auxLast(l) }
}


fun main() {
    val input = object {}.javaClass.getResource("/day01/20231201")!!.readText()
    println("--- Day1: Trebuchet! ---")

//    // Answer 1: 55621
    println("Part 1: ${answer1(input)}")

    // Answer 2: 53587 is too low... should be 53592.
    print("Part 2: ${answer2(input)}")
}
