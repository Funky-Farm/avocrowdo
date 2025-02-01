package dev.funky.avocrowdo

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    Row(
        Modifier
            .fillMaxSize()
            .border(1.dp, MaterialTheme.colorScheme.background)
    ) {
        Settings()
        Canvas()
    }
}

