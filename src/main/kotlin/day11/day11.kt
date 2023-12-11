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

        fun parse(input: String, emptyValue: Int = 2): Sequence<Galaxy> {
            val rows = input
                .trim()
                .lines()

            println("Length: ${rows.first().length}")
            val cols = (0..<rows.first().length).map { colIdx ->
                rows.joinToString(separator = "") { it[colIdx].toString() }
            }

            val emptyRows = emptyIndices(rows)
            val emptyCols = emptyIndices(cols)

            val g = rows.withIndex().flatMap { (rIdx, row) ->
                row.withIndex().mapNotNull { (cIdx, ch) -> if (ch == '#') (rIdx to cIdx) else null }
            }
            println("Cols: $cols")
            println("Empty rows: $emptyRows")
            println("Empty cols: $emptyCols")
            println("--- ROWS ---")
            g.forEach { println(it) }
            println("------------")

            // Parse the galaxies.
            return rows.asSequence().withIndex().flatMap { (rIdx, row) ->
                row.withIndex().mapNotNull { (cIdx, ch) -> if (ch == '#') (rIdx to cIdx) else null }
            }.withIndex().map { (idx, coordinates) ->
                val (rIdx, cIdx) = coordinates
                // Determine the adjustment to the coordinates.
                println("Processing ${idx + 1}: $coordinates")
                val er = emptyRows.filter { it <= rIdx }
                val ec = emptyCols.filter { it <= cIdx }
                println("\temptyRows: ${er.size} -> $er")
                println("\temptyCols: ${ec.size} -> $ec")

                val adjustedCoordinates = Coord(
                    rIdx.toLong() + (emptyValue - 1) * emptyRows.count { it <= rIdx },
                    cIdx.toLong() + (emptyValue - 1) * emptyCols.count { it <= cIdx }
                )
                val g = Galaxy(idx + 1, adjustedCoordinates)
                println("\t${idx + 1}: $coordinates -> $adjustedCoordinates")
                g
            }
        }
    }
}

private fun <T> Sequence<T>.uniquePairs(): Sequence<Pair<T, T>> = sequence {
    this@uniquePairs.forEachIndexed { idx, a ->
        this@uniquePairs.drop(idx + 1).forEach { b ->
            yield(a to b)
        }
    }
}

private fun answer(input: String, emptyDistance: Int): Long =
    Galaxy.parse(input, emptyDistance)
        .uniquePairs()
        .fold(0L) { acc, pair -> acc + (pair.first - pair.second) }

fun answer1(input: String): Long =
    answer(input, 2)

fun answer2(input: String): Int = TODO()

fun main() {
    val input = object {}.javaClass.getResource("/day11.txt")!!.readText()

    println("--- Day 11: Cosmic Expansion ---")

    // Answer 1: 6820
    println("Part 1: ${answer1(input)}")

    // Answer 2: 337
//    println("Part 2: ${answer2(input)}")
}