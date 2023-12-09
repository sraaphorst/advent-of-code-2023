// Advent of Code 2023, Day 09.
// By Sebastian Raaphorst, 2023.

package day09

private fun calculateSeq(seq: List<Int>): Int {
    tailrec fun aux(currSeq: List<Int> = seq, acc: Int = 0): Int {
        val diffs = currSeq.zipWithNext().map { (s1, s2) -> s2 - s1}
        return if (diffs.all { it == 0 }) acc + currSeq.last()
        else aux(diffs, acc + currSeq.last())
    }

    return aux()
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
