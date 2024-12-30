package com.example.musicplayer

import android.os.Parcelable
import androidx.compose.ui.graphics.Path
import kotlinx.parcelize.Parcelize
import kotlin.time.Duration

@Parcelize
data class MusicData(
    val id:Long?=null,
    val name:String?=null,
    val duration:Long?=null,
    val filePath:String?=null,
):Parcelable
