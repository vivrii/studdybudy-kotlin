package org.vivrii.studdybudy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.vivrii.studdybudy.ui.components.MediaPlayer
import org.vivrii.studdybudy.ui.extensions.measureWidth
import org.vivrii.studdybudy.ui.theme.AppTheme

@Composable
@Preview
fun App() {
    AppTheme {
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }

        fun showTextSnackbar(text: String) {
            snackbarHostState.currentSnackbarData?.dismiss()
            scope.launch {
                snackbarHostState.showSnackbar(text)
            }
        }

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
                            onClick = { showTextSnackbar("Randomiser") },
                            modifier = modifier
                        ) {
                            Text(text = "Randomiser")
                        }
                        Button(
                            onClick = { showTextSnackbar("Looper") },
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
