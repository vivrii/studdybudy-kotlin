package org.vivrii.studdybudy.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.vectorResource
import org.vivrii.studdybudy.music.MusicPlayerState
import org.vivrii.studdybudy.music.MusicViewModel
import org.vivrii.studdybudy.music.MusicViewModel.Companion.PLAYER_STATE_UI_RATE
import org.vivrii.studdybudy.music.model.Song
import org.vivrii.studdybudy.ui.extensions.debug
import studdybudy.composeapp.generated.resources.Res
import studdybudy.composeapp.generated.resources.pause
import studdybudy.composeapp.generated.resources.play
import studdybudy.composeapp.generated.resources.repeat
import studdybudy.composeapp.generated.resources.repeat_off
import studdybudy.composeapp.generated.resources.repeat_once


@Composable
fun MediaPlayer(viewModel: MusicViewModel) {
    val currentSong by viewModel.currentSong.collectAsState()
    val playerState by viewModel.playerState.collectAsState()
    val repeatMode by viewModel.repeatMode.collectAsState()
    val progress by viewModel.progress.collectAsState()

    NowPlayingBar(
        song = currentSong,
        playerState = playerState,
        repeatMode = repeatMode,
        progress = progress,
        onClickPlay = { viewModel.playPauseToggle() },
        onClickRepeat = { viewModel.repeatModeCycle() },
    )
}

@Composable
fun NowPlayingBar(
    song: Song?,
    playerState: MusicPlayerState,
    repeatMode: Int,
    progress: Float,
    onClickPlay: () -> Unit,
    onClickRepeat: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(12.dp)
            .clip(MaterialTheme.shapes.large)
            .background(color = MaterialTheme.colors.surface) // todo: to be a colour based on album colour + transparency effect w background
            .padding(3.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AlbumArtIcon(song?.albumArtUri)
                SongInformation(song?.title, song?.artist)
                PlayIcon(playerState.isPlaying) { onClickPlay() }
                RepeatIcon(repeatMode) { onClickRepeat() }
            }

            SongProgressIndicator(progress, playerState.totalDurationMs, playerState.isPlaying)
        }
    }
}

@Composable
private fun SongProgressIndicator(progress: Float, totalDurationMs: Long, isPlaying: Boolean) {
    var previousProgress by remember { mutableFloatStateOf(0.0f) }
    val animatedProgress = remember { Animatable(0.0f) }

    LaunchedEffect(isPlaying) {
        animatedProgress.stop()
    }

    LaunchedEffect(progress) {
        if (isPlaying) {
            val resetAnimation = progress < previousProgress

            previousProgress = progress

            if (resetAnimation) {
                animatedProgress.snapTo(
                    targetValue = progress - PLAYER_STATE_UI_RATE.toFloat()/totalDurationMs,
                )
                animatedProgress.animateTo(
                    targetValue = progress,
                    animationSpec = tween(PLAYER_STATE_UI_RATE.toInt(), easing = LinearEasing)
                )
            } else {
                animatedProgress.animateTo(
                    targetValue = progress,
                    animationSpec = tween(
                        PLAYER_STATE_UI_RATE.toInt(),
                        easing = LinearEasing
                    )
                )
            }
        }
    }

    LinearProgressIndicator(
        progress = animatedProgress.value,
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small.copy(topStart = CornerSize(0.dp)))
            .height(6.dp)
    )
}

@Composable
private fun AlbumArtIcon(albumArtUri: String?) {
    val albumArtResource = albumArtUri?.let { asyncPainterResource(it) }
    albumArtResource?.let {
        KamelImage(
            resource = { it },
            contentDescription = " Album art",
            modifier = Modifier.width(48.dp).height(48.dp)
        )
    } ?: run {
        // todo: instead of debug box, use a default uri to an included missing icon file
        Box(modifier = Modifier.width(48.dp).height(48.dp).debug())
    }
}

@Composable
private fun RowScope.SongInformation(title: String?, artist: String?) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.height(48.dp).padding(horizontal = 12.dp).weight(1.0f)
    ) {
        Text(
            text = title ?: "--",
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.body1
        )
        Text(
            text = artist ?: "--",
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.caption
        )
    }
}

@Composable
private fun PlayIcon(
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    if (isPlaying) {
        vectorResource(resource = Res.drawable.pause) to "pause button"
    } else {
        vectorResource(resource = Res.drawable.play) to "play button"
    }.let { (imageVector, contentDescription) ->
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(48.dp)
                .padding(3.dp)
                .clickable { onClick() },
            tint = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
private fun RepeatIcon(
    repeatMode: Int,
    onClick: () -> Unit
) {
    when (repeatMode) {
        0 -> vectorResource(resource = Res.drawable.repeat_off) to "repeat off"
        1 -> vectorResource(resource = Res.drawable.repeat) to "repeat on"
        else -> vectorResource(resource = Res.drawable.repeat_once) to "repeat once"
    }.let { (imageVector, contentDescription) ->
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(32.dp)
                .padding(3.dp)
                .clickable { onClick() },
            tint = MaterialTheme.colors.onSurface
        )
    }
}
