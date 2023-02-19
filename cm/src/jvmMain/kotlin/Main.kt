import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import hw.App

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
