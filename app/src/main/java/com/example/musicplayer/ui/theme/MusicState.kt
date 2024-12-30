package com.example.musicplayer.ui.theme

import androidx.compose.material3.SliderState
import com.example.musicplayer.MusicData

data class MusicState(
    val musicList: MutableList<MusicData> = mutableListOf(),
    val playerState: PlayerState?=null,
    val sliderState: Float=0f
)
