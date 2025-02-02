package models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.serialization.Serializable

/**
 * A shape that can be drawn on a canvas.
 */
@Serializable
sealed interface Object {
    fun draw(scope: DrawScope)

    fun contains(point: Point, radius: Int): Boolean
}

/**
 * A circle defined by a center point and a radius.
 *
 * @param center The center of the circle. Precondition, this is within the bounds of the canvas.
 * @param radius The radius of the circle. Precondition, the radius does not exceed the bounds of the canvas, with
 *               the center as the origin.
 */
@Serializable
data class Circle(val center: Point, val radius: UInt, val hollow: Boolean) : Object {
    override fun draw(scope: DrawScope) {
        scope.drawCircle(
            color = if (hollow) Color.Transparent else Color.Black,
            radius = radius.toFloat(),
            center = this@Circle.center.toOffset()
        )
    }

    override fun contains(point: Point, radius: Int): Boolean {
        return (point - center).magnitude() <= this.radius.toInt() + radius
    }
}

/**
 * A polygon defined by a list of points. The last point will be assumed to connect the first, if they are not
 * already the same.
 *
 * @param points The points that define the polygon. Precondition, these are within the bounds of the canvas.
 * @param hollow Whether the polygon should be hollow or filled.
 */
@Serializable
data class Polygon(val points: List<Point>, val hollow: Boolean) : Object {
    override fun draw(scope: DrawScope) {
        val path = Path()
        val first = points.first()

        path.moveTo(first.x.toFloat(), first.y.toFloat())
        points
            .subList(1, points.size)
            .forEach { path.lineTo(it.x.toFloat(), it.y.toFloat()) }
        path.lineTo(first.x.toFloat(), first.y.toFloat())

        scope.drawPath(
            path = path,
            color = Color.Black,
            style = if (hollow) Stroke() else Fill
        )
    }

    override fun contains(point: Point, radius: Int): Boolean {
        var inside = false
        for (i in points.indices) {
            val j = (i + 1) % points.size
            if ((points[i].y > point.y + radius) != (points[j].y > point.y + radius) &&
                point.x + radius < (points[j].x - points[i].x) * (point.y + radius - points[i].y) / (points[j].y - points[i].y) + points[i].x
            ) {
                inside = !inside
            }
        }
        return inside
    }
}
