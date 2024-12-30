package com.example.musicplayer.ui.theme

import android.media.MediaPlayer
import com.example.musicplayer.MusicData

class MusicPlayer private constructor() {
    private var mediaPlayer: MediaPlayer? = null
    private var currentMusic: MusicData? = null

    fun playMusic(music: MusicData) {
        try {
            if (currentMusic?.id != music.id) {
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(music.filePath)
                    prepare()
                    start()
                }
                currentMusic = music
            } else {
                mediaPlayer?.start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun pauseMusic() {
        try {
            mediaPlayer?.pause()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun seekTo(position: Long) {
        try {
            mediaPlayer?.seekTo(position.toInt())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun release() {
        try {
            mediaPlayer?.release()
            mediaPlayer = null
            currentMusic = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getCurrentPosition(): Int {
        return try {
            mediaPlayer?.currentPosition ?: 0
        } catch (e: Exception) {
            0
        }
    }

    companion object {
        @Volatile
        private var instance: MusicPlayer? = null

        fun getInstance(): MusicPlayer {
            return instance ?: synchronized(this) {
                instance ?: MusicPlayer().also { instance = it }
            }
        }
    }
}