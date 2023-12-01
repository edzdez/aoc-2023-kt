fun main() {
    fun isNumberPart1(input: CharSequence): Int? {
        if (input.isEmpty())
            assert(false)

        return input.first().digitToIntOrNull()
    }

    fun isNumberPart2(input: CharSequence): Int? {
        if (input.isEmpty())
            assert(false)

        val digit = isNumberPart1(input);
        if (digit != null)
            return digit;

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

    fun run(input: List<String>, isNumber: (CharSequence) -> Int?): Int {
        var res = 0
        for (line in input) {
            var num = 0

            var leftSeen = false
            var rightSeen = false

            var leftIdx = 0
            var rightIdx = line.length - 1

            while (!leftSeen || !rightSeen) {
                if (!leftSeen) {
                    val digit = isNumber(line.subSequence(leftIdx, line.length))
                    if (digit != null) {
                        leftSeen = true
                        num += 10 * digit
                    }
                }

                if (!rightSeen) {
                    val digit = isNumber(line.subSequence(rightIdx, line.length))
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

    fun part1(input: List<String>): Int = run(input, ::isNumberPart1)
    fun part2(input: List<String>): Int = run(input, ::isNumberPart2)

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day01_test1")
    check(part1(testInput1) == 142)
    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
