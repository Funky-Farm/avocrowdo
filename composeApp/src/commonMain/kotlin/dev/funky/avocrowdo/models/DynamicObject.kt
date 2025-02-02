package models

import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlinx.serialization.Serializable

@Serializable
sealed class DynamicObject : Object {
    data object AgentGenerator : DynamicObject() {
        override fun draw(scope: DrawScope) {
            println("Drawing agent generator")
            TODO("Not yet implemented")
        }

        override fun contains(point: Point, radius: Int): Boolean {
            TODO("Not yet implemented")
        }
    }

    data object AgentConsumer : DynamicObject() {
        override fun draw(scope: DrawScope) {
            println("Drawing agent consumer")
            TODO("Not yet implemented")
        }

        override fun contains(point: Point, radius: Int): Boolean {
            TODO("Not yet implemented")
        }
    }
}
