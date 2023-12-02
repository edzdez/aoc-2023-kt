import kotlin.math.max

fun main() {
    data class Game(val id: Int, var sets: List<Triple<Int, Int, Int>>)

    val (RED, GREEN, BLUE) = Triple(12, 13, 14)

    fun parseGame(game: String): Game {
        val id = game.substringAfter("Game ").substringBefore(':').toInt()

        val sets = game.substringAfter(": ").split("; ").map {
            val set = it.split(", ").toList()
            var (red, green, blue) = Triple(0, 0, 0)
            for (color in set) {
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

        return Game(id, sets)
    }

    fun part1(input: List<String>): Int = input.map(::parseGame).filter { game ->
        game.sets.all {
            it.first <= RED && it.second <= GREEN && it.third <= BLUE
        }
    }.fold(0) { acc, game -> acc + game.id }

    fun part2(input: List<String>): Int = input.map(::parseGame).fold(0) { res, game ->
        res + game.sets.fold(Triple(0, 0, 0)) { acc, set ->
            Triple(max(acc.first, set.first), max(acc.second, set.second), max(acc.third, set.third))
        }.toList().reduce(Int::times)
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}