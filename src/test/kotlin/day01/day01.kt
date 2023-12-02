// Advent of Code 2023, Day 01.
// By Sebastian Raaphorst, 2023.

package day01

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day01Test {
    companion object {
        private val input1 = """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
        """.trimIndent()

        private val input2 = """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen
        """.trimIndent()
    }

    @Test
    fun `Problem 1 example`() {
        assertEquals(142, answer1(input1))
    }

    @Test
    fun `Problem 2 example`() {
        assertEquals(281, answer2(input2))
    }
}