import java.util.Stack

fun main() {
    data class Part(val data: HashMap<Char, Int>)
    data class Rule(val pred: (Part) -> Boolean, val next: String, val c: Char, val op: Char, val num: Int)
    data class Workflow(val rules: List<Rule>)

    fun processInput(input: List<String>): Pair<Map<String, Workflow>, List<Part>> {
        val workflows = input.takeWhile(String::isNotBlank).map { line ->
            val name = line.substringBefore('{')
            val rules = line.substringAfter('{').trim('{', '}').split(',').map {
                if (it.contains(':')) {
                    val c = it[0]
                    val op = it[1]
                    val num = it.substring(2).substringBefore(':').toInt()
                    val next = it.substringAfter(':')

                    Rule({ p: Part ->
                        when (op) {
                            '<' -> p.data[c]!! < num

                            '>' -> p.data[c]!! > num

                            else -> throw IllegalArgumentException("invalid operation")
                        }
                    }, next, c, op, num)
                } else {
                    Rule(
                        { _: Part -> true }, it, 'w', '=', 0
                    )
                }
            }

            name to Workflow(rules)
        }.toMap()

        val parts = input.dropWhile(String::isNotBlank).drop(1).map { line ->
            val data = HashMap<Char, Int>()
            for (datum in line.trim('{', '}').split(',')) {
                val parts = datum.split('=')
                data[parts[0].first()] = parts[1].toInt()
            }
            Part(data)
        }.toList()

        return workflows to parts
    }

    fun part1(input: List<String>): Int {
        val (workflows, parts) = processInput(input)

        var ans = 0
        for (part in parts) {
            var workflow = "in"
            inner@ while (workflow != "R" && workflow != "A") {
                val rules = workflows[workflow]!!.rules
                for (rule in rules) {
                    if (rule.pred(part)) {
                        workflow = rule.next
                        continue@inner
                    }
                }
            }

            if (workflow == "A") {
                val partInfo = part.data.map { it.value }.sum()
                ans += partInfo
            }
        }

        return ans
    }

    fun part2(input: List<String>): Long {
        val (workflows, _) = processInput(input)

        val accepted = mutableListOf<Map<Char, Pair<Int, Int>>>()
        val stack = Stack<Pair<Map<Char, Pair<Int, Int>>, Pair<String, Int>>>()
        stack.push(
            mapOf(
                'x' to (1 to 4000), 'm' to (1 to 4000), 'a' to (1 to 4000), 's' to (1 to 4000)
            ) to ("in" to 0)
        )

        while (!stack.empty()) {
            val (map, inst) = stack.pop()
            val (name, step) = inst

            if (name == "A") {
                accepted.add(map)
                continue
            }

            if (name != "R") {
                val rules = workflows[name]!!.rules
                if (step < rules.size) {
                    val rule = rules[step]

                    // pass this rule
                    val passedMap = map.toMutableMap()
                    if (rule.op == '<') {
                        val prev = passedMap[rule.c]!!
                        passedMap[rule.c] = prev.first to rule.num - 1
                    } else if (rule.op == '>') {
                        val prev = passedMap[rule.c]!!
                        passedMap[rule.c] = rule.num + 1 to prev.second
                    }
                    stack.push(passedMap to (rule.next to 0))

                    // fail this rule
                    val failedMap = map.toMutableMap()
                    if (rule.op == '<') {
                        val prev = failedMap[rule.c]!!
                        failedMap[rule.c] = rule.num to prev.second
                        stack.push(failedMap to (name to step + 1))
                    } else if (rule.op == '>') {
                        val prev = failedMap[rule.c]!!
                        failedMap[rule.c] = prev.first to rule.num
                        stack.push(failedMap to (name to step + 1))
                    }
                }
            }
        }

        return accepted.sumOf { res ->
            val x = (res['x']!!.second - res['x']!!.first + 1).toLong()
            val m = (res['m']!!.second - res['m']!!.first + 1).toLong()
            val a = (res['a']!!.second - res['a']!!.first + 1).toLong()
            val s = (res['s']!!.second - res['s']!!.first + 1).toLong()
            x * m * a * s
        }
    }

    val testInput = readInput("Day19_test")
    check(part1(testInput) == 19114)
    check(part2(testInput) == 167409079868000L)

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}
