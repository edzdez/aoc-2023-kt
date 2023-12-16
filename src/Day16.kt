import kotlin.math.max

fun main() {
    var grid: List<List<Char>> = listOf(listOf())
    var maxX = 0
    var maxY = 0

    fun run(
        energized: MutableSet<Pair<Int, Int>>,
        seen: MutableSet<Pair<Pair<Int, Int>, Pair<Int, Int>>>,
        loc: Pair<Int, Int>,
        d: Pair<Int, Int>
    ) {
        var (x, y) = loc
        val (dx, dy) = d

        if (seen.contains(loc to d)) return

        energized.add(loc)
        seen.add(loc to d)

        x += dx
        y += dy

        if (!(x in 0..<maxX && y in 0..<maxY)) return

        when (grid[y][x]) {
            '/' -> {
                if (dx == 1) run(energized, seen, x to y, 0 to -1)
                else if (dx == -1) run(energized, seen, x to y, 0 to 1)
                else if (dy == 1) run(energized, seen, x to y, -1 to 0)
                else if (dy == -1) run(energized, seen, x to y, 1 to 0)
            }

            '\\' -> {
                if (dx == 1) run(energized, seen, x to y, 0 to 1)
                else if (dx == -1) run(energized, seen, x to y, 0 to -1)
                else if (dy == 1) run(energized, seen, x to y, 1 to 0)
                else if (dy == -1) run(energized, seen, x to y, -1 to 0)
            }

            '-' -> {
                if (dy != 0) {
                    run(energized, seen, x to y, 1 to 0)
                    run(energized, seen, x to y, -1 to 0)
                } else run(energized, seen, x to y, d)
            }

            '|' -> {
                if (dx != 0) {
                    run(energized, seen, x to y, 0 to 1)
                    run(energized, seen, x to y, 0 to -1)
                } else run(energized, seen, x to y, d)
            }

            '.' -> {
                run(energized, seen, x to y, d)
            }
        }
    }

    fun part1(input: List<String>): Int {
        grid = input.map { it.toList() }.toList()
        maxY = grid.size
        maxX = grid[0].size

        val energized = mutableSetOf<Pair<Int, Int>>()
        val seen = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
        run(energized, seen, -1 to 0, 1 to 0)

        return energized.size - 1
    }

    fun part2(input: List<String>): Int {
        var ans = 0

        for (x in (0..<maxX)) {
            val energized = mutableSetOf<Pair<Int, Int>>()
            val seen = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()

            run(energized, seen, x to -1, 0 to 1)
            ans = max(ans, energized.size - 1)
            energized.clear()
            seen.clear()

            run(energized, seen, x to maxY, 0 to -1)
            ans = max(ans, energized.size - 1)
        }

        for (y in (0..<maxY)) {
            val energized = mutableSetOf<Pair<Int, Int>>()
            val seen = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()

            run(energized, seen, -1 to y, 1 to 0)
            ans = max(ans, energized.size - 1)
            energized.clear()
            seen.clear()

            run(energized, seen, maxX to y, -1 to 0)
            ans = max(ans, energized.size - 1)
        }

        return ans
    }

    val testInput = readInput("Day16_test")
    check(part1(testInput) == 46)
    check(part2(testInput) == 51)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}