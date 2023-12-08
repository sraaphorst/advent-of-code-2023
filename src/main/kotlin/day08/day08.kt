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

private fun gcd(a: Long, b: Long): Long =
    if (b == 0L) a else gcd(b, a % b)

private fun lcm(a: Long, b: Long): Long =
    (a * b) / gcd(a, b)

private fun numSteps(directions: List<Direction>, network: Network, startNode: String, end: (String) -> Boolean): Long {
    tailrec fun aux(moves: Sequence<Direction> = sequence { while (true) yieldAll(directions) },
                    node: String = startNode,
                    steps: Long = 0L): Long = when {
        end(node) -> steps
        else -> aux(moves.drop(1), moves.first()(network.getValue(node)), steps + 1)
    }

    return aux()
}

fun answer1(input: String): Long =
    parse(input).let { (directions, network) -> numSteps(directions, network, "AAA") { it == "ZZZ" } }

fun answer2(input: String): Long {
    val (directions, network) = parse(input)
    val startNodes = network.keys.filter { it.last() == 'A' }
    val endNodes = network.keys.filter { it.last() == 'Z' }

    val steps = startNodes.map { node -> numSteps(directions, network, node) { it in endNodes} }
    return steps.reduce { acc, num -> lcm(acc, num) }
}

fun main() {
    val input = object {}.javaClass.getResource("/day08.txt")!!.readText()

    println("--- Day 8: Haunted Wasteland ---")

    // Answer 1: 20659
    println("Part 1: ${answer1(input)}")

    // Answer 2: 15690466351717
    println("Part 2: ${answer2(input)}")
}