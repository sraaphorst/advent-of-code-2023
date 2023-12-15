// Advent of Code common functions.
// By Sebastian Raaphorst, 2023.

package common

// Transpose of a List<String>, where the Strings represent rows.
//fun List<String>.transpose(): List<String> = when {
//    isEmpty() -> listOf()
//    else -> first().indices.map { colIdx -> joinToString(separator = "") { it[colIdx].toString() } }
//}

// Transpose of a List<List<T>>, where the inner lists represent the rows.
fun <T> List<List<T>>.transpose(): List<List<T>> = when {
    isEmpty() -> listOf()
    else -> first().indices.map { colIdx -> map { row -> row[colIdx] } }
}

// Rotate a List<List<T>> by 90 degrees clockwise.
fun <T> List<List<T>>.rotateClockwise(): List<List<T>> =
    transpose().map(List<T>::reversed)

// Rotate a List<List<T>> by 90 degrees counterclockwise.
fun <T> List<List<T>>.rotateCounterClockwise(): List<List<T>> =
    transpose().reversed()




