// Advent of Code 2023, Day 13.
// By Sebastian Raaphorst, 2023.

package day13

typealias Data = List<Long>
typealias RowData = Data
typealias ColData = Data
typealias Terrain = Pair<RowData, ColData>

// Determine if there is a perfect line of reflection between data[line] and data[line+1].
private fun checkLine(data: Data, line: Int): Boolean =
    (0..line).all {
        // Match idx to its appropriate line on the other side, if it exists.
        val firstLineIdx = line - it
        val secondLineIdx = line + it + 1
        secondLineIdx >= data.size || data[firstLineIdx] == data[secondLineIdx]
    }

// Check to see if there is a line of reflection after line it.
// If there is, we want to add 1 to indicate how many lines are before the line.
// If not, we return null.
private fun checkData(data: Data): Int? =
    (0..(data.size - 2)).firstOrNull { checkLine(data, it) }?.let { it + 1 }

// Check the rows for a line of reflection:
// 1. terrain.first are the rows, so if a value is returned, there are that many rows above it; and
// 2. terrain.second are the columns, so if a value is returned, there are that many columns left of it.
// As per the puzzle description:
// 1. return 100 * the number of rows above a line of horizontal line of reflection; or
// 2. return the number of columns to the left of a line of reflection.
private fun checkTerrain(terrain: Terrain): Int =
    (checkData(terrain.first) ?: 0) * 100 + (checkData(terrain.second) ?: 0)

// Convert a string of # and . to a Long corresponding to 1 and 0 respectively.
private fun stringToLong(bitString: String): Long =
    bitString.fold(0L) { acc, char ->
        (acc shl 1) or (if (char == '#') 1L else 0L)
    }

private fun parseBoard(data: List<String>): List<Long> =
    data.map(::stringToLong)

private fun parse(input: String): List<Terrain> =
    input.trim().split("\n\n").map {
        val rows = it.lines()
        val cols = (0..<rows.first().length).map { colIdx ->
            rows.joinToString(separator = "") { it[colIdx].toString() }
        }
        parseBoard(rows) to parseBoard(cols)
    }

private fun answer(input: String): Int =
    parse(input).sumOf(::checkTerrain)

fun answer1(input: String): Int =
    answer(input)

fun answer2(input: String): Int = TODO()

fun main() {
    val input = object {}.javaClass.getResource("/day13.txt")!!.readText()

    println("--- Day 13: Point of Incidence ---")

    // Answer 1: 33356
    println("Part 1: ${answer1(input)}")

    // Answer 2:
//    println("Part 2: ${answer2(input)}")
}