// Advent of Code 2023, Day 07.
// By Sebastian Raaphorst, 2023.

package day07

private typealias CardMap = Map<Char, Int>

private enum class HandType(val value: Int, val cardCounts: List<Int>) {
    FiveOfAKind(6, listOf(5)),
    FourOfAKind(5, listOf(4, 1)),
    FullHouse(4, listOf(3, 2)),
    ThreeOfAKind(3, listOf(3, 1, 1)),
    TwoPair(2, listOf(2, 2, 1)),
    Pair(1, listOf(2, 1, 1, 1)),
    High(0, listOf(1, 1, 1, 1, 1))
}

private data class Hand(val cards: List<Int>, val bet: Int) {
    private fun calculateType(counts: List<Int>): Int =
        HandType
            .entries
            .firstOrNull { counts == it.cardCounts }
            ?.let(HandType::value) ?: 0

    private fun getCounts(modifiedCards: List<Int>): List<Int> =
        modifiedCards
            .groupingBy { it }
            .eachCount()
            .filterValues { it != 0 }
            .values
            .sortedDescending()

    val type: Int = calculateType(getCounts(cards))

    // The type treating jacks as jokers.
    val jokerType: Int = run {
        val jokerCount = cards.count { it == JOKER_VALUE }
        val cardCounts = getCounts(cards.filterNot { it == JOKER_VALUE })

        // If cardCounts is not empty, then add the number of jokers to the highest count.
        // If it is empty, then all cards were jokers, so make the cardCounts 5.
        calculateType(
            if (cardCounts.isEmpty()) listOf(jokerCount)
            else listOf(cardCounts.first() + jokerCount) + cardCounts.drop(1)
        )
    }

    companion object {
        const val JOKER_VALUE: Int = 0

        // Compare two hands to determine which one is greater than the other.
        fun comparator(typeSelector: (Hand) -> Int) = Comparator<Hand> { h1, h2 ->
            typeSelector(h1).compareTo(typeSelector(h2)).takeUnless { it == 0 }
                ?: h1.cards.zip(h2.cards)
                    .firstOrNull { (c1, c2) -> c1 != c2 }
                    ?.let { (c1, c2) -> c1.compareTo(c2) }
                ?: 0
        }

        val CardMap1: CardMap = mapOf(
            'A' to 14,
            'K' to 13,
            'Q' to 12,
            'J' to 11,
            'T' to 10,
        ) + ('2'..'9').associateWith { it.digitToInt() }

        // For part 2, make J the weakest card (value 0).
        val CardMap2: CardMap = CardMap1.mapValues { (k, v) -> if (k == 'J') JOKER_VALUE else v }

        fun parse(cardMap: CardMap): (String) -> Hand = {
            it
                .trim()
                .split(" ")
                .let { (cs, bs) -> Hand(cs.map(cardMap::getValue), bs.toInt()) }
        }
    }
}

private fun answer(input: String, typeExtractor: (Hand) -> Int, cardMap: CardMap): Int =
    input
        .lines()
        .map(Hand.parse(cardMap))
        .sortedWith(Hand.comparator(typeExtractor))
        .withIndex()
        .sumOf { (idx, hand) -> (idx + 1) * hand.bet }

fun answer1(input: String): Int =
    answer(input, Hand::type, Hand.CardMap1)

fun answer2(input: String): Int =
    answer(input, Hand::jokerType, Hand.CardMap2)

fun main() {
    val input = object {}.javaClass.getResource("/day07.txt")!!.readText()

    println("--- Day 7: Camel Cards ---")

    // Answer 1: 253910319
    println("Part 1: ${answer1(input)}")

    // Answer 2: 254083736
    println("Part 2: ${answer2(input)}")
}