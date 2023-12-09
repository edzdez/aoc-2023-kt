fun main() {
    data class Network(val instructions: String, val delta: Map<String, Pair<String, String>>)

    val wordRegex = Regex("\\w+")

    fun parseInput(input: List<String>): Network {
        val delta = input.drop(2).associate { line ->
            val matches = wordRegex.findAll(line).map { it.value }.toList()
            matches[0] to Pair(matches[1], matches[2])
        }

        return Network(input[0], delta)
    }

    fun run(state: String, step: Char, delta: Map<String, Pair<String, String>>): String {
        val (l, r) = delta[state]!!
        return if (step == 'L') l else r
    }

    fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

    fun lcm(a: Long, b: Long): Long = (a * b) / gcd(a, b)

    fun part1(input: List<String>): Int {
        val (instructions, delta) = parseInput(input)
        var state = "AAA"
        var steps = 0
        do {
            state = run(state, instructions[steps % instructions.length], delta)
            steps += 1
        } while (state != "ZZZ")

        return steps
    }

    fun part2(input: List<String>): Long {
        val (instructions, delta) = parseInput(input)
        val states = delta.keys.filter { it.endsWith('A') }.toHashSet()

        return states.map {
            var state = it
            var steps = 0
            do {
                state = run(state, instructions[steps % instructions.length], delta)
                steps += 1
            } while (!state.endsWith('Z'))
            steps.toLong()
        }.reduce(::lcm)
    }

    val testInput1 = readInput("Day08_test1")
    check(part1(testInput1) == 2)

    val testInput2 = readInput("Day08_test2")
    check(part1(testInput2) == 6)

    val testInput3 = readInput("Day08_test3")
    check(part2(testInput3) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}