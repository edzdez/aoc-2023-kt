import kotlin.math.abs
import kotlin.math.min
import kotlin.math.max

fun main() {
    fun getEmptyRows(input: List<String>): List<Int> = input.mapIndexed { i, row ->
        if (!row.contains('#')) i else -1
    }.filter { it != -1 }.toList()

    fun getEmptyCols(input:List<String>): List<Int> {
        val res = mutableListOf<Int>()
        for (col in input[0].indices) {
            if (!input.map { it[col] }.contains('#'))
                res.add(col)
        }

        return res
    }

    fun expand(input: List<String>): List<List<Char>> {
        val emptyRows = getEmptyRows(input)
        val emptyCols = getEmptyCols(input)

        val expanded = mutableListOf<MutableList<Char>>()
        for ((y, row) in input.withIndex()) {
            if (emptyRows.contains(y)) {
                expanded.add(MutableList(input.first().length + emptyCols.size) { '.' })
                expanded.add(MutableList(input.first().length + emptyCols.size) { '.' })
            }
            else {
                expanded.add(mutableListOf())
                for ((x, cell) in row.withIndex()) {
                    if (emptyCols.contains(x)) {
                        expanded.last().add('.')
                        expanded.last().add('.')
                    } else {
                        expanded.last().add(cell)
                    }
                }
            }
        }

        return expanded
    }

    fun findGalaxies(map: List<List<Char>>): List<Pair<Int, Int>> {
        val res = mutableListOf<Pair<Int, Int>>()
        for ((y, row) in map.withIndex()) {
            for ((x, cell) in row.withIndex()) {
                if (cell == '#')
                    res.add(Pair(y, x))
            }
        }

        return res
    }

    fun part1(input: List<String>): Int {
        val map = expand(input)
        val galaxies = findGalaxies(map)
        var res = 0
        for (i in galaxies.indices) {
            val (y1, x1) = galaxies[i]
            for (j in i + 1..<galaxies.size) {
                val (y2, x2) = galaxies[j]

                res += abs(x2 - x1) + abs(y2 - y1)
            }
        }
        return res
    }

    fun part2(input: List<String>): Long {
        val emptyRows = getEmptyRows(input)
        val emptyCols = getEmptyCols(input)
        val map = input.map { it.toList() }.toList()
        val factor = 1_000_000

        val galaxies = findGalaxies(map)
        var res = 0L
        for (i in galaxies.indices) {
            val (y1, x1) = galaxies[i]
            for (j in i + 1..<galaxies.size) {
                val (y2, x2) = galaxies[j]

                val betweenRows = emptyRows.filter { min(y1, y2) < it && it < max(y1, y2) }
                val betweenCols = emptyCols.filter { min(x1, x2) < it && it < max(x1, x2) }

                res += abs(x2 - x1) + abs(y2 - y1)
                res += betweenRows.size.toLong() * (factor - 1)
                res += betweenCols.size.toLong() * (factor - 1)
            }
        }

        return res
    }

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374)
//    check(part2(testInput) == 1030)
//    check(part2(testInput) == 8410)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}