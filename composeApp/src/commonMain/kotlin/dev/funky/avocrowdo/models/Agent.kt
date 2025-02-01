package models

import kotlin.math.pow
import kotlin.random.Random
import kotlin.random.Random.Default

/**
 * An entity that moves around in a space.
 */
interface Agent {
    /** An agent's size, which is used to determine its collision radius. */
    val size: Int
    var position: Point
    var velocity: Point

    fun update(state: GameState)

    fun step() {
        ()
    }
}

data class RandomAgent(
    override val size: Int,
    override var position: Point,
    override var velocity: Point,
    val maxVelocity: Double
) : Agent {
    override fun update(state: GameState) {
        val xSpeed = Random.nextDouble(0.0, maxVelocity)
        val ySpeed = (maxVelocity.pow(2) - xSpeed.pow(2)).pow(0.5)
        velocity = Point(xSpeed, ySpeed)
    }
}
