// Advent of Code 2023, Day 13.
// By Sebastian Raaphorst, 2023.

package day13

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day13Test {
    companion object {
        private val input = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.

            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#
        """.trimIndent()
    }

    @Test
    fun `Problem 1 example`() {
        assertEquals(405, answer1(input))
    }

//    @Test
//    fun `Problem 2 example`() {
//        assertEquals(525152, answer2(input))
//    }
}
