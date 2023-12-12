// Advent of Code 2023, Day 12.
// By Sebastian Raaphorst, 2023.

package day12

private data class Input(val springs: String, val counts: List<Int>)

private fun memoize(function: (Input, (Input) -> Int) -> Int): (Input) -> Int {
    val cache = mutableMapOf<Input, Int>()
    lateinit var memoizedFunctionWrapper: (Input) -> Int

    val memoizedFunction: (Input) -> Int = { input ->
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
            Input("$pattern.", counts)
        }

private fun answerAux(input: Input, memoizedFunc: (Input) -> Int): Int {
    val (springs, groups) = input
    if (groups.isEmpty())
    // If everything remaining in springs is either unknown or not a spring, then this is acceptable.
        return if (springs.all { it == '.' || it == '?' }) 1 else 0
    else {
        val firstGroup = groups.first()
        val remainingGroups = groups.drop(1)

        // Add the sizes of the remaining groups, and 1 for each gap.
        val remainingSpaces = remainingGroups.sum() + remainingGroups.size

        val upperBound = springs.length - remainingSpaces - firstGroup
        return (0..upperBound).fold(0) { acc, idx ->
            val possibleSprings = ".".repeat(idx) + "#".repeat(firstGroup) + "."
            if (springs.zip(possibleSprings).all { (spring, possibleSpring) -> spring == possibleSpring || spring == '?' })
                acc + memoizedFunc(Input(springs.drop(possibleSprings.length), remainingGroups))
            else acc
        }
    }
}

private val answer = memoize(::answerAux)

fun answer1(input: String): Int {
//    println(answer(Input("?#", listOf(1))))
    val inputs = parse(input)
    inputs.forEach { inp  ->
        val a = answer(inp)
        println("$inp -> $a")
    }
    return parse(input).sumOf { inp -> answer(inp) }
}

fun answer2(input: String): Long = TODO()

fun main() {
    val input = object {}.javaClass.getResource("/day12.txt")!!.readText()

    println("--- Day 11: Cosmic Expansion ---")

    // Answer 1:
    println("Part 1: ${answer1(input)}")

    // Answer 2:
    println("Part 2: ${answer2(input)}")
}