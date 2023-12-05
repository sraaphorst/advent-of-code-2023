// Advent of Code 2023, Day 05.
// By Sebastian Raaphorst, 2023.

package day05

private data class MappingEntry(val destinationStart: Long, val sourceStart: Long, val length: Long) {
    // Precalculate to avoid repeatedly creating these ranges.
    private val sourceRange: LongRange = (sourceStart..(sourceStart + length))
    private val destinationRange: LongRange = (destinationStart..(destinationStart + length))

    fun lookup(source: Long): Long? =
        // Check for intersection.
        if (source in sourceRange) destinationStart + (source - sourceStart)
        else null

    fun reverseLookup(destination: Long): Long? =
        if (destination in destinationRange) sourceStart + (destination - destinationStart)
        else null

    companion object {
        fun parse(input: String): MappingEntry {
            val (destinationStart, sourceStart, length) = input.trim().split(" ").map(String::toLong)
            return MappingEntry(destinationStart, sourceStart, length)
        }
    }
}

// Note that using a List<MappingEntry> here with firstNotNullOfOrNull is immensely faster than using a
// Sequence<MappingEntry> here with mapNotNull and firstOrNull. The List approach takes a couple of minutes.
private data class Mapping(val entries: List<MappingEntry>) {
    fun lookup(source: Long): Long =
        entries.firstNotNullOfOrNull { it.lookup(source) } ?: source

    fun reverseLookup(destination: Long): Long =
        entries.firstNotNullOfOrNull { it.reverseLookup(destination) } ?: destination

    companion object {
        fun parse(input: String): Mapping =
            Mapping(input.trim().lines().drop(1).map(MappingEntry::parse))
    }
}

private fun parser(input: String, seedParser: (String) -> Sequence<LongRange>): Pair<Sequence<LongRange>, List<Mapping>> {
    val (seedsString, mapsText) = input.trim().split('\n', limit=2)
    val seedSeq = seedsString
        .split(':')[1]
        .trim()
        .let(seedParser)

    // Now parse each of the sections.
    val mappings = mapsText.trim().split("\n\n").map(Mapping::parse)

    return seedSeq to mappings
}

fun answer1(input: String): Long {
    val (seedSeq, mappings) = parser(input, ::parseSeeds)
    return seedSeq.minOf { it.minOf { seed ->
        mappings.fold(seed){ acc, map -> map.lookup(acc) }
    }}
}

private fun parseSeeds(input: String): Sequence<LongRange> =
    input.split(' ').asSequence().map{ it.toLong()..it.toLong() }

// Due to the enormous sizes, we instead work backwards, looking for the lowest numbered location that
// corresponds to a seed.
fun answer2(input: String): Long {
    val (seedSeq, mappings) = parser(input, ::parseSeedRanges)
    val reverseMappings = mappings.reversed()

    return generateSequence(0L) { it + 1 }.filter { location ->
        val seed = reverseMappings.fold(location){ acc, map -> map.reverseLookup(acc) }
        seedSeq.any { it.contains(seed) }
    }.first()
}

private fun parseSeedRanges(input: String): Sequence<LongRange> =
    input
        .split(' ')
        .windowed(2, 2)
        .map {
            val lower = it[0].toLong()
            val upper = lower + it[1].toLong()
            (lower..upper) }
        .asSequence()

fun main() {
    val input = object {}.javaClass.getResource("/day05.txt")!!.readText()

    println("--- Day 5: If You Give A Seed A Fertilizer ---")

    // Answer 1: 178159714
    println("Part 1: ${answer1(input)}")

    // Answer 2: 100165128
    print("Part 2: ${answer2(input)}")
}