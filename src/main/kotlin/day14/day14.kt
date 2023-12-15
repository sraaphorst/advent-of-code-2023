// Advent of Code 2023, Day 14.
// By Sebastian Raaphorst, 2023.

package day14

import common.rotateClockwise

enum class Landscape(val symbol: Char) {
    SPACE('.'),
    ROUND('O'),
    CUBE('#')
}

typealias Row = List<Landscape>
typealias Segment = List<Landscape>
typealias Platform = List<Row>

// Cached functions here will be very useful and as demonstrated by previous day, trying to extract the logic
// to be functional or do something like Python's memoize is a horror show.
// Thus, while it is stateful and not at all functional, we encapsulate the state to at least hide it.
data object PlatformOperations {
    private val rowCache: MutableMap<Row, Row> = mutableMapOf()
    private val rowEvaluationCache: MutableMap<Row, Int> = mutableMapOf()
    private val platformCache: MutableMap<Platform, Platform> = mutableMapOf()
    private val cycleDetector: MutableMap<Int, Platform> = mutableMapOf()

    // Rolling along rows is far easier than rolling along columns, so we will rotate east instead of north.
    fun rollEast(row: Row): Row {
        if (row in rowCache) return rowCache.getValue(row)

        // Divide the row by cubes. An empty list represents a cube, and a non-empty list represents a segment
        // between cubes.
        fun split(remaining: Row = row, segments: List<Segment> = emptyList()): List<Segment> = when {
            remaining.isEmpty() -> segments
            remaining.first() == Landscape.CUBE ->
                split(remaining.drop(1), segments + listOf(emptyList()))
            else -> {
                val newRemaining: Row = remaining.dropWhile { it != Landscape.CUBE }
                val newSegment: Segment = remaining.takeWhile { it != Landscape.CUBE }
                println("newRemaining: '${newRemaining.show()}', newSegment: '${newSegment.show()}'")
                split(newRemaining, segments + listOf(newSegment))
            }
        }

        fun adjustSegment(segment: Segment): Segment =
            List(segment.count { it == Landscape.SPACE }) { Landscape.SPACE } +
                    List(segment.count { it == Landscape.ROUND }) { Landscape.ROUND }

        val segments = split()
        println(segments.map { it.show() })
        val adjustedRow = segments.map(::adjustSegment).fold(emptyList<Landscape>()) { acc, segment -> when {
            segment.isEmpty() -> acc + listOf(Landscape.CUBE)
            else -> acc + segment
        } }

        rowCache[row] = adjustedRow
        return adjustedRow
    }
}

fun parseRow(input: String): Row =
    input.map { symbol -> Landscape.entries.firstOrNull { it.symbol == symbol }
        ?: throw IllegalArgumentException("Illegal character in row: '$symbol'")
    }

fun List<Landscape>.show(): String =
    map { it.symbol }.joinToString("")

fun parsePlatform(input: String): Platform =
    input.trim().lines().map(::parseRow)

fun answer1(input: String): Int = TODO()

fun answer2(input: String): Int = TODO()

fun main() {
    val input = object {}.javaClass.getResource("/day14.txt")!!.readText()

    println("--- Day 14: Parabolic Reflector Dish ---")

    // Answer 1:
//    println("Part 1: ${answer1(input)}")
    val rowString = "#.O.O...#O#.O.#.O.OO.O...#.O."
    val row = parseRow(rowString)
    val rolledRow = PlatformOperations.rollEast(row)
    println(rowString)
    println(rolledRow.show())


    // Answer 2:
//    println("Part 2: ${answer2(input)}")
}