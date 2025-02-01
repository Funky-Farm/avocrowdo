package models

import kotlinx.serialization.Serializable

/**
 * A shape that can be drawn on a canvas. There also exist [DynamicObject]s, which are a sub-sealed class of [Object].
 */
@Serializable
sealed interface Object

/**
 * A circle defined by a center point and a radius.
 *
 * @param center The center of the circle. Precondition, this is within the bounds of the canvas.
 * @param radius The radius of the circle. Precondition, the radius does not exceed the bounds of the canvas, with
 *               the center as the origin.
 */
@Serializable
data class Circle(val center: Point, val radius: UInt, val hollow: Boolean) : Object

/**
 * A polygon defined by a list of points. The last point will be assumed to connect the first, if they are not
 * already the same.
 *
 * @param points The points that define the polygon. Precondition, these are within the bounds of the canvas.
 * @param hollow Whether the polygon should be hollow or filled.
 */
@Serializable
data class Polygon(val points: List<Point>, val hollow: Boolean) : Object

/**
 * A line defined by a list of points. The line will be drawn between each point in order. Spiritually similar to
 * [Polygon], except the loop is not closed and hence does not necessarily form a closed shape.
 */
@Serializable
data class Lines(val points: List<Point>) : Object
