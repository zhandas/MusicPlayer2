package com.example.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.example.musicplayer.ui.theme.MusicListScreen
import com.example.musicplayer.ui.theme.MusicPlayerTheme



class MainActivity : ComponentActivity() {
    private val musicViewModel by viewModels<MusicViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicPlayerTheme {
                MusicListScreen(
                    musicViewModel = musicViewModel,
                    onSelectedAudio = { event ->
                        musicViewModel.onMusicEvent(event)
                    }
                )
            }
        }

        musicViewModel.setContentResolver(contentResolver)
    }
}

