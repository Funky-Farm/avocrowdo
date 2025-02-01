package dev.funky.avocrowdo

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SliderState
import androidx.compose.runtime.mutableStateOf

@OptIn(ExperimentalMaterial3Api::class)
object Tesseract {
    var tracePaths = mutableStateOf(false)
    var colourCode = mutableStateOf(false)
    var showDirection = mutableStateOf(false)

    var sliderEnabled = mutableStateOf(true)
    var sliderState = SliderState(0f, valueRange = 0f..100f)
}