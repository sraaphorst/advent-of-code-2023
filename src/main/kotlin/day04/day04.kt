// Advent of Code 2023, Day 04.
// By Sebastian Raaphorst, 2023.

package day04

private data class Card(val id: Int, val winningNumbers: Set<Int>, val numbers: Set<Int>) {
    val matches: Int by lazy {
        winningNumbers.intersect(numbers).size
    }

    val points: Int by lazy {
        if (matches == 0) 0
        else 1 shl (matches - 1)
    }

    companion object {
        fun parse(input: String): Card {
            // Eliminate all duplicate spaces before starting the parsing.
            val (idStr, numSetStr1, numSetStr2) = input.replace("  ", " ").split(": ", "|").map(String::trim)
            val id = idStr.split(" ").last().toInt()
            val winningNumbers = numSetStr1.split(' ').map(String::toInt).toSet()
            val numbers = numSetStr2.split(' ').map(String::toInt).toSet()
            return Card(id, winningNumbers, numbers)
        }
    }
}

private fun parse(input: String): Set<Card> =
    input.lineSequence().map(Card::parse).toSet()

fun answer1(input: String): Int =
    parse(input).sumOf(Card::points)

fun answer2(input: String): Int {
    fun aux(cards: Map<Int, Card> = parse(input).associateBy { it.id },
            numCards: Map<Int, Int> = cards.values.associate { it.id to 1 },
            currCardId: Int = 1): Int =
        if (currCardId > cards.size) numCards.values.sum()
        else {
            val numCurrCard = numCards[currCardId] ?: 0
            val matches = cards[currCardId]?.matches ?: 0
            val newNumCards = numCards.mapValues { (id, numCard) ->
                if (id in (currCardId + 1)..(currCardId + matches)) numCard + numCurrCard
                else numCard
            }
            aux(cards, newNumCards, currCardId + 1)
        }

    return aux()
}

fun main() {
    val input = object {}.javaClass.getResource("/day04.txt")!!.readText()

    println("--- Day 4: Scratchcards ---")

    // Answer 1: 26218
    println("Part 1: ${answer1(input)}")

    // Answer 2: 9997537
    print("Part 2: ${answer2(input)}")
}