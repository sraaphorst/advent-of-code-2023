// Advent of Code 2023, Day 03.
// By Sebastian Raaphorst, 2023.

package day03

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day03Test {
    companion object {
        private val input = """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..
        """.trimIndent()
    }

    @Test
    fun `Problem 1 example`() {
        assertEquals(4361, answer1(input))
    }

    @Test
    fun `Problem 2 example`() {
        assertEquals(467835, answer2(input))
    }
}