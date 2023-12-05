import kotlin.math.max
import kotlin.math.min

fun main() {
    data class Almanac(
        val seeds: List<Long>, val maps: List<MutableMap<Pair<Long, Long>, Long>>
    )

    fun parseInput1(input: List<String>): Almanac {
        val seeds = input[0].substringAfter("seeds: ").split(" ").map(String::toLong).toList()
        val maps = mutableListOf<MutableMap<Pair<Long, Long>, Long>>()

        val it = input.drop(2).listIterator()
        while (it.hasNext()) {
            it.next()
            val map = mutableMapOf<Pair<Long, Long>, Long>()
            while (it.hasNext()) {
                val line = it.next()
                if (line.isBlank()) break

                val nums = line.split(' ').map(String::toLong)
                map[Pair(nums[1], nums[1] + nums[2] - 1L)] = nums[0]
            }

            maps.add(map)
        }

        return Almanac(seeds, maps)
    }

    fun findInMap(map: MutableMap<Pair<Long, Long>, Long>, num: Long): Long {
        val a = map.keys.filter { num >= it.first && num <= it.second }
        if (a.isEmpty()) return num
        val startRange = a.first()
        val end = map[startRange]!!
        return end + (num - startRange.first)
    }

    fun joinRanges(ranges: List<Pair<Long, Long>>): List<Pair<Long, Long>> {
        val retRanges = mutableListOf(ranges[0])
        for (range in ranges) {
            if (range.second <= retRanges.last().second) {
                continue
            } else if (range.first <= retRanges.last().second) {
                retRanges[retRanges.size - 1] =
                    Pair(min(retRanges.last().first, range.first), max(retRanges.last().second, range.second))
            } else {
                retRanges.add(range)
            }
        }

        return retRanges
    }

    fun findIntersection(range: Pair<Long, Long>, from: Pair<Long, Long>): Pair<Long, Long>? {
        return if (range.first <= from.first && range.second >= from.second) from
        else if (from.first <= range.first && from.second >= range.second) range
        else if ((from.first <= range.second && range.second <= from.second) || (range.first <= from.second && from.second <= range.second)) Pair(
            max(range.first, from.first), min(range.second, range.second)
        ) else null
    }

    fun mapRange(map: MutableMap<Pair<Long, Long>, Long>, range: Pair<Long, Long>): List<Pair<Long, Long>> {
        val satisfiedRange = mutableListOf<Pair<Long, Long>>()
        val actualRange = mutableListOf<Pair<Long, Long>>()
        for ((fromRange, to) in map) {
            val intersection = findIntersection(range, fromRange)
            if (intersection != null) {
                satisfiedRange.add(intersection)
                val lDiff = intersection.first - fromRange.first
                val actual = Pair(to + lDiff, to + lDiff + (intersection.second - intersection.first))
                actualRange.add(actual)
            }
        }

        satisfiedRange.add(Pair(range.first - 1, range.first - 1))
        satisfiedRange.add(Pair(range.second + 1, range.second + 1))
        satisfiedRange.sortBy { it.first }

        for ((a, b) in satisfiedRange.windowed(2, 1)) {
            if (a.second + 1 != b.first) actualRange.add(Pair(a.second + 1, b.first - 1))
        }
        return actualRange
    }

    fun part1(input: List<String>): Long {
        val parsed = parseInput1(input)

        var locNumber = Long.MAX_VALUE
        for (seed in parsed.seeds) {
            var num = seed
            for (map in parsed.maps) {
                num = findInMap(map, num)
            }
            locNumber = min(locNumber, num)
        }

        return locNumber
    }

    fun part2(input: List<String>): Long {
        val parsed = parseInput1(input)
        val initialRanges = parsed.seeds.chunked(2).map { Pair(it[0], it[0] + it[1] - 1L) }.sortedBy { it.first }
        var joinedRanges = joinRanges(initialRanges)

        for (map in parsed.maps) {
            val newRanges = mutableListOf<Pair<Long, Long>>()
            for (range in joinedRanges) {
                val add = mapRange(map, range)
                newRanges.addAll(add)
            }
            newRanges.sortBy { it.first }
            joinedRanges = joinRanges(newRanges)
        }

        return joinedRanges.first().first
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}