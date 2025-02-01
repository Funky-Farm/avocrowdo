package models

import kotlin.math.pow

/**
 * An arbitrary point on the canvas, in the form `(x, y)`.
 */
data class Point(val x: Double, val y: Double) {
    fun magnitude(): Double = (this.x.pow(2) + this.y.pow(2)).pow(0.5)
    operator fun minus(other: Point): Point = Point(this.x - other.x, this.y - other.y)
    operator fun times(d: Double): Point = Point(this.x * d, this.y * d)
    operator fun div(d: Double): Point = Point(this.x / d, this.y / d)
}