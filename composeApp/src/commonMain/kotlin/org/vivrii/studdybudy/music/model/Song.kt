package org.vivrii.studdybudy.music.model

data class Song(
    val title: String,
    val artist: String,
    val audioUri: String,
    val albumArtUri: String?
)
