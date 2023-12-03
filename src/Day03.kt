import kotlin.math.max
import kotlin.math.min

fun main() {
    fun isSymbol(c: Char): Boolean {
        return c != '.' && !c.isLetterOrDigit()
    }

    fun part1(input: List<String>): Int {
        val numRegex = Regex("\\d+")

        var res = 0
        for (row in input.indices) {
            for (match in numRegex.findAll(input[row])) {
                val (start, end) = Pair(match.range.first, match.range.last)
                val num = match.value.toInt()

                inner@ for (row in max(row - 1, 0)..min(row + 1, input.size - 1)) {
                    for (col in max(start - 1, 0)..min(end + 1, input[0].length - 1)) {
                        if (isSymbol(input[row][col])) {
                            res += num
                            break@inner
                        }
                    }
                }
            }
        }

        return res
    }

    fun part2(input: List<String>): Int {
        val numRegex = Regex("\\d+")

        var res = 0
        for (row in input.indices) {
            for (col in input[row].indices) {
                if (input[row][col] != '*') continue

                val adjacentNums = mutableListOf<Pair<Int, Int>>()
                for (dr in -1..1) {
                    if (row + dr < 0 || row + dr >= input.size) {
                        continue
                    }

                    adjacentNums.addAll(numRegex.findAll(
                        input[row + dr].subSequence(max(col - 1, 0), min(col + 2, input[row].length))
                    ).map { Pair(row + dr, max(col - 1, 0) + it.range.first) })
                }

                if (adjacentNums.size == 2) {
                    var ratio = 1
                    for ((row, i) in adjacentNums) {
                        for (match in numRegex.findAll(input[row])) {
                            if (i in match.range)
                                ratio *= match.value.toInt()
                        }
                    }

                    res += ratio
                }
            }
        }

        return res
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}