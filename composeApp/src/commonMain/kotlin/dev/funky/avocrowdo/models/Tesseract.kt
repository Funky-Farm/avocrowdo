package dev.funky.avocrowdo.models

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SliderState
import androidx.compose.runtime.mutableStateOf
import models.Agent
import models.GameState
import models.Point
import models.Polygon
import models.SimpleAgent
import kotlin.random.Random

val exampleObjects: List<Polygon> = List(12) {
    Polygon(
        listOf(
            Point(it * 200.0, 200.0),
            Point(it * 200.0 + 150, 200.0),
            Point(it * 200.0 + 150, 300.0),
            Point(it * 200.0, 300.0)
        ),
        false
    )
}.plus(List(36) {
    Polygon(
        listOf(
            Point((it % 4) * 200.0 + 50, (it / 4) * 200.0 + 350),
            Point((it % 4) * 200.0 + 150, (it / 4) * 200.0 + 350),
            Point((it % 4) * 200.0 + 150, (it / 4) * 200.0 + 400),
            Point((it % 4) * 200.0 + 50, (it / 4) * 200.0 + 400)
        ),
        true
    )
}).plusElement(
    Polygon(
        listOf(
            Point(1200.0, 800.0),
            Point(1250.0, 800.0),
            Point(1250.0, 500.0),
            Point(1350.0, 500.0),
            Point(1350.0, 400.0),
            Point(1200.0, 400.0),
        ),
        false
    )
)

val random = Random(System.currentTimeMillis())
val center = Point(800.0, 800.0)
val radius = 400.0

val exampleAgents: List<Agent> = List(25) {
    var x: Double
    var y: Double
    do {
        x = random.nextDouble(0.0, 900.0)
        y = random.nextDouble(0.0, 1200.0)
    } while (exampleObjects.any { it.contains(Point(x, y), 10) })

    val targetX = 1200.0
    val targetY = random.nextDouble(450.0, 750.0)
    SimpleAgent(
        position = Point(x, y),
        velocity = Point(0.0, 0.0),
        size = 10,
        maxVelocity = 5.0,
        target = Point(targetX, targetY)
    )
}

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