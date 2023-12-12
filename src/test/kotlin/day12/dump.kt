package day12

import arrow.core.MemoizedDeepRecursiveFunction

val fibonacciWorker = MemoizedDeepRecursiveFunction<Int, Int> { n ->
    println("Called with $n")
    when (n) {
        0 -> 0
        1 -> 1
        else -> callRecursive(n - 1) + callRecursive(n - 2)
    }
}

fun main() {
    println(fibonacciWorker(10))
    println(fibonacciWorker(10))
}
