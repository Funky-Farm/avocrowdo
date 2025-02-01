package dev.funky.avocrowdo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Canvas() {
    var sliderEnabled by Tesseract.sliderEnabled
    val sliderPosition = remember { Tesseract.sliderState }
    val sliderInteraction = remember { MutableInteractionSource() }

    Box(Modifier.padding(16.dp)) {
        androidx.compose.foundation.Canvas(Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.Red,
                radius = 100f,
                center = center,
                style = Stroke(width = 5f)
            )
        }

        Slider(
            modifier = Modifier.align(Alignment.BottomCenter),
            state = sliderPosition,
            interactionSource = sliderInteraction,
            enabled = sliderEnabled,
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = sliderInteraction,
                    colors = SliderDefaults.colors(),
                    enabled = sliderEnabled,
                    thumbSize = DpSize(4.dp, 24.dp),
                    modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.primary),
                )
            },
        )

        Column(
            Modifier
                .align(Alignment.TopEnd)
                .background(MaterialTheme.colorScheme.background)
        ) {
            val currentTime by derivedStateOf { sliderPosition.value.roundToInt() }
            val totalTime by derivedStateOf { sliderPosition.valueRange.endInclusive.roundToInt() }

            Text(
                "$currentTime / $totalTime seconds"
            )
        }
    }
}