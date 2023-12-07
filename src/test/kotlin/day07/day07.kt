// Advent of Code 2023, Day 07.
// By Sebastian Raaphorst, 2023.

package day07

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day07Test {
    companion object {
        private val input = """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
        """.trimIndent()
    }

    @Test
    fun `Problem 1 example`() {
        assertEquals(6440, answer1(input))
    }

    @Test
    fun `Problem 2 example`() {
        assertEquals(5905, answer2(input))
    }
}
