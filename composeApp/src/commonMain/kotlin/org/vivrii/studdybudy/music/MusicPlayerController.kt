package org.vivrii.studdybudy.music

interface MusicPlayerController {
    fun play()
    fun pause()
    fun seekTo(positionMs: Long)
    fun loadSong(songUri: String)
    fun observeState(onStateChanged: (MusicPlayerState) -> Unit)
}
