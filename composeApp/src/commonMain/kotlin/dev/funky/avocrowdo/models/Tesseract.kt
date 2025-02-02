package dev.funky.avocrowdo.models

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SliderState
import androidx.compose.runtime.mutableStateOf
import models.Agent
import models.GameState
import models.PathFindingAgent
import models.Point
import models.Polygon
import models.SimpleAgent

val exampleObjects: List<Polygon> = listOf(
    Polygon(
        points = listOf(
            Point(100.0, 100.0),
            Point(200.0, 100.0),
            Point(200.0, 200.0),
            Point(100.0, 200.0),
        ),
        hollow = true
    ),
    Polygon(
        points = listOf(
            Point(400.0, 150.0),
            Point(500.0, 150.0),
            Point(500.0, 250.0),
            Point(400.0, 250.0),
        ),
        hollow = true
    ),
)

val exampleAgents: List<Agent> = listOf(
    SimpleAgent(
        position = Point(80.0, 120.0),
        velocity = Point(1.0, 0.0),
        size = 10,
        maxVelocity = 1.0,
        target = Point(1000.0, 500.0),
    ),
    SimpleAgent(
        position = Point(60.0, 90.0),
        velocity = Point(-1.0, 0.0),
        size = 10,
        maxVelocity = 1.0,
        target = Point(1000.0, 500.0),
    ),
    PathFindingAgent(
        position = Point(90.0, 60.0),
        velocity = Point(0.0, -1.0),
        size = 10,
        maxVelocity = 1.0,
        target = Point(1000.0, 500.0),
    ),
    PathFindingAgent(
        position = Point(350.0, 130.0),
        velocity = Point(0.0, 1.0),
        size = 10,
        maxVelocity = 1.0,
        target = Point(1000.0, 500.0),
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
object Tesseract {
    var tracePaths = mutableStateOf(false)
    var colourCode = mutableStateOf(false)
    var showDirection = mutableStateOf(false)

    /** Time step, in milliseconds. */
    var timeStep = mutableStateOf(100)

    /** Total time, in milliseconds.*/
    var totalTime
        get() = sliderState.valueRange.endInclusive.toInt()
        set(value) {
            sliderState = SliderState(sliderState.value, valueRange = 0f..value.toFloat())
        }

    /** Current time, in milliseconds. */
    var currentTime: Float
        get() = sliderState.value
        set(value) {
            sliderState.value = value
        }

    var playEnabled = mutableStateOf(false)
    const val TARGET_FPS: Float = 30f

    var sliderEnabled = mutableStateOf(true)
    var sliderState = SliderState(0f, valueRange = 0f..100 * 1000f)

    var gameState = GameState(exampleObjects, exampleAgents, 1000.0, 1000.0)
}