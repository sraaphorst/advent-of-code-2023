// Advent of Code 2023, Day 14.
// By Sebastian Raaphorst, 2023.

package day14

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day14Test {
    companion object {
        private val input = """
            O....#....
            O.OO#....#
            .....##...
            OO.#O....O
            .O.....O#.
            O.#..O.#.#
            ..O..#O..O
            .......O..
            #....###..
            #OO..#....
        """.trimIndent()
    }

    @Test
    fun `Problem 1 example`() {
        assertEquals(136, answer1(input))
    }

    @Test
    fun `Problem 2 example`() {
        assertEquals(64, answer2(input))
    }
}
