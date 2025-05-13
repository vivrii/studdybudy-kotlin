package org.vivrii.studdybudy.music

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class AndroidMusicPlayerController : MusicPlayerController {
    private var musicPlayerState = MusicPlayerState(
        isPlaying = true,
        currentDurationMs = 4_000,
        totalDurationMs = 10_000,
    )

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun play() {
        musicPlayerState = musicPlayerState.copy(isPlaying = true)
    }

    override fun pause() {
        musicPlayerState = musicPlayerState.copy(isPlaying = false)
    }

    override fun seekTo(positionMs: Long) {
        musicPlayerState = musicPlayerState.copy(currentDurationMs = positionMs)
    }

    override fun observeState(onStateChanged: (MusicPlayerState) -> Unit) {
        scope.launch {
            while (isActive) {
                onStateChanged(musicPlayerState)
                delay(250)
                if (musicPlayerState.isPlaying) {
                    musicPlayerState = musicPlayerState.copy(
                        currentDurationMs = musicPlayerState
                            .currentDurationMs
                            .plus(250)
                            .mod(musicPlayerState.totalDurationMs) // todo: div by zero protection
                    )
                }
            }
        }
    }
}
