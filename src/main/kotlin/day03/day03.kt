// Advent of Code 2023, Day 03.
// By Sebastian Raaphorst, 2023.

package day03

typealias Coord = Pair<Int, Int>

data class Symbol(val coord: Coord, val symbol: Char) {
    val isGear by lazy {
        symbol == '*'
    }
}

data class NumberEntry(val row: Int, val startCol: Int, val endCol: Int, val value: Int) {
    fun adjacentToSymbol(symbolPositions: Set<Coord>): Boolean {
        return ((row - 1)..(row + 1)).any { r -> ((startCol - 1)..(endCol + 1)).any { c -> (r to c) in symbolPositions } }
    }

    fun adjacentToGear(gear: Coord): Boolean =
        (gear.first in (row - 1)..(row + 1)) && (gear.second in (startCol -1)..(endCol + 1))
}

private fun parseSymbols(input: String): Set<Symbol> =
    input
        .lineSequence()
        .mapIndexed { rowIdx, row ->
            row.mapIndexedNotNull { colIdx, c -> if (c != '.' && !c.isDigit()) Symbol(rowIdx to colIdx, c) else null }
        }.flatten().toSet()

private fun parseNumberEntries(input: String): Set<NumberEntry> {
    val numberRegex = """\d+""".toRegex()

    return input.lineSequence().mapIndexed { rowIdx, line ->
        numberRegex.findAll(line).map { matchResult ->
            NumberEntry(rowIdx, matchResult.range.first, matchResult.range.last, matchResult.value.toInt())
        }
    }.flatten().toSet()
}

fun answer1(input: String): Int {
    val symbolPositions = parseSymbols(input).map(Symbol::coord).toSet()
    return parseNumberEntries(input).filter { it.adjacentToSymbol(symbolPositions) }.sumOf(NumberEntry::value)
}

fun answer2(input: String): Int {
    val gearPositions = parseSymbols(input).filter(Symbol::isGear).map(Symbol::coord).toSet()
    val numberEntries = parseNumberEntries(input)
    return gearPositions.map { gear -> numberEntries.filter { it.adjacentToGear(gear) } }.filter { it.size == 2 }
        .map { it.fold(1){acc, numberEntry -> acc * numberEntry.value } }.sum()
}

fun main() {
    val input = object {}.javaClass.getResource("/day03.txt")!!.readText()

    println("--- Day 3: Gear Ratios ---")

    // Answer 1: 535235
    println("Part 1: ${answer1(input)}")

    // Answer 2: 79844424
    print("Part 2: ${answer2(input)}")
}