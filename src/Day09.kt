fun main() {
    val numRegex = Regex("-?\\d+")

    fun computeHistory(line: String): List<List<Int>> {
        val values = mutableListOf(numRegex.findAll(line).map { it.value.toInt() }.toList())
        while (!values.last().all { it == 0 }) {
            values.add(values.last().windowed(2, 1).map { it[1] - it[0] }.toList())
        }
        values.toList()
        return values
    }

    fun part1(input: List<String>): Int = input.map(::computeHistory).sumOf { it -> it.sumOf { it.last() } }
    fun part2(input: List<String>): Int = input.map(::computeHistory).sumOf {
        it.foldRight(0) { nums, acc: Int ->
            nums.first() - acc
        }
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}