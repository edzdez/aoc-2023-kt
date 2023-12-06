fun main() {
    data class Races(val times: List<Int>, val dists: List<Int>)

    val numRegex = Regex("\\d+")

    fun parseInput(input: List<String>): Races {
        val times = numRegex.findAll(input[0]).map { it.value.toInt() }.toList()
        val dists = numRegex.findAll(input[1]).map { it.value.toInt() }.toList()
        return Races(times, dists)
    }

    fun part1(input: List<String>): Int {
        val parsed = parseInput(input)
        var res = 1
        for (i in parsed.times.indices) {
            val lens = mutableListOf<Int>()
            for (j in 0..parsed.times[i]) {
                val timeLeft = parsed.times[i] - j
                lens.add(j * timeLeft)
            }
            val ways = lens.count { it > parsed.dists[i] }
            res *= ways
        }

        return res
    }

    fun part2(input: List<String>): Int {
        val time = input[0].substringAfter(": ").trim().split(Regex("\\s+")).joinToString("").toLong()
        val dist = input[1].substringAfter(": ").trim().split(Regex("\\s+")).joinToString("").toLong()

        val lens = mutableListOf<Long>()
        for (j in 0..time) {
            val timeLeft = time - j
            lens.add(j * timeLeft)
        }
        return lens.count { it > dist }
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
