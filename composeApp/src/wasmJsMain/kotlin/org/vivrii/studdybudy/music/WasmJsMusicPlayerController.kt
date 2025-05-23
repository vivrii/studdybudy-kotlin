package org.vivrii.studdybudy.music

import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLAudioElement

// todo: errors and logs

class WasmJsMusicPlayerController : MusicPlayerController {
    private lateinit var _onStateChanged: () -> Unit

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var player: HTMLAudioElement = (document.createElement("audio") as HTMLAudioElement)

    override fun play() {
        player.play()
    }

    override fun pause() {
        player.pause()
    }

    override fun seekTo(positionMs: Long) {
        player.currentTime = positionMs.div(1000.0)
    }

    override fun loadSong(songUri: String) {
        player.src = songUri
        player.load()
    }

    override fun repeatMode(repeat: Boolean) {
        player.loop = repeat
    }

    override fun observeState(onStateChanged: (MusicPlayerState) -> Unit) {
        _onStateChanged = {
            onStateChanged(
                MusicPlayerState(
                    isPlaying = !player.paused,
                    currentDurationMs = player.currentTime.times(1000).toLong(),
                    totalDurationMs = player.duration.times(1000).toLong()
                )
            )
        }

        scope.launch {
            while (isActive) {
                _onStateChanged()
                if (!player.paused) {
                    delay(100)
                } else {
                    delay(250)
                }
            }
        }
    }
}