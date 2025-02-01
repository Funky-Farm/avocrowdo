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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.funky.avocrowdo.Tesseract.currentTime
import dev.funky.avocrowdo.Tesseract.totalTime
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timer

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Canvas() {
    var sliderEnabled by Tesseract.sliderEnabled
    val sliderPosition = remember { Tesseract.sliderState }
    val sliderInteraction = remember { MutableInteractionSource() }

    val timeStep by Tesseract.timeStep
    var playEnabled by Tesseract.playEnabled

    val radius by derivedStateOf { (currentTime / totalTime) * 100f }
    val scope = rememberCoroutineScope()
    var timer by remember { mutableStateOf<Timer?>(null) }

    LaunchedEffect(playEnabled) {
        if (playEnabled) {
            timer = timer("Play", period = (1000f / Tesseract.TARGET_FPS).toLong()) {
                scope.launch {
                    if (playEnabled) {
                        sliderPosition.value += timeStep
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

    Box(Modifier.padding(16.dp)) {
        androidx.compose.foundation.Canvas(Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.Red,
                radius = radius,
                center = center,
                style = Stroke(width = 5f)
            )
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