fun main() {
    fun isNumber(input: CharSequence, part2: Boolean): Int? {
        if (input.isEmpty())
            assert(false)

        val digit = input.first().digitToIntOrNull()
        if (digit != null)
            return digit

        if (!part2)
            return null

        if (input.startsWith("one")) return 1
        if (input.startsWith("two")) return 2
        if (input.startsWith("three")) return 3
        if (input.startsWith("four")) return 4
        if (input.startsWith("five")) return 5
        if (input.startsWith("six")) return 6
        if (input.startsWith("seven")) return 7
        if (input.startsWith("eight")) return 8
        if (input.startsWith("nine")) return 9

        return null
    }

    fun run(input: List<String>, part2: Boolean): Int {
        var res = 0
        for (line in input) {
            var num = 0

            var leftSeen = false
            var rightSeen = false

            var leftIdx = 0
            var rightIdx = line.length - 1

            while (!leftSeen || !rightSeen) {
                if (!leftSeen) {
                    val digit = isNumber(line.subSequence(leftIdx, line.length), part2)
                    if (digit != null) {
                        leftSeen = true
                        num += 10 * digit
                    }
                }

                if (!rightSeen) {
                    val digit = isNumber(line.subSequence(rightIdx, line.length), part2)
                    if (digit != null) {
                        rightSeen = true
                        num += digit
                    }
                }

                ++leftIdx
                --rightIdx
            }

            res += num
        }

        return res
    }

    fun part1(input: List<String>): Int = run(input, false)
    fun part2(input: List<String>): Int  = run(input, true)

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day01_test1")
    check(part1(testInput1) == 142)
    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
