package dev.funky.avocrowdo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Settings() {
    var tracePaths by remember { Tesseract.tracePaths }
    var colourCode by remember { Tesseract.colourCode }
    var showDirection by remember { Tesseract.showDirection }

    Column(
        Modifier
            .width(300.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OptionToggle("Trace Paths", tracePaths) { tracePaths = it }

        OptionToggle("Colour Code by Density", colourCode) { colourCode = it }

        OptionToggle("Show as Arrows", showDirection) { showDirection = it }
    }
}

@Composable
fun OptionToggle(
    title: String,
    value: Boolean,
    callback: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title)
        Switch(
            checked = value,
            onCheckedChange = callback
        )
    }
}
