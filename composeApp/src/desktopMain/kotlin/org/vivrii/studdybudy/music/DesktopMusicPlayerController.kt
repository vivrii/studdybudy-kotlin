package org.vivrii.studdybudy.music

import io.kamel.core.utils.File
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.util.Duration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class DesktopMusicPlayerController : MusicPlayerController {
    private lateinit var _onStateChanged: () -> Unit

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var player: MediaPlayer? = null

    override fun play() {
        player?.play()
    } // todo: handle/throw errors + notify when null

    override fun pause() {
        player?.pause()
    } // todo: handle/throw errors + notify when null

    override fun seekTo(positionMs: Long) {
        player?.seek(Duration(positionMs.toDouble()))
    } // todo: handle/throw errors + notify when null

    override fun loadSong(songUri: String) {
        // todo: skip if local file...
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(songUri))
            .build()

        val file = File.createTempFile("song", ".mp4")
        file.deleteOnExit()

        client.send(request, HttpResponse.BodyHandlers.ofFile(file.toPath()))

        val media = Media(file.toURI().toString())
        player = MediaPlayer(media)
    } // todo: handle/throw errors

    override fun repeatMode(repeat: Boolean) {
        player?.cycleCount = if (repeat) MediaPlayer.INDEFINITE else 1
    } // todo: handle/throw errors + notify when null

    override fun observeState(onStateChanged: (MusicPlayerState) -> Unit) {
        _onStateChanged = {
            onStateChanged(
                MusicPlayerState(
                    isPlaying = player?.status == MediaPlayer.Status.PLAYING,
                    currentDurationMs = player?.currentTime?.toMillis()?.toLong() ?: 0L,
                    totalDurationMs = player?.media?.duration?.toMillis()?.toLong() ?: 0L
                )
            )
        }

        scope.launch {
            while (isActive) {
                _onStateChanged()
                if (player?.status == MediaPlayer.Status.PLAYING) {
                    delay(100)
                } else {
                    delay(250)
                }
            }
        }
    }
}