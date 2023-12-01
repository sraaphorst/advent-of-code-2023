// Advent of Code 2023, Day 01.
// By Sebastian Raaphorst, 2023.

package day01

private fun processDigits(lines: List<String>): Int =
    lines.sumOf { line ->
        val digits = line.filter(Char::isDigit)
        val firstDigit = Character.getNumericValue(digits.first())
        val lastDigit = Character.getNumericValue(digits.last())
        println("Line: $line, Digits: $digits, $firstDigit$lastDigit")
        10 * firstDigit + lastDigit
    }

fun answer1(input: String): Int =
    processDigits(input.trimIndent().lines())

private val replaceWords = listOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9"
)

// Process the line to generate a new line that meets the criteria.
private tailrec fun processor(line: String, newLine: String = ""): String =
    if (line.isEmpty()) newLine
    else if (line.first().isDigit()) processor(line.drop(1), newLine + line.first())
    else {
        val word = replaceWords.find { w -> line.startsWith(w.first) }
        if (word != null)
            processor(line.removePrefix(word.first), newLine + word.second)
        else
            processor(line.drop(1), newLine + line.first())
    }

fun answer2(input: String): Int =
    processDigits(input.trimIndent().lines().map{line ->
        val abc = processor(line)
        println("Old: $line")
        println("New: $abc")
        abc
    })


fun main() {
    val input = object {}.javaClass.getResource("/day01/20231201")!!.readText()
    println("--- Day1: Trebuchet! ---")

//    // Answer 1: 55621
//    println("Part 1: ${answer1(input)}")

    // Answer 2: 53587 is too low... should be 53592.
    print("Part 2: ${answer2(input)}")
}
