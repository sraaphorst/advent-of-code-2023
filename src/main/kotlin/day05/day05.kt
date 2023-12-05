// Advent of Code 2023, Day 05.
// By Sebastian Raaphorst, 2023.

package day05

private data class MappingEntry(val destinationStart: Long, val sourceStart: Long, val length: Long) {
    fun lookup(source: Long): Long? =
        if (source in (sourceStart..(sourceStart + length))) destinationStart + (source - sourceStart)
        else null

    companion object {
        fun parse(input: String): MappingEntry {
            val (destinationStart, sourceStart, length) = input.trim().split(" ").map(String::toLong)
            return MappingEntry(destinationStart, sourceStart, length)
        }
    }
}

private data class Mapping(val entries: Sequence<MappingEntry>) {
    fun lookup(source: Long): Long =
        entries.mapNotNull { it.lookup(source)  }.firstOrNull() ?: source

    companion object {
        fun parse(input: String): Mapping =
            Mapping(input.trim().lineSequence().drop(1).map(MappingEntry::parse))
    }
}

private fun answer(input: String, seedParser: (String) -> Set<Long>): Long {
    val (seedsString, mapsText) = input.trim().split('\n', limit=2)
    val seedSet = seedsString
        .split(':')[1]
            .trim()
            .let(seedParser)
    println("Seed set size: ${seedSet.size}")

    // Now parse each of the sections.
    val mappings = mapsText.trim().split("\n\n").map(Mapping::parse)

    return seedSet.minOf {
        mappings.fold(it) { value, mapping ->
            mapping.lookup(value)
        }
    }
}

fun answer1(input: String): Long =
    answer(input, ::parseSeeds)

private fun parseSeeds(input: String): Set<Long> =
    input.split(' ').map(String::toLong).toSet()

fun answer2(input: String): Long =
    answer(input, ::parseSeedRanges)

private fun parseSeedRanges(input: String): Set<Long> =
    input
        .split(' ')
        .windowed(2, 2)
        .flatMap {
            val lower = it[0].toLong()
            val upper = lower + it[1].toLong()
            (lower..upper).toSet() }
            .toSet()

fun main() {
    val input = object {}.javaClass.getResource("/day05.txt")!!.readText()

    println("--- Day 5: If You Give A Seed A Fertilizer ---")

    // Answer 1: 178159714
    println("Part 1: ${answer1(input)}")

    // Answer 2: 9997537
    print("Part 2: ${answer2(input)}")
}