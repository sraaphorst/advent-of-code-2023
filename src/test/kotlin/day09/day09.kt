// Advent of Code 2023, Day 09.
// By Sebastian Raaphorst, 2023.

package day09

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day09Test {
    companion object {
        private val input = """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
        """.trimIndent()
    }

    @Test
    fun `Problem 1 example`() {
        assertEquals(114, answer1(input))
    }

    @Test
    fun `Problem 2 example`() {
        assertEquals(2, answer2(input))
    }
}
