import kotlin.math.abs
import kotlin.math.min

fun main() {
    data class Step(val direction: Char, val steps: Int, val color: String)

    fun processInput(input: List<String>): List<Step> = input.map {
        val split = it.split(' ')
        Step(split[0][0], split[1].toInt(), split[2].trim('(', ')'))
    }.toList()

    fun fix(input: List<Step>): List<Step> = input
        .map {
            val color = it.color
            val dir = when (color.last()) {
                '0' -> 'R'
                '1' -> 'D'
                '2' -> 'L'
                '3' -> 'U'
                else -> throw IllegalArgumentException("oops")
            }
            val mag = color.substring(1, 6).toInt(16)
            Step(dir, mag, color)
        }

    fun findVertices(steps: List<Step>): List<Pair<Int, Int>> {
        val l = mutableListOf<Pair<Int, Int>>()

        var (minX, minY) = 0 to 0

        var (x, y) = 0 to 0
        for (step in steps) {
            val (dx, dy) = when (step.direction) {
                'U' -> 0 to 1
                'D' -> 0 to -1
                'L' -> -1 to 0
                'R' -> 1 to 0
                else -> throw IllegalArgumentException("oops")
            }

            x += dx * step.steps
            y += dy * step.steps

            minX = min(minX, x)
            minY = min(minY, y)

            l.add(x to y)
        }

        for (i in l.indices) {
            val (oldX, oldY) = l[i]
            l[i] = oldX + abs(minX) to oldY + abs(minY)
        }

        return l
    }

    fun integrate(vs: List<Pair<Int, Int>>): Long {
        var ans = 0L
        // \int_C \langle 0, x \rangle \cdot d\vec{r}
        // = \int_{y_1}^{y_2} x \cdot y
        // = x \cdot (y_2 - y_1)
        // if x1 and x2 are different, then y1 and y2 are the same so we would be integrating 0
        for (i in vs.indices) {
            val (x, y1) = vs[i]
            val (_, y2) = vs[(i + 1) % vs.size]
            ans += x.toLong() * (y2 - y1)
        }

        return abs(ans)
    }

    // assuming clockwise orientation :)
    fun computeSpace(steps: List<Step>): Long {
        var space = 0.0
        var perim = 0
        for (i in steps.indices) {
            val (dir1, mag1, _) = steps[i]
            val (dir2, _, _) = steps[(i + 1) % steps.size]
            space += (mag1 - 1).toDouble() * 0.5

            perim += mag1
            space += when (dir1) {
                'U' -> if (dir2 == 'L') 0.75 else 0.25
                'D' -> if (dir2 == 'R') 0.75 else 0.25
                'L' -> if (dir2 == 'D') 0.75 else 0.25
                'R' -> if (dir2 == 'U') 0.75 else 0.25
                else -> throw IllegalArgumentException("oops")
            }
        }

        return (space + 2).toLong()
    }

    fun part1(input: List<String>): Int {
        val steps = processInput(input)
        val vertices = findVertices(steps)
        return integrate(vertices).toInt() + computeSpace(steps).toInt()
    }

    fun part2(input: List<String>): Long {
        val steps = fix(processInput(input))
        val vertices = findVertices(steps)
        return  integrate(vertices) + computeSpace(steps)
    }

    val testInput = readInput("Day18_test")
    check(part1(testInput) == 62)
    check(part2(testInput) == 952408144115L)

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}
