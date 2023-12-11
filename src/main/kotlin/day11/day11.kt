// Advent of Code 2023, Day 11.
// By Sebastian Raaphorst, 2023.

package day11

import kotlin.math.abs

private typealias Coord = Pair<Long, Long>

private data class Galaxy(val id: Int, val coordinates: Coord) {
    operator fun minus(otherGalaxy: Galaxy): Long =
        abs(coordinates.first - otherGalaxy.coordinates.first) +
                abs(coordinates.second - otherGalaxy.coordinates.second)

    companion object {
        private fun emptyIndices(input: List<String>): Set<Int> =
            input
                .withIndex()
                .filter { (_, lst) -> lst.all { it == '.' } }
                .map(IndexedValue<String>::index)
                .toSet()

        fun parse(input: String, emptyValue: Long = 2): List<Galaxy> {
            val rows = input
                .trim()
                .lines()

            val cols = (0..<rows.first().length).map { colIdx ->
                rows.joinToString(separator = "") { it[colIdx].toString() }
            }

            val emptyRows = emptyIndices(rows)
            val emptyCols = emptyIndices(cols)

            // Parse the galaxies.
            return rows.withIndex().flatMap { (rIdx, row) ->
                row.withIndex().mapNotNull { (cIdx, ch) -> if (ch == '#') (rIdx to cIdx) else null }
            }.withIndex().map { (idx, coordinates) ->
                val (rIdx, cIdx) = coordinates
                // Determine the adjustment to the coordinates.
                val adjustedCoordinates = Coord(
                    rIdx.toLong() + (emptyValue - 1) * emptyRows.count { it <= rIdx },
                    cIdx.toLong() + (emptyValue - 1) * emptyCols.count { it <= cIdx }
                )
                Galaxy(idx + 1, adjustedCoordinates)
            }
        }
    }
}

private fun <T> List<T>.uniquePairs(): List<Pair<T, T>> =
    indices.flatMap { i ->
        ((i + 1)..<size).map { j ->
            this[i] to this[j]
    }
}

fun answer(input: String, emptyDistance: Long): Long =
    Galaxy.parse(input, emptyDistance)
        .uniquePairs()
        .fold(0L) { acc, pair -> acc + (pair.first - pair.second) }

fun answer1(input: String): Long =
    answer(input, 2)

fun answer2(input: String): Long =
    answer(input, 1_000_000)

fun main() {
    val input = object {}.javaClass.getResource("/day11.txt")!!.readText()

    println("--- Day 11: Cosmic Expansion ---")

    // Answer 1: 9521550
    println("Part 1: ${answer1(input)}")

    // Answer 2: 298932923702
    println("Part 2: ${answer2(input)}")
}