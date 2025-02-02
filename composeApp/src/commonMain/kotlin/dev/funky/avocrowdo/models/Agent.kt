package models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import kotlin.math.pow
import kotlin.random.Random

/**
 * An entity that moves around in a space.
 */
interface Agent {
    /** The agent's size, which is used to determine its collision radius. */
    val size: Int

    /** The agent's current position. */
    var position: Point

    /** The agent's current velocity. */
    var velocity: Point

    /** Update the agent's position and velocity, given an observation of the game. */
    fun update(state: GameState)

    /** Execute the agent's movement, given a velocity. This function accounts for collisions. */
    fun execute(state: GameState, velocity: Point) {
        // Update the agent's position. If the agent "bumps into" anything, it should go as far as possible to just
        // about touch the object.
        val (xCollide, yCollide) = state.objects.map { obj ->
            val xCollide = obj.contains(Point(position.x + velocity.x, position.y), size)
            val yCollide = obj.contains(Point(position.x, position.y + velocity.y), size)
            Pair(xCollide, yCollide)
        }.reduce { acc, pair -> Pair(acc.first || pair.first, acc.second || pair.second) }.let {
            if (it.first || it.second) return@let Pair(it.first, it.second)

            return@let state.agents.map { agent ->
                val xCollide = agent.position != position
                    && agent.collides(Point(position.x + velocity.x, position.y), size)
                val yCollide = agent.position != position
                    && agent.collides(Point(position.x, position.y + velocity.y), size)
                Pair(xCollide, yCollide)
            }.reduce { acc, pair -> Pair(acc.first || pair.first, acc.second || pair.second) }
        }

        if (!xCollide) position.x += velocity.x
        if (!yCollide) position.y += velocity.y
    }

    /** Draw the agent in a particular scope. */
    fun draw(scope: DrawScope) {
        scope.drawCircle(
            color = Color.Black,
            style = Fill,
            radius = size.toFloat(),
            center = position.toOffset()
        )
    }

    /** Check if this agent collides with some other agent at [point] with radius [radius]. */
    fun collides(point: Point, radius: Int): Boolean {
        return (point - position).magnitude() < size + radius
    }
}

/**
 * An agent which behaves completely randomly.
 */
data class RandomAgent(
    override val size: Int,
    override var position: Point,
    override var velocity: Point,
    val maxVelocity: Double,
) : Agent {
    override fun update(state: GameState) {
        val xSpeed = Random.nextDouble(0.0, maxVelocity)
        val ySpeed = (maxVelocity.pow(2) - xSpeed.pow(2)).pow(0.5)
        velocity = Point(xSpeed, ySpeed)
        this.execute(state, velocity)
    }
}

data class SimpleAgent(
    override val size: Int,
    override var position: Point,
    override var velocity: Point,
    val maxVelocity: Double,
    val targetPosition: Point,
) : Agent {
    override fun update(state: GameState) {
        val distance: Point = targetPosition - position
        velocity = (distance / distance.magnitude()) * maxVelocity
        this.execute(state, velocity)
    }
}

data class PathFindingAgent(
    override val size: Int,
    override var position: Point,
    override var velocity: Point,
    var target: Point,
) : Agent {
    override fun update(state: GameState) {
        TODO("Not yet implemented")
    }
}
