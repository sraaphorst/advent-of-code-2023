package common

// Tail of a list for simplicity.
fun <A> List<A>.tail(): List<A> = drop(1)

// Join a List of List.
fun <A> List<List<A>>.joinWithSeparator(sep: A): List<A> = when {
    isEmpty() -> emptyList()
    else -> flatMapIndexed { idx, lst ->
        if (idx < size - 1) lst + sep else lst
    }
}

fun <A> List<A>.split(sep: A): List<List<A>> {
    // Helper function to recursively process the list
    tailrec fun aux(remaining: List<A> = this, current: List<A> = emptyList(), acc: List<List<A>> = emptyList()): List<List<A>> {
        return when {
            remaining.isEmpty() -> acc + listOf(current)
            remaining.first() == sep -> aux(remaining.drop(1), emptyList(), acc + listOf(current))
            else -> aux(remaining.drop(1), current + remaining.first(), acc)
        }
    }
    return aux()
}
