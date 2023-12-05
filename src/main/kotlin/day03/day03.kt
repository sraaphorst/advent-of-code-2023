// Advent of Code 2023, Day 03.
// By Sebastian Raaphorst, 2023.

package day03

private typealias Coord = Pair<Int, Int>

private data class Symbol(val coord: Coord, val symbol: Char) {
    val isGear: Boolean = symbol == '*'
}

private data class NumberEntry(val row: Int, val range: IntRange, val value: Int) {
    fun isAdjacentTo(symbol: Symbol): Boolean =
        symbol.coord.first in (row - 1)..(row + 1) &&
                symbol.coord.second in (range.first - 1)..(range.last + 1)
}

private fun parseSymbols(input: String): Set<Symbol> =
    input
        .lineSequence()
        .flatMapIndexed { rowIdx, row ->
            row.mapIndexedNotNull { colIdx, c -> if (c != '.' && !c.isDigit()) Symbol(rowIdx to colIdx, c) else null }
        }.toSet()

private fun parseNumberEntries(input: String): Set<NumberEntry> =
    input
        .lineSequence()
        .flatMapIndexed { rowIdx, row ->
            """\d+""".toRegex().findAll(row).map { NumberEntry(rowIdx, it.range, it.value.toInt()) }
        }.toSet()

private fun adjacentValueSum(input: String,
                             predicate: (Symbol) -> Boolean,
                             listPredicate: (List<NumberEntry>) -> Boolean,
                             reducer: (List<Int>) -> Int): Int {
    val numberEntries = parseNumberEntries(input)
    return parseSymbols(input)
        .asSequence()
        .filter(predicate)
        .map { symbol -> numberEntries.filter { it.isAdjacentTo(symbol) } }
        .filter(listPredicate)
        .map { it.map(NumberEntry::value) }
        .map(reducer)
        .sum()
}

fun answer1(input: String): Int =
    adjacentValueSum(input, { true },  { true },  { it.sum() })

fun answer2(input: String): Int =
    adjacentValueSum(input, { it.isGear }, { it.size == 2 }, { it.reduce { acc, i -> acc * i } })

fun main() {
    val input = object {}.javaClass.getResource("/day03.txt")!!.readText()

    println("--- Day 3: Gear Ratios ---")

    // Answer 1: 535235
    println("Part 1: ${answer1(input)}")

    // Answer 2: 79844424
    println("Part 2: ${answer2(input)}")
}