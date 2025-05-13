package org.vivrii.studdybudy.music

data class MusicPlayerState(
    val isPlaying: Boolean = false,
    val currentDurationMs: Long = 0L,
    val totalDurationMs: Long = 0L
)
