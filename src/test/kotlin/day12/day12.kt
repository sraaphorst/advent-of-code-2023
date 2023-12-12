// Advent of Code 2023, Day 12.
// By Sebastian Raaphorst, 2023.

package day12

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day12Test {
    companion object {
        private val input = """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1
        """.trimIndent()
    }

    @Test
    fun `Problem 1 example`() {
        assertEquals(21, answer1(input))
    }

//    @Test
//    fun `Problem 2 example`() {
//        assertEquals(1030, answer2(input))
//    }
}
