package org.vivrii.studdybudy

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import javafx.application.Platform
import org.vivrii.studdybudy.music.DesktopMusicPlayerController
import org.vivrii.studdybudy.music.MusicViewModel

fun main() = application {
    Platform.startup { }

    val viewModel = MusicViewModel(controller = DesktopMusicPlayerController())

    Window(
        onCloseRequest = ::exitApplication,
        title = "studdybudy",
    ) {
        App(viewModel = viewModel)
    }
}
