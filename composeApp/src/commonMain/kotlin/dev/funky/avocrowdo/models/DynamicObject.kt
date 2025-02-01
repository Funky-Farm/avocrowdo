package models

import kotlinx.serialization.Serializable

@Serializable
sealed class DynamicObject : Object {
    data object AgentGenerator : DynamicObject()

    data object AgentConsumer : DynamicObject()
}
