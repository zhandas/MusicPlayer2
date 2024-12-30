package com.example.musicplayer.ui.theme

import com.example.musicplayer.MusicData

data class PlayerState(
    val music:MusicData?=null,
    val action: PlayerAction=PlayerAction.PAUSE
)