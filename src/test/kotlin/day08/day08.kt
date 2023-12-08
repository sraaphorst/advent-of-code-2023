// Advent of Code 2023, Day 08.
// By Sebastian Raaphorst, 2023.

package day08

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day08Test {
    companion object {
        private val input1_1 = """
            RL

            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent()

        private val input1_2 = """
            LLR

            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent()

        private val input2 = """
            LR

            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)
        """.trimIndent()
    }

    @Test
    fun `Problem 1 example`() {
        assertEquals(2, answer1(input1_1))
        assertEquals(6, answer1(input1_2))
    }

    @Test
    fun `Problem 2 example`() {
        assertEquals(6, answer2(input2))
    }
}
