package models

import kotlin.math.pow
import kotlin.random.Random

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
        TODO()
    }
}

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
    }
}

data class SimpleAgent(
    override val size: Int,
    override var position: Point,
    override var velocity: Point,
    val maxVelocity: Double,
    val targetPosition: Point
) : Agent {
    override fun update(state: GameState) {
        val distance: Point = targetPosition - position
        velocity = (distance / distance.magnitude()) * maxVelocity
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

class AgentGenerator {
    val interval: Double;
    val agent: Agent;

    fun update() {
        if (globalTime % interval == 0) {
            gameState.agents.add(agent);
        }
    }
}

class AgentConsumer {
    val interval: Double;
    val position: Point;

    fun update() {
        for (agent in gameState.agents) {
            if (agent.position == this.position) {
                agents.remove(agent);
            }
        }
    }
}
