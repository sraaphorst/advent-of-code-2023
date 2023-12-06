// Advent of Code 2023, Day 06.
// By Sebastian Raaphorst, 2023.

package day06

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day06Test {
    companion object {
        private val input = """
            Time:      7  15   30
            Distance:  9  40  200
        """.trimIndent()
    }

    @Test
    fun `Problem 1 example`() {
        assertEquals(288, answer1(input))
    }

    @Test
    fun `Problem 2 example`() {
        assertEquals(71503, answer2(input))
    }
}
