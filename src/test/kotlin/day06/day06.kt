// Advent of Code 2023, Day 06.
// By Sebastian Raaphorst, 2023.

package day06

import kotlin.math.ceil
import kotlin.math.sqrt

// We want to BEAT the record, so we need to add a tiny epsilon to distance to eliminate the case that we tie
// the record.
private val epsilon = 1e-10

// To get the answer, we want to solve the monic quadratic polynomial:
// x(t-x) = d -> -x^2 + tx - d = 0 -> x^2 - tx + d = 0
private fun solveMonicQuadratic(b: Double, c: Double): Pair<Double, Double> {
    // We assume that the discriminant is >= 0.
    val discriminant = b * b - 4 * c
    val sqrtDiscriminant = sqrt(discriminant)
    return (-b + sqrtDiscriminant) / 2.0 to (-b - sqrtDiscriminant) / 2.0
}

fun answer1(input: String): Long =
    input
        .trim()
        .lines()
        .map { it.split(':')[1].trim().split(' ').mapNotNull(String::toLongOrNull) }
        .let { it[0].zip(it[1]) }
        .fold(1L){ acc, (t, d) ->
            val (r1, r2) = solveMonicQuadratic(-t.toDouble(), d.toDouble() + epsilon)
            val diff = ceil(r1).toLong() - ceil(r2).toLong()
            acc * diff
        }

fun answer2(input: String): Long {
    val (t, d) = input
        .trim()
        .lines()
        .map { it.split(':')[1].replace(" ", "").let(String::toLong) }
    val (r1, r2) = solveMonicQuadratic(-t.toDouble(), d.toDouble() + epsilon)
    return ceil(r1).toLong() - ceil(r2).toLong()
}

fun main() {
    val input = object {}.javaClass.getResource("/day06.txt")!!.readText()

    println("--- Day 6: Wait For It ---")

    // Answer 1: 252000
    println("Part 1: ${answer1(input)}")

    // Answer 2: 36992486
    println("Part 2: ${answer2(input)}")
}