// Advent of Code 2023, Day 02.
// By Sebastian Raaphorst, 2023.

package day02

data class Round(val red: Int = 0, val green: Int = 0, val blue: Int = 0) {

    companion object {
        fun parse(round: String): Round =
            round
                .split(",")
                .map { it.trim().split(" ") }
                .fold(Round()) { acc, (count, color) -> when (color) {
                    "red"   -> acc.copy(red   = count.toInt())
                    "green" -> acc.copy(green = count.toInt())
                    "blue"  -> acc.copy(blue  = count.toInt())
                    else    -> acc
                }
            }
    }
}

data class Game(val gameIdx: Int, val rounds: Set<Round>) {
    val power: Int by lazy {
        (rounds.maxOfOrNull(Round::red) ?: 0) *
                (rounds.maxOfOrNull(Round::green) ?: 0) *
                (rounds.maxOfOrNull(Round::blue) ?: 0)
    }

    companion object {
        fun parse(input: String): Game = Game(
            gameIdx = input.substringBefore(":").filter { it.isDigit() }.toInt(),
            rounds = input.substringAfter(":")
                .split(";")
                .map(Round::parse)
                .toSet()
        )
    }
}

fun answer1(games: List<Game>): Int =
    games
        .filter { game -> game.rounds.all { it.red <= 12 && it.green <= 13 && it.blue <= 14 } }
        .sumOf(Game::gameIdx)

fun answer2(games: List<Game>): Int =
    games.sumOf(Game::power)

fun parse(input: String): List<Game> =
    input.lines().map(Game::parse)

fun main() {
    val games = parse(object {}.javaClass.getResource("/day02.txt")!!.readText())

    println("--- Day 2: Cube Conundrum ---")

    // Answer 1: 2256
    println("Part 1: ${answer1(games)}")

    // Answer 2: 74229
    println("Part 2: ${answer2(games)}")
}