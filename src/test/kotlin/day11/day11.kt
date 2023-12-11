// Advent of Code 2023, Day 11.
// By Sebastian Raaphorst, 2023.

package day11

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day11Test {
    companion object {
        private val input = """
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....
        """.trimIndent()
    }

    @Test
    fun `Problem 1 example`() {
        assertEquals(374, answer1(input))
    }

    @Test
    fun `Problem with size 10`() {
        assertEquals(1030, answer(input, 10))
    }

    @Test
    fun `Problem with size 100`() {
        assertEquals(8410, answer(input, 100))
    }
}
