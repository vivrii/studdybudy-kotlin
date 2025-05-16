package org.vivrii.studdybudy.music

interface MusicPlayerController {
    fun play()
    fun pause()
    fun seekTo(positionMs: Long)
    fun loadSong(songUri: String)
    fun repeatMode(repeat: Boolean)
    fun observeState(onStateChanged: (MusicPlayerState) -> Unit)
}
