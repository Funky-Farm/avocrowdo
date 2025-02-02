package models

import androidx.compose.ui.geometry.Offset
import org.jetbrains.kotlinx.multik.api.linalg.solve
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.data.D1
import org.jetbrains.kotlinx.multik.ndarray.data.D2
import org.jetbrains.kotlinx.multik.ndarray.data.NDArray
import org.jetbrains.kotlinx.multik.ndarray.data.get
import kotlin.math.pow

/**
 * An arbitrary point on the canvas, in the form `(x, y)`.
 */
data class Point(var x: Double, var y: Double) {
    fun magnitude(): Double = (this.x.pow(2) + this.y.pow(2)).pow(0.5)

    operator fun minus(other: Point): Point = Point(this.x - other.x, this.y - other.y)

    operator fun times(d: Double): Point = Point(this.x * d, this.y * d)

    operator fun div(d: Double): Point = Point(this.x / d, this.y / d)

    operator fun compareTo(other: Point): Int = PointComparator().compare(this, other)

    operator fun plusAssign(velocity: Point) {
        this.x += velocity.x
        this.y += velocity.y
    }

    fun toOffset(): Offset = Offset(x.toFloat(), y.toFloat())
}

class PointComparator : Comparator<Point> {
    override fun compare(p0: Point, p1: Point): Int {
        // lancelot wrote this shit like i aint got nuthin to do wit this
        return when ((p0.x < p1.x) to (p0.y < p1.y)) {
            true to true -> -1
            false to false -> 1
            else -> 0
        }
    }
}

/**
 * Checks if two lines intersect.
 *
 * @param line1 The first line, described by two points.
 * @param line2 The second line, described by two points.
 */
fun linesIntersect(p1: Point, p2: Point, p3: Point, p4: Point): Boolean {

    // Set up the line intersection equations.
    val a: NDArray<Double, D2> = mk.ndarray(
        mk[
            mk[p2.x - p1.x, p1.y - p2.y],
            mk[p4.x - p3.x, p3.y - p4.y]
        ]
    )
    val b: NDArray<Double, D1> = mk.ndarray(
        mk[p1.x * p2.y - p2.x * p1.y,
            p3.x * p4.y - p4.x * p3.y]
    )

    // Calculate determinant.
    val det = a[0][0] * a[1][1] - a[0][1] * a[1][0]
    if (det == 0.0) {
        return true
    }

    // Try to find a solution.
    return try {
        val x: NDArray<Double, D1> = mk.linalg.solve(a, b)
        val xPoint: Point = Point(x[0], x[1])
        // Check if the solution is within the bounds of the vertices.
        (p3 <= xPoint && xPoint <= p4) || (p4 <= xPoint && xPoint <= p3)
    } catch (_: Exception) {
        // No solution, so no intersection.
        false
    }
}

fun lineIntersectsPolygon(p1: Point, p2: Point, obstacle: Polygon): Boolean {
    val obstaclePairs = obstacle.points.zipWithNext()
    for ((op1, op2) in obstaclePairs) {
        if (linesIntersect(p1, p2, op1, op2)) {
            return true
        }
    }

    return linesIntersect(p1, p2, obstacle.points.last(), obstacle.points.first())
}

fun lineIntersectsObjects(p1: Point, p2: Point, objects: List<Polygon>): Boolean = objects.any { lineIntersectsPolygon(p1, p2, it) }
