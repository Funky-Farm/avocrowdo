package models

import androidx.compose.ui.geometry.Offset
import kotlinx.serialization.Serializable
import kotlin.math.pow

/**
 * An arbitrary point on the canvas, in the form `(x, y)`.
 */
@Serializable
data class Point(var x: Double, var y: Double) {
    fun magnitude(): Double = (this.x.pow(2) + this.y.pow(2)).pow(0.5)
    operator fun minus(other: Point): Point = Point(this.x - other.x, this.y - other.y)
    operator fun times(d: Double): Point = Point(this.x * d, this.y * d)
    operator fun div(d: Double): Point = Point(this.x / d, this.y / d)
    fun toOffset(): Offset = Offset(x.toFloat(), y.toFloat())
    operator fun plusAssign(velocity: Point) {
        this.x += velocity.x
        this.y += velocity.y
    }
}