package dev.funky.avocrowdo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.Dimension

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Avocrowdo",
    ) {
        window.minimumSize = Dimension(1080, 720)

        Box(Modifier.size(window.width.dp, window.height.dp)) {
            MaterialTheme {
                App()
            }
        }
    }
}
