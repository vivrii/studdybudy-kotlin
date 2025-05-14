package org.vivrii.studdybudy.music

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class AndroidMusicPlayerController(context: Context) : MusicPlayerController {
    private lateinit var _onStateChanged: () -> Unit

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var player = ExoPlayer.Builder(context).build()

    override fun play() {
        if (player.isCommandAvailable(Player.COMMAND_PLAY_PAUSE)) {
            player.play()
        } // todo: throw error + user notification? + log if command not available
    }

    override fun pause() {
        if (player.isCommandAvailable(Player.COMMAND_PLAY_PAUSE)) {
            player.pause()
        } // todo: throw error + user notification? + log if command not available
    }

    override fun seekTo(positionMs: Long) {
        if (player.isCommandAvailable(Player.COMMAND_SEEK_IN_CURRENT_MEDIA_ITEM)) {
            player.seekTo(positionMs)
        } // todo: throw error + user notification? + log if command not available
    }

    override fun loadSong(songUri: String) {
        if (player.isCommandAvailable(Player.COMMAND_SET_MEDIA_ITEM)) {
            val mediaItem = MediaItem.fromUri(songUri)
            player.setMediaItem(mediaItem)
            player.prepare()
        } // todo: throw error + user notification? + log if command not available
    }

    override fun observeState(onStateChanged: (MusicPlayerState) -> Unit) {
        _onStateChanged = {
            onStateChanged(
                MusicPlayerState(
                    isPlaying = player.isPlaying,
                    currentDurationMs = player.currentPosition,
                    totalDurationMs = player.duration
                )
            )
        }

        scope.launch {
            while (isActive) {
                _onStateChanged()
                delay(250)
            }
        }
    }
}
