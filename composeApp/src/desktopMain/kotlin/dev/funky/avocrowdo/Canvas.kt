package dev.funky.avocrowdo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.onClick
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.funky.avocrowdo.Tesseract.currentTime
import dev.funky.avocrowdo.Tesseract.totalTime
import kotlinx.coroutines.launch
import models.Agent
import models.GameState
import models.Point
import models.Polygon
import models.RandomAgent
import java.util.*
import kotlin.concurrent.timer

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
    RandomAgent(
        position = Point(90.0, 90.0),
        velocity = Point(1.0, 0.0),
        size = 10,
        maxVelocity = 1.0,
    ),
    RandomAgent(
        position = Point(60.0, 90.0),
        velocity = Point(-1.0, 0.0),
        size = 10,
        maxVelocity = 1.0,
    ),
    RandomAgent(
        position = Point(90.0, 60.0),
        velocity = Point(0.0, -1.0),
        size = 10,
        maxVelocity = 1.0,
    ),
    RandomAgent(
        position = Point(350.0, 130.0),
        velocity = Point(0.0, 1.0),
        size = 10,
        maxVelocity = 1.0,
    ),
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Canvas() {
    val sliderEnabled by Tesseract.sliderEnabled
    val sliderPosition = remember { Tesseract.sliderState }
    val sliderInteraction = remember { MutableInteractionSource() }

    val timeStep by Tesseract.timeStep
    var playEnabled by Tesseract.playEnabled

    val radius by derivedStateOf { (currentTime / totalTime) * 100f }
    val scope = rememberCoroutineScope()
    var timer by remember { mutableStateOf<Timer?>(null) }

    val gameState = remember { Tesseract.gameState }

    LaunchedEffect(playEnabled) {
        if (playEnabled) {
            timer = timer("Play", period = (1000f / Tesseract.TARGET_FPS).toLong()) {
                scope.launch {
                    if (playEnabled) {
                        sliderPosition.value += timeStep
                        gameState.update()
                        if (sliderPosition.value >= totalTime) playEnabled = false
                    }
                }
            }

            // Pressing play when the slider is at the end should reset the slider.
            if (currentTime >= totalTime) {
                currentTime = 0f
            }
        } else {
            timer?.cancel()
            timer = null
        }
    }

    // Debug, populate gamestate
    LaunchedEffect(Unit) {
        Tesseract.gameState = GameState(exampleObjects, exampleAgents, 1000.0, 1000.0)
    }

    Box(Modifier.padding(16.dp)) {
        androidx.compose.foundation.Canvas(Modifier.fillMaxSize()) {
            for (obj in gameState.objects) {
                obj.draw(this)
            }

            for (agent in gameState.agents) {
                agent.draw(this)
            }
        }

        Row(
            Modifier.align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if (playEnabled) Icons.Filled.Pause else Icons.Default.PlayArrow,
                contentDescription = "Play",
                modifier = Modifier
                    .onClick { playEnabled = !playEnabled }
                    .size(24.dp)
            )

            Slider(
                state = sliderPosition,
                interactionSource = sliderInteraction,
                enabled = sliderEnabled,
                thumb = {
                    SliderDefaults.Thumb(
                        interactionSource = sliderInteraction,
                        colors = SliderDefaults.colors(),
                        enabled = sliderEnabled,
                        thumbSize = DpSize(4.dp, 24.dp),
                        modifier = Modifier.border(2.dp, MaterialTheme.colorScheme.primary),
                    )
                },
            )
        }

        Column(
            Modifier
                .align(Alignment.TopEnd)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text("${currentTime / 1000} / ${totalTime / 1000} seconds")
        }
    }
}