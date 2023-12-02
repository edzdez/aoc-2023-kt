import kotlin.math.max

fun main() {
    data class Game(val id: Int, var draws: List<Triple<Int, Int, Int>>)

    val RED = 12
    val GREEN = 13
    val BLUE = 14

    fun parseGame(game: String): Game {
        val id = game.substringAfter("Game ").substringBefore(':').toInt()

        val draws = game.substringAfter(": ").split("; ").map {
            val drawStr = it.split(", ").toList()
            var red = 0
            var green = 0
            var blue = 0
            for (color in drawStr) {
                if (color.endsWith("red")) {
                    red += color.substringBefore(' ').toInt()
                } else if (color.endsWith("green")) {
                    green += color.substringBefore(' ').toInt()
                } else {
                    blue += color.substringBefore(' ').toInt()
                }
            }

            Triple(red, green, blue)
        }.toList()

        return Game(id, draws)
    }

    fun part1(input: List<String>): Int {
        val games = input.map { parseGame(it) }

        var ans = 0
        for (game in games) {
            var flag = true
            for (set in game.draws) {
                if (set.first > RED || set.second > GREEN || set.third > BLUE)
                    flag = false
            }
            if (flag)
                ans += game.id
        }

        return ans
    }

    fun part2(input: List<String>): Int {
        val games = input.map { parseGame(it) }

        var ans = 0
        for (game in games) {
            var red = 0
            var green = 0
            var blue = 0
            for (set in game.draws) {
                red = max(red, set.first)
                green = max(green, set.second)
                blue = max(blue, set.third)
            }

            val power = red * green * blue
            ans += power
        }

        return ans
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}