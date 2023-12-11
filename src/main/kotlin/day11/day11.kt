// Advent of Code 2023, Day 11.
// By Sebastian Raaphorst, 2023.

package day11

import kotlin.math.abs

private const val EMPTY_SPACE: Char = '.'
private const val GALAXY: Char = '#'
private typealias Coord = Pair<Long, Long>

private fun manhattanDistance(c1: Coord, c2: Coord): Long =
    abs(c1.first - c2.first) + abs(c1.second - c2.second)

// To optimize, calculate the adjustments for each dataset (rows / columns) for each index.
// This tells us how much we must adjust each coordinate by to get from the input data to the actual data.
private fun calculateAdjustments(data: List<String>, emptyValue: Long): List<Long> {
    // Get the indices that are empty in the data set, i.e. the lines that consist only of empty space.
    val emptyIndices = data
        .withIndex()
        .filter { (_, lst) -> lst.all { it == EMPTY_SPACE } }
        .map(IndexedValue<String>::index)
        .toSet()
    return data.indices.fold(listOf(0L)) { acc, index ->
        acc + (acc.last() + if (index in emptyIndices) emptyValue - 1 else 0)
    }.drop(1) // Drop the initial 0 used in the calculation.
}

private fun parse(input: String, emptyValue: Long = 2): List<Coord> {
    val rows = input.trim().lines()
    val cols = (0..<rows.first().length).map { colIdx ->
        rows.joinToString(separator = "") { it[colIdx].toString() }
    }

    val rowAdjustments = calculateAdjustments(rows, emptyValue)
    val colAdjustments = calculateAdjustments(cols, emptyValue)

    // Parse the galaxy coordinates and adjust them.
    return rows.withIndex().flatMap { (rIdx, row) ->
        row.withIndex().mapNotNull { (cIdx, ch) -> if (ch == GALAXY) (rIdx to cIdx) else null }
    }.map {
        val (rIdx, cIdx) = it
        rIdx.toLong() + rowAdjustments[rIdx] to cIdx.toLong() + colAdjustments[cIdx]
    }
}

private fun <T> List<T>.uniquePairs(): List<Pair<T, T>> =
    indices.flatMap { i ->
        ((i + 1)..<size).map { j ->
            this[i] to this[j]
    }
}

fun answer(input: String, emptyDistance: Long): Long =
    parse(input, emptyDistance)
        .uniquePairs()
        .fold(0L) { acc, pair -> acc + manhattanDistance(pair.first, pair.second) }

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