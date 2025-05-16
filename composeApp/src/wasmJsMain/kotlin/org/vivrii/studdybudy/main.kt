package org.vivrii.studdybudy

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import org.vivrii.studdybudy.music.MusicViewModel
import org.vivrii.studdybudy.music.WasmJsMusicPlayerController

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val viewModel = MusicViewModel(controller = WasmJsMusicPlayerController())

    ComposeViewport(document.body!!) {
        App(viewModel = viewModel)
    }
}
