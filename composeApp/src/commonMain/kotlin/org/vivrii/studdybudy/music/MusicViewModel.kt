package org.vivrii.studdybudy.music

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.vivrii.studdybudy.music.model.Song

class MusicViewModel(private val controller: MusicPlayerController) {
    // todo: look into the common viewmodel: https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-viewmodel.html
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _currentSong = MutableStateFlow<Song?>(Song(
        // todo: set this null to begin with here when real data can be populated
        title = "その箱の中に入る。",
        artist = "Rory in early 20s",
        uri = "https://sharkinfestedserver.ddns.net/song.mp3",
        albumArtUri = "https://i.scdn.co/image/ab67616d00001e02a2c008f576d66fa1b7b82050"
    ))
    val currentSong: StateFlow<Song?> = _currentSong

    private val _playerState = MutableStateFlow(MusicPlayerState())
    val playerState: StateFlow<MusicPlayerState> = _playerState

    val progress: StateFlow<Float> = _playerState.map { state ->
        if (state.totalDurationMs > 0) {
            state.currentDurationMs / state.totalDurationMs.toFloat()
        } else {
            0.0f
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0f
    )

    private val _repeatMode = MutableStateFlow(0)
    val repeatMode: StateFlow<Int> = _repeatMode

    init {
        _currentSong.value?.uri?.let { controller.loadSong(it) } // todo: temporary placement here

        controller.observeState { newState ->
            _playerState.value = newState
        }
    }

    fun playPauseToggle() {
        val playing = _playerState.value.isPlaying
        if (playing) controller.pause() else controller.play()
    }

    fun repeatModeCycle() {
        _repeatMode.value = _repeatMode.value.plus(1).mod(3)
    }

    fun seekTo(positionMs: Long) {
        controller.seekTo(positionMs)
    }
}
