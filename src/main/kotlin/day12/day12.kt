// Advent of Code 2023, Day 12.
// By Sebastian Raaphorst, 2023.

package day12

import arrow.core.MemoizedDeepRecursiveFunction

private data class Input(val springs: String, val counts: List<Int>)

private fun memoize(function: (Input, (Input) -> Long) -> Long): (Input) -> Long {
    val cache = mutableMapOf<Input, Long>()
    lateinit var memoizedFunctionWrapper: (Input) -> Long

    val memoizedFunction: (Input) -> Long = { input ->
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

private fun answerAux(input: Input, memoizedFunc: (Input) -> Long): Long {
    val (springs, groups) = input
    if (groups.isEmpty())
    // If everything remaining in springs is either unknown or not a spring, then this is acceptable.
        return if (springs.all { it == '.' || it == '?' }) 1L else 0L
    else {
        val firstGroup = groups.first()
        val remainingGroups = groups.drop(1)

        // Add the sizes of the remaining groups, and 1 for each gap.
        val remainingSpaces = remainingGroups.sum() + remainingGroups.size

        val upperBound = springs.length - remainingSpaces - firstGroup
        return (0..upperBound).fold(0L) { acc, idx ->
            val possibleSprings = ".".repeat(idx) + "#".repeat(firstGroup) + "."
            if (springs.zip(possibleSprings).all { (spring, possibleSpring) -> spring == possibleSpring || spring == '?' })
                acc + memoizedFunc(Input(springs.drop(possibleSprings.length), remainingGroups))
            else acc
        }
    }
}

private val answer = memoize(::answerAux)

private val answer2 = MemoizedDeepRecursiveFunction<Input, Int> { (springs, groups) ->
    when {
        groups.isEmpty() ->
            if (springs.all { it == '.' || it == '?' }) 1 else 0

        else -> {
            val firstGroup = groups.first()
            val remainingGroups = groups.drop(1)

            // Add the sizes of the remaining groups, and 1 for each gap.
            val remainingSpaces = remainingGroups.sum() + remainingGroups.size

            val upperBound = springs.length - remainingSpaces - firstGroup
            (0..upperBound).fold(0) { acc, idx ->
                val possibleSprings = ".".repeat(idx) + "#".repeat(firstGroup) + "."
                if (springs.zip(possibleSprings)
                        .all { (spring, possibleSpring) -> spring == possibleSpring || spring == '?' }
                )
                    acc + callRecursive(Input(springs.drop(possibleSprings.length), remainingGroups))
                else acc
            }
        }
    }
}

fun answer1(input: String): Long =
    parse(input).sumOf(answer::invoke)

fun answer2(input: String): Long =
    parse(input)
        .map { (springs, counts) ->
            val newSprings = List (5) { springs }.joinToString("?")
            val newCounts = List(5) { counts }.flatten()
            println("springs -> $springs")
            println("new springs -> $newSprings")
            println("counts -> $counts")
            println("new counts -> $newCounts")
            Input(newSprings, newCounts) }
        .map(answer::invoke)
        .also { println(it) }
        .sum()

fun main() {
    val input = object {}.javaClass.getResource("/day12.txt")!!.readText()

    println("--- Day 11: Cosmic Expansion ---")

    // Answer 1: 7716
    println("Part 1: ${answer1(input)}")

    // Answer 2: 18716325559999
    println("Part 2: ${answer2(input)}")
}