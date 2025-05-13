package org.vivrii.studdybudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.vivrii.studdybudy.music.AndroidMusicPlayerController
import org.vivrii.studdybudy.music.MusicViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = MusicViewModel(controller = AndroidMusicPlayerController())

        setContent {
            App(viewModel = viewModel)
        }
    }
}

//@Preview
//@Composable
//fun AppAndroidPreview() {
//    App()
//}
