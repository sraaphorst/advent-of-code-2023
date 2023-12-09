// Advent of Code 2023, Day 09.
// By Sebastian Raaphorst, 2023.

package day09

private fun calculateSeq(seq: List<Int>): Int {
    val diffs = seq.zipWithNext().map { (s1, s2) -> s2 - s1 }
    val next = if (diffs.all { it == 0 }) 0 else calculateSeq(diffs)
    return seq.last() + next
}

private fun parse(input: String): Sequence<List<Int>> =
    input
        .lineSequence()
        .map { it.trim().split(" ").map(String::toInt) }

fun answer1(input: String): Int =
    parse(input).sumOf(::calculateSeq)

fun answer2(input: String): Int =
    parse(input).map(List<Int>::reversed).sumOf(::calculateSeq)

fun main() {
    val input = object {}.javaClass.getResource("/day09.txt")!!.readText()

    println("--- Day 9: Mirage Maintenance ---")

    // Answer 1: 1921197370
    println("Part 1: ${answer1(input)}")

    // Answer 2: 1124
    println("Part 2: ${answer2(input)}")
}
