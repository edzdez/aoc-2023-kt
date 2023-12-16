fun main() {
    fun hash(step: String): Int = step.fold(0) { acc, c ->
        ((acc + c.code) * 17) % 256
    }

    fun part1(input: List<String>): Int = input[0].split(',').sumOf(::hash)

    fun part2(input: List<String>): Int {
        val steps = input[0].split(',')

        val boxes = List<MutableList<Pair<String, Int>>>(256) { mutableListOf() }
        for (step in steps) {
            if (step.endsWith('-')) {
                val label = step.substringBefore('-')
                val box = hash(label)
                boxes[box].removeIf { it.first == label }
            } else {
                val label = step.substringBefore('=')
                val box = hash(label)
                val idx = boxes[box].indexOfFirst { it.first == label }
                if (idx == -1) {
                    boxes[box].add(label to step.last().digitToInt())
                } else {
                    boxes[box][idx] = label to step.last().digitToInt()
                }
            }
        }

        return boxes.mapIndexed { boxNum, box ->
            box.mapIndexed { spotNum, pair ->
                (boxNum + 1) * (spotNum + 1) * pair.second
            }.sum()
        }.sum()
    }

    val testInput = readInput("Day15_test")
    check(part1(testInput) == 1320)
    check(part2(testInput) == 145)

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}