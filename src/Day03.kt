import kotlin.math.max
import kotlin.math.min

fun main() {
    val numRegex = Regex("\\d+")
    fun isSymbol(c: Char): Boolean {
        return c != '.' && !c.isLetterOrDigit()
    }

    fun part1(input: List<String>): Int = input.indices.sumOf { row ->
        val (maxRow, maxCol) = Pair(input.size - 1, input[row].length - 1)
        numRegex.findAll(input[row]).filter { match ->
            val (start, end) = Pair(match.range.first, match.range.last)
            (max(row - 1, 0)..min(row + 1, maxRow)).any { row ->
                (max(start - 1, 0)..min(end + 1, maxCol)).any { col ->
                    isSymbol(input[row][col])
                }
            }
        }.map { it.value.toInt() }.sum()
    }

    fun part2(input: List<String>): Int = input.indices.sumOf { row ->
        input[row].indices.filter { col -> input[row][col] == '*' }.map { col ->
            (max(row - 1, 0)..min(row + 1, input.size)).flatMap { row ->
                numRegex.findAll(
                    input[row].subSequence(max(col - 1, 0), min(col + 2, input[row].length))
                ).map { Pair(row, max(col - 1, 0) + it.range.first) }
            }.toList()
        }.filter { it.size == 2 }.sumOf {
            it.map { (row, i) ->
                numRegex.findAll(input[row]).first { match -> i in match.range }.value.toInt()
            }.reduce(Int::times)
        }
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}