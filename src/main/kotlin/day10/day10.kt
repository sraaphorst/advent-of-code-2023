// Advent of Code 2023, Day 10.
// By Sebastian Raaphorst, 2023.

package day10

private typealias Coord = Pair<Int, Int>

private enum class Direction(val deltaX: Int = 0, val deltaY: Int = 0) {
    NORTH(deltaY = -1),
    SOUTH(deltaY = 1),
    EAST(deltaX = 1),
    WEST(deltaX = -1)
}

private operator fun Coord.plus(direction: Direction): Coord =
    (first + direction.deltaX) to (second + direction.deltaY)

private operator fun Direction.unaryMinus() = when(this) {
    Direction.NORTH -> Direction.SOUTH
    Direction.SOUTH -> Direction.NORTH
    Direction.EAST -> Direction.WEST
    Direction.WEST -> Direction.EAST
}

private enum class Pipe(val symbol: Char, val directions: Set<Direction> = emptySet()) {
    VERTICAL('|', setOf(Direction.NORTH, Direction.SOUTH)),
    HORIZONTAL('-', setOf(Direction.WEST, Direction.EAST)),
    NORTHEAST('L', setOf(Direction.NORTH, Direction.EAST)),
    NORTHWEST('J', setOf(Direction.NORTH, Direction.WEST)),
    SOUTHWEST('7', setOf(Direction.SOUTH, Direction.WEST)),
    SOUTHEAST('F', setOf(Direction.SOUTH, Direction.EAST)),
    GROUND('.'),
    START('S')
}

private class PipeMap(val startCoord: Coord, private val grid: Map<Coord, Pipe>) {
    val width = grid.keys.maxBy { it.first }.first
    val height = grid.keys.maxBy { it.second }.second

    private fun neighbourhood(coord: Coord): Set<Coord> =
        Direction
            .entries
            .map { (coord.first + it.deltaX) to (coord.second + it.deltaY) }
            .filter { it.first in (0..<width) && it.second in  (0..<height)}.toSet()

    private val startPiece: Pipe = run {
        // Find the directions that lead into this pipe.
        val neighbours = neighbourhood(startCoord)
        val directionsIn = Direction.entries.filter { dir ->
            neighbours.any { dir in grid.getValue(it).directions && it + dir == startCoord }
        }
        assert(directionsIn.size == 2) { "Start piece does not have two directions in." }

        // Now translate to directions that lead out of this pipe.
        // This will correspond to exactly one of the Pipe pieces
        val directionsOut = directionsIn.map { -it }.toSet()
        Pipe.entries.first { it.directions == directionsOut }
    }

    fun getValue(coord: Coord): Pipe =
        if (coord == startCoord) startPiece
        else grid.getValue(coord)

    val allCoords: Set<Coord> = grid.keys

    companion object {
        fun parse(input: String): PipeMap {
            // Convert into a map <Coord, Char> to start with.
            val grid = input
                .lines()
                .withIndex()
                .flatMap { (y, line) ->
                    line
                        .toList()
                        .withIndex()
                        .map { (x, ch) ->
                            (x to y) to (Pipe.entries.find { it.symbol == ch } ?: error("No match for ${ch}."))
                        }
                }.toMap()

            val startCoord = grid.filterValues { it == Pipe.START }.keys.first()
            return PipeMap(startCoord, grid)
        }
    }
}

private fun findMainCurve(pipeMap: PipeMap): Set<Coord> {
    tailrec fun aux(start: Boolean = true,
                    currCoord: Coord = pipeMap.startCoord,
                    directionIn: Direction = pipeMap.getValue(pipeMap.startCoord).directions.first(),
                    curvePoints: Set<Coord> = emptySet()): Set<Coord> =
        if (!start && currCoord == pipeMap.startCoord) curvePoints
        else {
            // We don't want to leave by how we came.
            val badDirectionOut = -directionIn
            val directionOut = pipeMap.getValue(currCoord).directions.first { it != badDirectionOut }
            aux(false, currCoord + directionOut, directionOut, curvePoints + currCoord)
        }
    return aux()
}

fun answer1(input: String): Int =
    findMainCurve(PipeMap.parse(input)).size / 2

fun answer2(input: String): Int {
    val pipeMap = PipeMap.parse(input)
    val mainCurvePoints = findMainCurve(pipeMap)
    val crossings = setOf(Pipe.VERTICAL, Pipe.NORTHEAST, Pipe.NORTHWEST)
    return (pipeMap.allCoords - mainCurvePoints).count { coord ->
        // We want the number of points inside the polygon that is the main curve.
        // Cast a ray to the west from (the top half of) each point not on the main curve.
        // Count the number of times it intersections VERTICAL, NORTHEAST, and NORTHWEST (due to casting from top half).
        // By the ray casting algorithm for polygon point inclusion from the Jordan Curve Theorem,
        // an even number of crossings means a point is out, and an odd number means a point is inside.
        (0..<coord.first)
            .map { it to coord.second }
            .intersect(mainCurvePoints)
            .count { pipeMap.getValue(it) in crossings } % 2 == 1
    }
}

fun main() {
    val input = object {}.javaClass.getResource("/day10.txt")!!.readText()

    println("--- Day 10: Pipe Maze ---")

    // Answer 1: 6820
    println("Part 1: ${answer1(input)}")

    // Answer 2: 337
    println("Part 2: ${answer2(input)}")
}