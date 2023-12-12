// Advent of Code 2023, Day 12.
// By Sebastian Raaphorst, 2023.

package day12

// To simplify memoization, bunch input parameters together in a data class.
private data class Input(val springs: String, val counts: List<Int>)

// Tried using Arrow's arrow.core.MemoizedDeepRecursiveFunction, but code ran insanely long for part 2.
// This is probably because of complete lack of tail recursion.
// Perhaps I will attempt to replace fold with tail recursive solution.
// Instead, had to write my own memoize function.
private fun <T, U> memoize(function: (T, (T) -> U) -> U): (T) -> U {
    val cache = mutableMapOf<T, U>()
    lateinit var memoizedFunctionWrapper: (T) -> U

    val memoizedFunction: (T) -> U = { input ->
        cache.getOrPut(input) { function(input, memoizedFunctionWrapper) }
    }

    memoizedFunctionWrapper = memoizedFunction
    return memoizedFunctionWrapper
}

private fun parse(input: String): List<Input> =
    input.trim()
        .lines()
        .map {
            val (pattern, countsStr) = it.split(' ')
            val counts = countsStr.split(',').map(String::toInt)
            Input(pattern, counts)
        }

// As I was sick and feverish today, code heavily inspired by solution by Python solution by reddit user NimbyDagda:
// https://github.com/AshGriffiths/advent_of_code/blob/main/2023/day_twelve/day12.py
private fun answerAux(input: Input, memoizedFunc: (Input) -> Long): Long {
    val (springs, counts) = input
    if (counts.isEmpty())
        // If everything remaining in springs is either unknown or not a spring, then this is acceptable.
        return if (springs.all { it == '.' || it == '?' }) 1L else 0L
    else {
        val firstCount = counts.first()
        val remainingCounts = counts.drop(1)

        // Add the sizes of the remaining counts, and 1 for each gap.
        val remainingSpaces = remainingCounts.sum() + remainingCounts.size

        val upperBound = springs.length - remainingSpaces - firstCount
        return (0..upperBound).fold(0L) { acc, idx ->
            val possibleSprings = ".".repeat(idx) + "#".repeat(firstCount) + "."
            if (springs.zip(possibleSprings).all { (spring, possibleSpring) ->
                spring == possibleSpring || spring == '?' })
                acc + memoizedFunc(Input(springs.drop(possibleSprings.length), remainingCounts))
            else acc
        }
    }
}

private val answer = memoize(::answerAux)

fun answer1(input: String): Long =
    parse(input).sumOf(answer::invoke)

fun answer2(input: String): Long =
    parse(input)
        .map { (springs, counts) ->
            Input(List (5) { springs }.joinToString("?") , List(5) { counts }.flatten()) }
        .sumOf(answer::invoke)

fun main() {
    val input = object {}.javaClass.getResource("/day12.txt")!!.readText()

    println("--- Day 12: Hot Springs ---")

    // Answer 1: 7716
    println("Part 1: ${answer1(input)}")

    // Answer 2: 18716325559999
    println("Part 2: ${answer2(input)}")
}