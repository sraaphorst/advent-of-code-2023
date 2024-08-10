// Advent of Code 2023, Day 14.
// By Sebastian Raaphorst, 2023.

package day14

import common.joinWithSeparator
import common.rotateClockwise
import common.split
import java.math.BigInteger

// Note that for this problem, we start by taking the transpose of the board and treating it as rows rather than
// columns to mentally simplify things, rolling east instead of north. The problems are equivalent.

// Rows are just longer segments, typically, that can contain CUBE.
typealias Load = Long

sealed class CachedOps<Item, ItemRep> {
    protected val ItemCache: MutableMap<Item, ItemRep> = mutableMapOf()
    protected val ItemRepUncache: MutableMap<ItemRep, Item> = mutableMapOf()
    protected val ItemRepRollCache: MutableMap<ItemRep, ItemRep> = mutableMapOf()
    protected val ItemRepLoadCache: MutableMap<ItemRep, Load> = mutableMapOf()

    // **********************************
    // *** IMPLEMENTATION TO COMPLETE ***
    // **********************************

    // Take an item and perform a simple evaluation on it, without referring to the caches.
    abstract fun calculateRep(item: Item): ItemRep

    // Take an item and perform a roll right evaluation on it, without referring to the caches.
    abstract fun calcRollRight(item: Item): Item

    abstract fun calcLoad(item: Item): Load

    // ************************
    // *** INTERFACE TO USE ***
    // ************************

    fun represent(item: Item): ItemRep =
        ItemCache.getOrPut(item) {
            calculateRep(item).also { ItemRepUncache[it] = item }
        }

    fun rollRight(rep: ItemRep): ItemRep =
        ItemRepRollCache.getOrPut(rep) {
            val item = ItemRepUncache[rep] ?: error("Item representation $rep not found in ItemRepUncache.")
            val rolledItem = calcRollRight(item)

            // Note that the rep rolled right is rolledItem, but since rolling rolledItem right is idempotent,
            // we want to store the representation of the rolled right as being rolled right.
            represent(rolledItem).also {
                ItemRepRollCache[it] = it
            }
        }

    fun load(rep: ItemRep): Load =
        ItemRepLoadCache.getOrPut(rep) {
            val item = ItemRepUncache[rep] ?: error("Item representation $rep not found in ItemRepUncache.")
            calcLoad(item)
        }
}

// Definitions and operations to be done on a row or segment of a row.
typealias LandscapeValue = BigInteger
enum class Landscape(val symbol: Char, val value: LandscapeValue) {
    SPACE('.', LandscapeValue.ZERO),
    ROUND('O', LandscapeValue.ONE),
    CUBE('#',  LandscapeValue.valueOf(2L)),
}

// We are working in base-3 to produce representations.
private val SHIFTER = BigInteger.valueOf(3L)

typealias Seg = List<Landscape>
typealias SegRep = LandscapeValue

// A collection of data operations on segments or rows.
// Note that a Row is equivalent to a Segment, except a true Segment does not contain any Landscape.CUBE.
data object SegOps: CachedOps<Seg, SegRep>() {
    // Split a segment into its constituents. For a row, this could be multiple segments.
    // For a segment, it should be an idempotent operation.
    private fun split(item: Seg): List<Seg> =
        item.split(Landscape.CUBE)

    // Calculate the value of the segment in base-3.
    override fun calculateRep(item: Seg): SegRep =
        item.fold(SegRep.ZERO) { acc, tile -> acc.times(SHIFTER).plus(tile.value) }

    override fun calcRollRight(item: Seg): Seg =
        split(item).map { seg ->
            assert(Landscape.CUBE !in seg)
            seg.partition { it == Landscape.SPACE }
                .let { (spaces, rolls) -> List(spaces.size) { Landscape.SPACE } + List(rolls.size) { Landscape.ROUND } }
        }.joinWithSeparator(Landscape.CUBE)

    override fun calcLoad(item: Seg): Load =
        item.withIndex()
            .filter { it.value == Landscape.ROUND }
            .sumOf { it.index + 1L }
}

// **************************
// *** PLATFORM OPERATIONS ***
// **************************
// Definitions and operations to be done on a row or segment of a row.

typealias Platform = List<List<Landscape>>
typealias PlatformRep = LandscapeValue

// Operations to be executed on platforms.
data object PlatformOps: CachedOps<Platform, PlatformRep>() {
    override fun calculateRep(item: Platform): PlatformRep =
        item.fold(PlatformRep.ZERO) { acc, row ->
            acc.times(SHIFTER.pow(row.size)).plus(SegOps.calculateRep(row))
        }

    override fun calcRollRight(item: Platform): Platform =
        item.map(SegOps::calcRollRight)

    override fun calcLoad(item: Platform): Load =
        item.sumOf(SegOps::calcLoad)

    // Rotation cache.
    private val RotationCache: MutableMap<PlatformRep, PlatformRep> = mutableMapOf()

    fun rotate(rep: PlatformRep): PlatformRep =
        RotationCache.getOrPut(rep) {
            val item = ItemRepUncache[rep] ?: error("Item representation $rep not found in ItemRepUncache.")
            represent(item.rotateClockwise())
        }
}

fun parseRow(input: String): Seg =
    input.map { symbol -> Landscape.entries.firstOrNull { it.symbol == symbol }
        ?: throw IllegalArgumentException("Illegal character in row: '$symbol'")
    }

fun List<Landscape>.show(): String =
    map { it.symbol }.joinToString("")

fun parsePlatform(input: String): Platform =
    input.trim().lines().map(::parseRow)

fun answer1(input: String): Load =
    parsePlatform(input)
        .let(PlatformOps::represent)
        .let(PlatformOps::rotate)
        .let(PlatformOps::rollRight)
        .let(PlatformOps::load)

//fun answer2(input: String): Int {
//    val platform = parsePlatform(input)
//    // Rotate counterclockwise 999,999,999 times.
//    val rotated = (0..1000000000).fold(platform) { acc, idx ->
//        val rotated = PlatformOperations.rotate(acc)
//        val rolled = PlatformOperations.rollEast(rotated)
//        if (idx % 1000000 == 0) println(idx)
//        if (idx > 999999000) println(PlatformOperations.evaluatePlatform(rolled))
//        rolled
//    }
//    return PlatformOperations.evaluatePlatform(rotated)
//}

fun main() {
    val input = object {}.javaClass.getResource("/day14.txt")!!.readText()

    println("--- Day 14: Parabolic Reflector Dish ---")

    // Answer 1: 109665
    println("Part 1: ${answer1(input)}")

    // Answer 2:
//    println("Part 2: ${answer2(input)}")
}