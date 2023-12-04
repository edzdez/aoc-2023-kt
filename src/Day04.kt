import kotlin.math.pow

fun main() {
    data class Card(val winningNumbers: HashSet<Int>, val numbers: HashSet<Int>)

    val numberRegex = Regex("\\d+")

    fun processCard(line: String): Card {
        fun findNumbers(nums: String) = numberRegex.findAll(nums).map { it.value.toInt() }.toHashSet()

        val winningNumbers = findNumbers(line.substringAfter(": ").substringBefore(" | "))
        val numbers = findNumbers(line.substringAfter(" | "))

        return Card(winningNumbers, numbers)
    }

    fun part1(input: List<String>): Int = input.asSequence().map(::processCard).map {
        it.numbers.intersect(it.winningNumbers).count()
    }.filter { it != 0 }.map { 2.0.pow(it - 1).toInt() }.sum()

    fun part2(input: List<String>): Int {
        val cards = input.map(::processCard)
        val noCards = MutableList(cards.size) { 1 }
        for ((i, card) in cards.withIndex()) {
            val matches = card.numbers.intersect(card.winningNumbers).count()
            for (j in 0..<matches) {
                noCards[i + j + 1] += noCards[i]
            }
        }

        return noCards.sum()
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}