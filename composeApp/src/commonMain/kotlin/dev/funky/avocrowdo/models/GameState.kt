package models

import kotlinx.serialization.Serializable

@Serializable
data class GameState(
    val objects: List<Object>,
    val agents: List<Agent>,
) {
    /** Delegates all agents to be updated in this state. */
    fun update() {
        agents.forEach { it.update(this) }
    }
}
