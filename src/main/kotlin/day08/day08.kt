// Advent of Code 2023, Day 08.
// By Sebastian Raaphorst, 2023.

package day08

private typealias Direction = (Pair<String, String>) -> String
private typealias Network = Map<String, Pair<String, String>>

private fun parse(input: String): Pair<List<Direction>, Network> {
    val lines = input.lines()
    val directions: List<Direction> = lines.first().trim().map { when(it) {
        'L' -> Pair<String, String>::first
        else -> Pair<String, String>::second
    }  }

    val pattern = Regex("""(\w+) = \((\w+), (\w+)\)""")
    val network = lines.drop(2).mapNotNull {
        pattern.matchEntire(it)?.destructured?.let { (k, l, r) ->
            k to (l to r)
        }
    }.toMap()

    return directions to network
}

fun answer1(input: String): Int {
    val (directions, network) = parse(input)

    tailrec fun aux(moves: Sequence<Direction> = sequence { while(true) yieldAll(directions) },
                    node: String = "AAA",
                    steps: Int = 0): Int = when {
        node == "ZZZ" -> steps
        else -> aux(moves.drop(1), moves.first()(network.getValue(node)), steps + 1)
    }

    return aux()
}

fun answer2(input: String): Int =
    TODO()

fun main() {
    val input = object {}.javaClass.getResource("/day08.txt")!!.readText()

    println("--- Day 8: Haunted Wasteland ---")

    // Answer 1: 20659
    println("Part 1: ${answer1(input)}")

    // Answer 2: 254083736
//    println("Part 2: ${answer2(input)}")
}