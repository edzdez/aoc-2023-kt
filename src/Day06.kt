import java.lang.IllegalStateException

fun main() {
    val numRegex = Regex("\\d+")
    val spaceRegex = Regex("\\s+")

    fun findFirst(range: LongProgression, time: Long, dist: Long): Long {
        for (i in range) {
            if (i * (time - i) > dist) return i
        }

        throw IllegalStateException("this should not happen")
    }

    fun part1(input: List<String>): Int {
        fun parse(a: String) = numRegex.findAll(a).map { it.value.toInt() }.toList()
        val times = parse(input[0])
        val dists = parse(input[1])

        var res = 1L
        for (i in times.indices) {
            val range = 0..times[i].toLong()
            val min = findFirst(range, times[i].toLong(), dists[i].toLong())
            val max = findFirst(range.reversed(), times[i].toLong(), dists[i].toLong())

            val ways = max - min + 1
            res *= ways
        }

        return res.toInt()
    }

    fun part2(input: List<String>): Int {
        fun parse(s: String) = s.substringAfter(": ").trim().split(spaceRegex).joinToString("").toLong()
        val time = parse(input[0])
        val dist = parse(input[1])

        val range = 0..time
        val min = findFirst(range, time, dist)
        val max = findFirst(range.reversed(), time, dist)

        return (max - min + 1).toInt()
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
