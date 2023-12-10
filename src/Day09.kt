import java.util.*
import kotlin.math.max

fun main() {
    fun findStart(input: List<String>): Pair<Int, Int> = input.mapIndexed { i, line ->
        if (line.contains('S')) Pair(i, line.indexOf('S')) else Pair(-1, -1)
    }.find { it != Pair(-1, -1) }!!

    fun flood(map: List<List<Char>>, start: Pair<Int, Int>): MutableList<MutableList<Int?>> {
        val res = MutableList(map.size) { MutableList<Int?>(map[0].size) { null } }
        val q = LinkedList<Pair<Int, Int>>()
        val tryUpdate = { y: Int, x: Int, v: Int ->
            if (y >= 0 && y < map.size && x >= 0 && x < map[0].size && res[y][x] == null) {
                res[y][x] = v + 1
                q.add(Pair(y, x))
            }
        }

        res[start.first][start.second] = 0
        q.add(start)

        while (q.isNotEmpty()) {
            val (y, x) = q.remove()

            val pipe = map[y][x]
            when (pipe) {
                '|' -> {
                    tryUpdate(y - 1, x, res[y][x]!!)
                    tryUpdate(y + 1, x, res[y][x]!!)
                }

                '-' -> {
                    tryUpdate(y, x - 1, res[y][x]!!)
                    tryUpdate(y, x + 1, res[y][x]!!)
                }

                'L' -> {
                    tryUpdate(y - 1, x, res[y][x]!!)
                    tryUpdate(y, x + 1, res[y][x]!!)
                }

                'J' -> {
                    tryUpdate(y - 1, x, res[y][x]!!)
                    tryUpdate(y, x - 1, res[y][x]!!)
                }

                '7' -> {
                    tryUpdate(y + 1, x, res[y][x]!!)
                    tryUpdate(y, x - 1, res[y][x]!!)
                }

                'F' -> {
                    tryUpdate(y + 1, x, res[y][x]!!)
                    tryUpdate(y, x + 1, res[y][x]!!)
                }

                '.' -> {
                    res[y][x] = -1
                }
            }
        }

        return res
    }

    fun findMe(
        theLoop: MutableList<MutableList<Int?>>,
        map: List<List<Char>>,
        res: MutableList<MutableList<Int?>>,
        thePoint: Pair<Int, Int>
    ) {
        val q = LinkedList<Pair<Int, Int>>()
        val tryUpdate = { y: Int, x: Int, v: Int ->
            if (y >= 0 && y < res.size && x >= 0 && x < res[0].size && res[y][x] == v - 1) {
                theLoop[y][x] = v - 1
                q.add(Pair(y, x))
            }
        }

        theLoop[thePoint.first][thePoint.second] = 0
        q.add(thePoint)

        while (q.isNotEmpty()) {
            val (y, x) = q.remove()

            val pipe = map[y][x]
            when (pipe) {
                '|' -> {
                    tryUpdate(y - 1, x, res[y][x]!!)
                    tryUpdate(y + 1, x, res[y][x]!!)
                }

                '-' -> {
                    tryUpdate(y, x - 1, res[y][x]!!)
                    tryUpdate(y, x + 1, res[y][x]!!)
                }

                'L' -> {
                    tryUpdate(y - 1, x, res[y][x]!!)
                    tryUpdate(y, x + 1, res[y][x]!!)
                }

                'J' -> {
                    tryUpdate(y - 1, x, res[y][x]!!)
                    tryUpdate(y, x - 1, res[y][x]!!)
                }

                '7' -> {
                    tryUpdate(y + 1, x, res[y][x]!!)
                    tryUpdate(y, x - 1, res[y][x]!!)
                }

                'F' -> {
                    tryUpdate(y + 1, x, res[y][x]!!)
                    tryUpdate(y, x + 1, res[y][x]!!)
                }

                '.' -> {
                    res[y][x] = -1
                }
            }
        }
    }

    fun tryFlood(theLoop: MutableList<MutableList<Int?>>, y: Int, x: Int) {
        val q = LinkedList<Pair<Int, Int>>()
        val tryUpdate = { y: Int, x: Int ->
            if (y >= 0 && y < theLoop.size && x >= 0 && x < theLoop[0].size && theLoop[y][x] == null) {
                theLoop[y][x] = -1
                q.add(Pair(y, x))
            }
        }

        theLoop[y][x] = -1
        q.add(Pair(y, x))

        while (q.isNotEmpty()) {
            val (y, x) = q.remove()

            tryUpdate(y - 1, x)
            tryUpdate(y + 1, x)
            tryUpdate(y, x - 1)
            tryUpdate(y, x + 1)
        }
    }

    fun fillNonEnclosed(theLoop: MutableList<MutableList<Int?>>, map: List<List<Char>>) {
        var a = 0
        for ((y, row) in theLoop.withIndex()) {
            for ((x, cell) in row.withIndex()) {
                if (cell == 1 && (map[y][x] == '|' || map[y][x] == 'L' || map[y][x] == 'J'))
                    a += 1
                else if (cell == null && a % 2 == 0) {
                    tryFlood(theLoop, y, x)
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val start = findStart(input)
        val asLists = input.map { it.toMutableList() }.toMutableList()

        var res = 0
        for (c in listOf('|', '-', 'L', 'J', '7', 'F')) {
            asLists[start.first][start.second] = c
            val results = flood(asLists, start)
            val nums = results.flatMapIndexed { y, row ->
                row.filterIndexed { x, v ->
                    if (v == null || v == -1) false
                    else {
                        val top = results.getOrNull(y - 1)?.getOrNull(x)
                        val bot = results.getOrNull(y + 1)?.getOrNull(x)
                        val left = results.getOrNull(y)?.getOrNull(x - 1)
                        val right = results.getOrNull(y)?.getOrNull(x + 1)
                        val ns = listOfNotNull(top, bot, left, right).filter { it == v - 1 }

                        ns.size == 2
                    }
                }
            }.filterNotNull()
            res = max(res, nums.maxOrNull() ?: 0)
        }

        return res
    }

    // not 681 or 158 or 218
    fun part2(input: List<String>): Int {
        val start = findStart(input)
        val asLists = input.map { it.toMutableList() }.toMutableList()

        val best = part1(input)
        var theLoop = MutableList(asLists.size) { MutableList<Int?>(asLists[0].size) { null } }
        for (c in listOf('|', '-', 'L', 'J', '7', 'F')) {
            asLists[start.first][start.second] = c
            val results = flood(asLists, start)
            val nums = results.flatMapIndexed { y, row ->
                row.filterIndexed { x, v ->
                    if (v == null || v == -1) false
                    else {
                        val top = results.getOrNull(y - 1)?.getOrNull(x)
                        val bot = results.getOrNull(y + 1)?.getOrNull(x)
                        val left = results.getOrNull(y)?.getOrNull(x - 1)
                        val right = results.getOrNull(y)?.getOrNull(x + 1)
                        val ns = listOfNotNull(top, bot, left, right).filter { it == v - 1 }

                        ns.size == 2
                    }
                }
            }.filterNotNull()

            if (nums.maxOrNull() != null && nums.max() == best) {
                val thePoint = results.mapIndexed { y, row ->
                    Pair(y, row.indexOf(nums.max()))
                }.filter { it.second != -1 }

                findMe(theLoop, asLists, results, thePoint.first())
                break
            }
        }

        theLoop = theLoop.map { it.map { if (it != null) 1 else null }.toMutableList() }.toMutableList()
        fillNonEnclosed(theLoop, asLists)
        println(theLoop)

        return theLoop.sumOf { it.count { it == null } }
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 4)
    val testInput2 = readInput("Day10_test2")
    check(part1(testInput2) == 8)
    val testInput3 = readInput("Day10_test3")
    check(part2(testInput3) == 4)
    val testInput4 = readInput("Day10_test4")
    check(part2(testInput4) == 4)
    val testInput5 = readInput("Day10_test5")
    check(part2(testInput5) == 8)
    val testInput6 = readInput("Day10_test6")
    check(part2(testInput6) == 10)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}