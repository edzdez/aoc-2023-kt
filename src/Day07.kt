import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

enum class HandType {
    Five, Four, Full, Three, Two, One, High,
}

fun main() {
    data class Hand(val cards: List<Int>)

    fun toRank(c: Char): Int {
        if (c.isDigit()) return c.digitToInt()
        if (c == 'T') return 10
        if (c == 'J') return 11
        if (c == 'Q') return 12
        if (c == 'K') return 13
        if (c == 'A') return 14

        throw IllegalArgumentException("this shouldn't happen :)")
    }

    fun parseInput(line: String): Pair<Hand, Int> {
        val hand = Hand(line.substringBefore(' ').map(::toRank))
        val bid = line.substringAfter(' ').toInt()
        return Pair(hand, bid)
    }

    fun getType(cards: List<Int>): HandType {
        val uniq = cards.toHashSet()
        if (uniq.size == 1) return HandType.Five
        val counts = uniq.map { n -> cards.count { it == n } }.sorted()
        if (counts == listOf(5)) return HandType.Five
        if (counts == listOf(1, 4)) return HandType.Four
        if (counts == listOf(2, 3)) return HandType.Full
        if (counts.contains(3)) return HandType.Three
        if (counts == listOf(1, 2, 2)) return HandType.Two
        if (counts.contains(2)) return HandType.One
        return HandType.High
    }

    fun getType2(cards: List<Int>): HandType {
        val js = cards.count { it == 1 }
        val uniq = cards.filter { it != 1 }.toHashSet()
        val counts = uniq.map { n -> cards.count { it == n } }.sorted()
        if (js == 5) return HandType.Five
        if (js + counts.last() == 5) return HandType.Five
        if (js + counts.last() == 4) return HandType.Four
        if (js == 1 && counts == listOf(2, 2)) return HandType.Full
        if (counts == listOf(2, 3)) return HandType.Full
        if (js + counts.last() == 3) return HandType.Three
        if (counts == listOf(1, 2, 2)) return HandType.Two
        if (js + counts.last() == 2) return HandType.One
        return HandType.High
    }

    fun comp(lhs: Hand, rhs: Hand, part1: Boolean): Int {
        val lhsType = if (part1) {
            getType(lhs.cards)
        } else {
            getType2(lhs.cards)
        }
        val rhsType = if (part1) {
            getType(rhs.cards)
        } else {
            getType2(rhs.cards)
        }

        if (lhsType > rhsType) return -1
        if (lhsType < rhsType) return 1

        for (i in 0..5) {
            val ans = lhs.cards[i] - rhs.cards[i]
            if (ans != 0) return ans
        }

        throw IllegalStateException("this should not happen")
    }

    fun part1(input: List<String>): Int {
        val parsed = input.map(::parseInput)
        return parsed.sortedWith { lhs, rhs -> comp(lhs.first, rhs.first, true) }.withIndex()
            .sumOf { (idx, hand) -> (idx + 1) * hand.second }
    }

    fun part2(input: List<String>): Int {
        val parsed = input.map(::parseInput)
            .map { card -> Pair(Hand(card.first.cards.map { if (it == 11) 1 else it }), card.second) }
        return parsed.sortedWith { lhs, rhs -> comp(lhs.first, rhs.first, false) }.withIndex()
            .sumOf { (idx, hand) -> (idx + 1) * hand.second }
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}