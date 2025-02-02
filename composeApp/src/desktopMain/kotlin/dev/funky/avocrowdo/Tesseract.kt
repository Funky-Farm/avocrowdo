package dev.funky.avocrowdo

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SliderState
import androidx.compose.runtime.mutableStateOf
import models.GameState

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