package dev.funky.avocrowdo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    var tracePaths by mutableStateOf(false)
    var colourCode by mutableStateOf(false)
    var showDirection by mutableStateOf(false)

    MaterialTheme {
        Row(
            Modifier
                .fillMaxSize()
                .border(1.dp, MaterialTheme.colors.primary)
        ) {
            Column(
                Modifier
                    .width(200.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colors.primary)
                    .border(1.dp, MaterialTheme.colors.primary),
            ) {
                OptionToggle("Trace Paths", tracePaths) { tracePaths = it }

                OptionToggle("Colour Code by Density", colourCode) { colourCode = it }

                OptionToggle("Show as Arrows", colourCode) { colourCode = it }
                
            }

            Box(
                Modifier
                    .fillMaxWidth()
            ) {
                Canvas(Modifier.border(1.dp, MaterialTheme.colors.primary)) {
                    drawCircle(
                        color = Color.Red,
                        radius = 100f,
                        center = center,
                        style = Stroke(width = 5f)
                    )
                }
            }

        }
    }
}

@Composable
fun OptionToggle(
    title: String,
    value: Boolean,
    callback: (Boolean) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        Text(title)
        Switch(
            checked = value,
            onCheckedChange = callback
        )
    }
}
