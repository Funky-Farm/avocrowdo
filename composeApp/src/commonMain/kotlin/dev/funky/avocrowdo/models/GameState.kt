package models

import kotlinx.serialization.Serializable

@Serializable
data class GameState(
    val objects: List<Object>,
    val agents: List<Agent>,
) {
    fun update() {
        agents.forEach { it.update(this) }
    }
}
