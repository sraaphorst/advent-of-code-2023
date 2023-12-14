// Advent of Code 2023, Day 13.
// By Sebastian Raaphorst, 2023.

package day13

typealias Data = List<Long>
typealias RowData = Data
typealias ColData = Data
typealias Terrain = Pair<RowData, ColData>

private enum class LineReflectionType {
    LineOfReflection,
    SmudgedLineOfReflection,
    NoReflection
}
// Determine if there is a line of reflection between data[line] and data[line+1]
// that either:
// 1. does not allow a smudge (part 1)
// 2. requires a smudge (part 2)
private fun checkLine(data: Data, line: Int): LineReflectionType {
    // Check if a Long has exactly one bit set. We use this to determine if
    fun smudgedReflection(l1: Long, l2: Long): Boolean {
        val l = l1 xor l2
        return l != 0L && (l and (l - 1) == 0L)
    }

    tailrec fun aux(idx: Int = 0, smudgeSeen: Boolean = false): LineReflectionType = when {
        // Stop if we reach line + 1 or if the reflected line does not exist.
        idx == line + 1 || line + idx + 1 >= data.size ->
            if (smudgeSeen) LineReflectionType.SmudgedLineOfReflection
            else LineReflectionType.LineOfReflection

        // No smudge here. Propagate smudgeSeen.
        data[line - idx] == data[line + idx + 1] ->
            aux(idx + 1, smudgeSeen)

        // Smudge found.
        smudgedReflection(data[line - idx], data[line + idx + 1]) && !smudgeSeen ->
            aux(idx + 1, true)

        // Either not equal, a second smudge found, or neither equal nor smudged equal.
        else -> LineReflectionType.NoReflection
    }

    return aux()
}

// Check to see if there is a line of reflection of the type required.
// If there is, we want to add 1 to indicate how many lines are before the line.
// If not, we return null.
private fun checkData(data: Data, lineReflectionType: LineReflectionType): Int? =
    (0..(data.size - 2)).firstOrNull { checkLine(data, it) == lineReflectionType }?.let { it + 1 }

// Check the rows for a line of reflection:
// 1. terrain.first are the rows, so if a value is returned, there are that many rows above it; and
// 2. terrain.second are the columns, so if a value is returned, there are that many columns left of it.
// As per the puzzle description:
// 1. return 100 * the number of rows above a line of horizontal line of reflection; or
// 2. return the number of columns to the left of a line of reflection.
private fun checkTerrain(terrain: Terrain, lineReflectionType: LineReflectionType): Int =
    (checkData(terrain.first, lineReflectionType) ?: 0) * 100 + (checkData(terrain.second, lineReflectionType) ?: 0)

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

fun answer1(input: String): Int =
    parse(input).sumOf { checkTerrain(it, LineReflectionType.LineOfReflection) }

fun answer2(input: String): Int =
    parse(input).sumOf { checkTerrain(it, LineReflectionType.SmudgedLineOfReflection) }

fun main() {
    val input = object {}.javaClass.getResource("/day13.txt")!!.readText()

    println("--- Day 13: Point of Incidence ---")

    // Answer 1: 33356
    println("Part 1: ${answer1(input)}")

    // Answer 2: 28475
    println("Part 2: ${answer2(input)}")
}