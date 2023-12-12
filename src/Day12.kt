fun main() {
    fun processLine(line: String): Pair<String, List<Int>> {
        val split = line.split(Regex("\\s+"))
        return Pair(split[0], split[1].split(',').map(String::toInt).toList())
    }

    fun trySpots(records: String, nums: List<Int>, recordIdx: Int): Long {
        if (recordIdx == records.length) {
            val groups = Regex("#+").findAll(records).map { it.value.length }.toList()
            return if (groups.size == nums.size && groups.zip(nums).all { (a, b) -> a == b }) 1 else 0
        }

        return if (records[recordIdx] == '?') {
            val s = StringBuilder(records)
            s.setCharAt(recordIdx, '#')
            val a = trySpots(s.toString(), nums, recordIdx + 1)
            s.setCharAt(recordIdx, '.')
            val b = trySpots(s.toString(), nums, recordIdx + 1)
            a + b
        } else {
            trySpots(records, nums, recordIdx + 1)
        }
    }

    fun trySpotsDP(
        map: MutableMap<Pair<String, List<Int>>, Long>,
        records: String,
        nums: List<Int>,
    ): Long {
        if (map.containsKey(Pair(records, nums))) {
            return map[Pair(records, nums)]!!
        }
        if (records.isEmpty() && nums.isEmpty()) return 1
        else if (!records.contains('?')) {
            val groups = Regex("#+").findAll(records).map { it.value.length }.toList()
            map[Pair(records, nums)] =
                if (groups.size == nums.size && groups.zip(nums).all { (a, b) -> a == b }) 1 else 0
        } else if (nums.isEmpty()) {
            map[Pair(records, nums)] = if (records.contains('#')) 0 else 1
        } else if (records.first() == '#') {
//            map[Pair(records, nums)] = if (nums.first() > 0) {
//                val newNums = nums.toMutableList()
//                --newNums[0]
//                trySpotsDP(map, records.substring(1), newNums)
//            } else 0
            return if (nums.first() > 0) {
                val newNums = nums.toMutableList()
                --newNums[0]
                trySpotsDP(map, records.substring(1), newNums)
            } else 0
        } else if (records.first() == '.') {
//            map[Pair(records, nums)] = if (nums.first() == 0) {
//                trySpotsDP(map, records.trim('.'), nums.dropWhile{ it == 0})
//            } else 0
            return if (nums.first() == 0) {
                trySpotsDP(map, records.trim('.'), nums.dropWhile { it == 0 })
            } else 0
        } else if (records.first() == '?'){
            val s = StringBuilder(records)
            s.setCharAt(0, '#')
            val a = trySpotsDP(map, s.toString(), nums)
            s.setCharAt(0, '.')
            val b = trySpotsDP(map, s.toString().trim('.'), nums.dropWhile { it == 0 })
            map[Pair(records, nums)] = a + b
        }

        return map[Pair(records, nums)]!!
    }

    fun part1(input: List<String>): Long {
        val map = mutableMapOf<Pair<String, List<Int>>, Long>()
        return input.sumOf { line ->
            val (records, nums) = processLine(line)
//            val a = trySpots(records, nums, 0)
            val a = trySpotsDP(map, records.trim('.'), nums)
            a
        }
    }

    fun part2(input: List<String>): Long {
        val map = mutableMapOf<Pair<String, List<Int>>, Long>()
        return input.sumOf { line ->
            val (records, nums) = processLine(line)
            val a = trySpotsDP(map, records.trim('.'), nums)
            a
        }
    }

    println(part1(listOf("??????? 2,1")))

//    val testInput = readInput("Day12_test")
//    check(part1(testInput) == 21L)
//    check(part2(testInput) == 525152L)

//    val input = readInput("Day12")
//    part1(input).println()
//    part2(input).println()
}