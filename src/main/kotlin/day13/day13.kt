// Advent of Code 2023, Day 13.
// By Sebastian Raaphorst, 2023.

package day13

private typealias Data = List<Long>
private typealias RowData = Data
private typealias ColData = Data
private typealias Terrain = Pair<RowData, ColData>

private enum class ReflectionType {
    Reflection,
    SmudgedReflection
}

// Determine if there is a line of reflection between data[line] and data[line+1], and if so,
// if it is a normal line of reflection or a smudged one.
// This could be optimized somewhat (e.g. by passing the required LineReflectionType here),
// but this works sufficiently fast.
private fun checkLine(data: Data, line: Int): ReflectionType? {
    // Check if two Longs differ in exactly one bit, which represents a reflection with smudge.
    fun smudgedReflection(l1: Long, l2: Long): Boolean {
        val l = l1 xor l2
        return l != 0L && (l and (l - 1) == 0L)
    }

    // At iteration idx, we compare lines (line - idx) and (line + idx + 1).
    tailrec fun aux(idx: Int = 0, smudgeSeen: Boolean = false): ReflectionType? = when {
        // Stop if we reach line + 1 or if the reflected line does not exist.
        idx == line + 1 || line + idx + 1 >= data.size ->
            if (smudgeSeen) ReflectionType.SmudgedReflection
            else ReflectionType.Reflection

        // Lines reflect and no smudge here. Propagate smudgeSeen.
        data[line - idx] == data[line + idx + 1] ->
            aux(idx + 1, smudgeSeen)

        // Lines reflect with smudge found. Propagate only if we have not yet seen a smudge.
        smudgedReflection(data[line - idx], data[line + idx + 1]) && !smudgeSeen ->
            aux(idx + 1, true)

        // Either lines do not reflect, or reflect with a smudge and we have already seen one.
        else -> null
    }

    return aux()
}

// Check to see if there is a line of reflection of the type required.
// If there is, we want to add 1 to indicate how many lines are before the line.
// If not, we return null.
private fun checkData(data: Data, reflectionType: ReflectionType): Int? =
    (0..(data.size - 2))
        .firstOrNull { checkLine(data, it) == reflectionType }
        ?.let { it + 1 }

// Check the rows and columns for the required type of line of reflection.
// 1. terrain.first are the rows, so if a value is returned by checkData, there are that many rows above it; and
// 2. terrain.second are the columns, so if a value is returned by checkData, there are that many columns left of it.
// As per the puzzle description:
// 1. return 100 * the number of rows above a line of horizontal line of reflection; or
// 2. return the number of columns to the left of a line of reflection.
private fun checkTerrain(terrain: Terrain, reflectionType: ReflectionType): Int =
    (checkData(terrain.first, reflectionType) ?: 0) * 100 + (checkData(terrain.second, reflectionType) ?: 0)

// Convert a string of # and . to a Long corresponding to 1 and 0 respectively.
private fun stringToLong(bitString: String): Long =
    bitString.fold(0L) { acc, char ->
        (acc shl 1) or (if (char == '#') 1L else 0L)
    }

private fun parseBoard(data: List<String>): List<Long> =
    data.map(::stringToLong)

private fun parse(input: String): List<Terrain> =
    input.trim().split("\n\n").map { terrainString ->
        val rows = terrainString.lines()
        val cols = (0..<rows.first().length).map { colIdx ->
            rows.joinToString(separator = "") { it[colIdx].toString() }
        }
        Terrain(parseBoard(rows), parseBoard(cols))
    }

fun answer1(input: String): Int =
    parse(input).sumOf { checkTerrain(it, ReflectionType.Reflection) }

fun answer2(input: String): Int =
    parse(input).sumOf { checkTerrain(it, ReflectionType.SmudgedReflection) }

fun main() {
    val input = object {}.javaClass.getResource("/day13.txt")!!.readText()

    println("--- Day 13: Point of Incidence ---")

    // Answer 1: 33356
    println("Part 1: ${answer1(input)}")

    // Answer 2: 28475
    println("Part 2: ${answer2(input)}")
}
