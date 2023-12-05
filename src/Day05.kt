import kotlin.math.max
import kotlin.math.min

fun main() {
    data class Almanac(
        val seeds: List<ULong>, val maps: List<MutableMap<Pair<ULong, ULong>, ULong>>
    )

    fun parseInput1(input: List<String>): Almanac {
        val seeds = input[0].substringAfter("seeds: ").split(" ").map(String::toULong).toList()
        val maps = mutableListOf<MutableMap<Pair<ULong, ULong>, ULong>>()

        val it = input.drop(2).listIterator()
        while (it.hasNext()) {
            it.next()
            val map = mutableMapOf<Pair<ULong, ULong>, ULong>()
            while (it.hasNext()) {
                val line = it.next()
                if (line.isBlank()) break

                val nums = line.split(' ').map(String::toULong)
                map[Pair(nums[1], nums[1] + nums[2] - 1UL)] = nums[0]
            }

            maps.add(map)
        }

        return Almanac(seeds, maps)
    }

    fun findInMap(map: MutableMap<Pair<ULong, ULong>, ULong>, num: ULong): ULong {
        val a = map.keys.filter { num >= it.first && num <= it.second }
        if (a.isEmpty()) return num
        val startRange = a.first()
        val end = map[startRange]!!
        return end + (num - startRange.first)
    }

    fun joinRanges(ranges: List<Pair<ULong, ULong>>): List<Pair<ULong, ULong>> {
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

    fun findIntersection(range: Pair<ULong, ULong>, from: Pair<ULong, ULong>): Pair<ULong, ULong>? {
        return if (range.first <= from.first && range.second >= from.second) from
        else if (from.first <= range.first && from.second >= range.second) range
        else if ((from.first <= range.second && range.second <= from.second) || (range.first <= from.second && from.second <= range.second)) Pair(
            max(range.first, from.first),
            min(range.second, range.second)
        )
        else if ((from.first <= range.first && range.first <= from.second) || (range.first <= from.first && from.first <= range.second)) Pair(
            max(range.first, from.first),
            min(range.second, range.second)
        )
        else null
    }

    fun mapRange(map: MutableMap<Pair<ULong, ULong>, ULong>, range: Pair<ULong, ULong>): List<Pair<ULong, ULong>> {
        val satisfiedRange = mutableListOf<Pair<ULong, ULong>>()
        val actualRange = mutableListOf<Pair<ULong, ULong>>()
        for ((fromRange, to) in map) {
            val intersection = findIntersection(range, fromRange)
            if (intersection != null) {
                satisfiedRange.add(intersection)

                val lDiff = intersection.first - fromRange.first
                val actual = Pair(to + lDiff, to + lDiff + (intersection.second - intersection.first))
//                println("int: $range, $fromRange = $intersection")
//                println("act: $actual")
                actualRange.add(actual)
            }
        }

        if (satisfiedRange.isEmpty()) {
            actualRange.add(range)
            return actualRange
        }

        satisfiedRange.sortBy { it.first }
//        println("sat $satisfiedRange")
        for ((r1, r2) in satisfiedRange.windowed(2, 1)) {
            actualRange.add(Pair(r1.second + 1UL, r2.second - 1UL))
        }
        val first = satisfiedRange.firstOrNull()
        if (first != null && first.first > range.first) actualRange.add(Pair(range.first, first.first - 1UL))
        val last = satisfiedRange.lastOrNull()
        if (last != null && last.second < range.second) actualRange.add(Pair(last.second + 1UL, range.second))

        return actualRange
    }

    fun part1(input: List<String>): ULong {
        val parsed = parseInput1(input)

        var locNumber = ULong.MAX_VALUE
        for (seed in parsed.seeds) {
            var num = seed
            for (map in parsed.maps) {
                num = findInMap(map, num)
            }
            locNumber = min(locNumber, num)
        }

        return locNumber
    }

    fun part2(input: List<String>): ULong {
        val parsed = parseInput1(input)
        val initialRanges = parsed.seeds.chunked(2).map { Pair(it[0], it[0] + it[1] - 1UL) }.sortedBy { it.first }
        var joinedRanges = joinRanges(initialRanges)
//        println("ranges: $joinedRanges")

        for (map in parsed.maps) {
            val newRanges = mutableListOf<Pair<ULong, ULong>>()
            for (range in joinedRanges) {
                val add = mapRange(map, range)
                newRanges.addAll(add)
            }
            newRanges.sortBy { it.first }
//            println("ranges: $newRanges")
            joinedRanges = joinRanges(newRanges)
//            joinedRanges = newRanges
        }

        // works for my input, probably doesn't work for anyone else :)
        return joinedRanges.filter { it.first != 0UL }.minOfOrNull { it.first }!!
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35UL)
    check(part2(testInput) == 46UL)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}