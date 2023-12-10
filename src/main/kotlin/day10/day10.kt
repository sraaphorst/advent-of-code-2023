// Advent of Code 2023, Day 10.
// By Sebastian Raaphorst, 2023.

package day10

private typealias Coord = Pair<Int, Int>

private enum class Direction(val deltaX: Int, val deltaY: Int) {
    NORTH(0, -1),
    SOUTH(0, 1),
    EAST(1, 0),
    WEST(-1, 0)
}

private operator fun Coord.plus(direction: Direction): Coord =
    (first + direction.deltaX) to (second + direction.deltaY)

private operator fun Direction.unaryMinus() = when(this) {
    Direction.NORTH -> Direction.SOUTH
    Direction.SOUTH -> Direction.NORTH
    Direction.EAST -> Direction.WEST
    Direction.WEST -> Direction.EAST
}

private enum class Pipe(val symbol: Char, val directions: Set<Direction>) {
    VERTICAL('|', setOf(Direction.NORTH, Direction.SOUTH)),
    HORIZONTAL('-', setOf(Direction.WEST, Direction.EAST)),
    NORTHEAST('L', setOf(Direction.NORTH, Direction.EAST)),
    NORTHWEST('J', setOf(Direction.NORTH, Direction.WEST)),
    SOUTHWEST('7', setOf(Direction.SOUTH, Direction.WEST)),
    SOUTHEAST('F', setOf(Direction.SOUTH, Direction.EAST)),
    GROUND('.', emptySet()),
    START('S', emptySet())
}

private class PipeMap(val startCoord: Coord, private val grid: Map<Coord, Pipe>) {
    val width = grid.keys.maxBy { it.first }.first
    val height = grid.keys.maxBy { it.second }.second

    fun neighbourhood(coord: Coord): Set<Coord> =
        Direction
            .entries
            .map { (coord.first + it.deltaX) to (coord.second + it.deltaY) }
            .filter { it.first in (0..<width) && it.second in  (0..<height)}.toSet()

    val startPiece: Pipe = run {
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

    // Now we use the Jordan Curve Theorem by casting a ray to the left for each point not on the main curve to
    // determine if it is considered "inside" the main curve.
    // If it crosses the main curve an odd number of times, it is inside the main curve.
    // If it crosses the main curve an even number of times, it is outside the main curve.
    // Due to the nature of the curve, we must decide what constitutes casting the main curve:
    // For each point, we will consider casting out in the "top half" of the point.
    // Thus, crossing the VERTICAL, NORTHEAST, and NORTHWEST pipe pieces will contribute to the crossing number, but
    // crossing the HORIZONTAL, SOUTHEAST, and SOUTHWEST pipe pieces will not.
    // In this case, we don't even bother counting the crossing number: we just alternate parity to determine
    // which points return true.
    // This could be optimized by going left or right (or even up or down, which would require different logic) but
    // the optimization time improvement is not worth the effort.
    val crossings = setOf(Pipe.VERTICAL, Pipe.NORTHEAST, Pipe.NORTHWEST)
    return (pipeMap.allCoords - mainCurvePoints).count { coord ->
        (0..<coord.first).fold(false) { parity, x ->
            val checkCoord = (x to coord.second)
            if (checkCoord in mainCurvePoints && pipeMap.getValue(checkCoord) in crossings) !parity else parity }
    }
}

fun main() {
    val input = object {}.javaClass.getResource("/day10.txt")!!.readText()

    println("--- Day 10: Pipe Maze ---")

    // Answer 1: 6820
    println("Part 1: ${answer1(input)}")

    // Answer 2: 1124
    println("Part 2: ${answer2(input)}")
}