package com.example.musicplayer


sealed interface MusicEvent {
    data class Start(val music:MusicData):MusicEvent
    data class Play(val music:MusicData):MusicEvent
    data class Pause(val music:MusicData):MusicEvent
    data class Next(val music:MusicData):MusicEvent
    data class Previous(val music:MusicData):MusicEvent
    data class SliderChange(val position:Float,val music:MusicData):MusicEvent
}