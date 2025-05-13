package org.vivrii.studdybudy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import studdybudy.composeapp.generated.resources.Res
import studdybudy.composeapp.generated.resources.pause
import studdybudy.composeapp.generated.resources.play
import studdybudy.composeapp.generated.resources.repeat
import studdybudy.composeapp.generated.resources.repeat_off
import studdybudy.composeapp.generated.resources.repeat_once

@Composable
@Preview
fun App() {
    AppTheme {
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val modifier = Modifier.padding(6.dp).measureWidth()

                        Button(
                            onClick = {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                scope.launch {
                                    snackbarHostState.showSnackbar("Randomiser")
                                }
                            },
                            modifier = modifier
                        ) {
                            Text(text = "Randomiser")
                        }
                        Button(
                            onClick = {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                scope.launch {
                                    snackbarHostState.showSnackbar("Looper")
                                }
                            },
                            modifier = modifier
                        ) {
                            Text(text = "Looper")
                        }
                    }

                    Spacer(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth()
                            .height(3.dp)
                            .clip(MaterialTheme.shapes.small)
                            .background(MaterialTheme.colors.surface)
                    )
                }
            },
            bottomBar = { MediaPlayer() }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {

            }
        }
    }
}

@Composable
private fun MediaPlayer() {
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
            var playing by remember { mutableStateOf(false) }
            var repeatMode by remember { mutableIntStateOf(0) }
            var progress by remember { mutableFloatStateOf(0.7f) }

            LaunchedEffect(playing, repeatMode) {
                while (playing) {
                    progress += 0.0005555556f
                    if (progress >= 1.0f) {
                        if (repeatMode == 0) {
                            playing = false
                        } else {
                            progress = 0.0f
                        }
                    }
                    delay(100)
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.width(48.dp).height(48.dp).debug())
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.height(48.dp).padding(horizontal = 12.dp)
                        .weight(1.0f)
                ) {
                    Text(
                        text = "Leave Me Be",
                        color = MaterialTheme.colors.onSurface,
                        style = TextStyle(
                            fontSize = 16.sp,
                        )
                    )
                    Text(
                        text = "Computerwife",
                        color = MaterialTheme.colors.onSurface,
                        style = TextStyle(
                            fontSize = 12.sp,
                        )
                    )
                }

                if (playing) {
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
                            .clickable { playing = !playing },
                        tint = MaterialTheme.colors.onSurface
                    )
                }

                when (repeatMode) {
                    0 -> {
                        Triple(
                            vectorResource(resource = Res.drawable.repeat_off),
                            "repeat off",
                            { repeatMode = 1 }
                        )
                    }

                    1 -> {
                        Triple(
                            vectorResource(resource = Res.drawable.repeat),
                            "repeat on",
                            { repeatMode = 2 }
                        )
                    }

                    else -> {
                        Triple(
                            vectorResource(resource = Res.drawable.repeat_once),
                            "repeat once",
                            { repeatMode = 0 }
                        )
                    }
                }.let { (imageVector, contentDescription, onClick) ->
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

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small.copy(topStart = CornerSize(0.dp)))
                    .height(6.dp)
            )

            LaunchedEffect(repeatMode, playing) {

            }
        }
    }
}
