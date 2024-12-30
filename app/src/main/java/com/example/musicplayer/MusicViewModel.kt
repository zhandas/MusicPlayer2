package com.example.musicplayer

import android.content.ContentResolver
import android.icu.text.Transliterator.Position
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.ui.theme.MusicPlayer
import com.example.musicplayer.ui.theme.MusicState
import com.example.musicplayer.ui.theme.PlayerAction
import com.example.musicplayer.ui.theme.PlayerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration

class MusicViewModel:ViewModel()
{
    private var contentResolver:ContentResolver?=null
    private val musicPlayer=MusicPlayer.getInstance()

    private val _musicState= MutableStateFlow(MusicState())
    val musicState: StateFlow<MusicState> = _musicState

    private var currentJob:Job?=null

    fun setContentResolver(contentResolver: ContentResolver){
        this.contentResolver=contentResolver
    }

    fun loadFile()=viewModelScope.launch(Dispatchers.IO) {
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} !=0"
        val sortOrder = "${MediaStore.Audio.Media.DATE_ADDED} DESC"

        contentResolver?.let { cosRes ->
            val cursor = cosRes.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                sortOrder

            )
            val musics = mutableListOf<MusicData>()
            cursor?.use {
                val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val pathColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

                while (it.moveToNext()) {
                    val id = it.getLong(idColumn)
                    val name = it.getString(nameColumn)
                    val duration = it.getLong(durationColumn)
                    val path = it.getString(pathColumn)

                    musics.add(MusicData(id, name, duration, path))
                }
                _musicState.value = _musicState.value.copy(
                    musicList = musics
                )
            }
        }

    }

    fun onMusicEvent(event: MusicEvent) {
        when (event) {
            is MusicEvent.Start -> {
                startMusic(event.music)
            }
            is MusicEvent.Play -> {
                playMusic(event.music)
            }
            is MusicEvent.Pause -> {
                pauseMusic()
            }
            is MusicEvent.Next -> {
                playNextMusic(event.music)
            }
            is MusicEvent.Previous -> {
                playPreviousMusic(event.music)
            }
            is MusicEvent.SliderChange -> {
                seekTo(event.position.toLong(), event.music)
            }
        }
    }

    private fun startMusic(music: MusicData) {
        musicPlayer.playMusic(music)
        updatePlayerState(music, PlayerAction.PLAY)
        startProgressUpdate()
    }

    private fun playMusic(music: MusicData) {
        musicPlayer.playMusic(music)
        updatePlayerState(music, PlayerAction.PLAY)
        startProgressUpdate()
    }

    private fun pauseMusic(){
        musicPlayer.pauseMusic()
        _musicState.value=_musicState.value.copy(
            playerState = _musicState.value.playerState?.copy(
                action = PlayerAction.PAUSE
            )
        )
        currentJob?.cancel()
    }

    private fun seekTo(position: Long, music: MusicData){
        musicPlayer.seekTo(position)
        _musicState.value=_musicState.value.copy(
            sliderState =position.toFloat()
        )
    }

    private fun playNextMusic(currentMusic:MusicData){
        val currentIndex=_musicState.value.musicList.indexOfFirst{it.id==currentMusic.id}
        val previousIndex=if(currentIndex>0) currentIndex-1 else _musicState.value.musicList.size-1
        val previousMusic=_musicState.value.musicList[previousIndex]
        startMusic(previousMusic)
    }

    private fun playPreviousMusic(currentMusic: MusicData) {
        try {
            val currentIndex = _musicState.value.musicList.indexOfFirst { it.id == currentMusic.id }
            if (currentIndex != -1) {
                val previousIndex = if (currentIndex - 1 < 0) _musicState.value.musicList.size - 1 else currentIndex - 1
                val previousMusic = _musicState.value.musicList[previousIndex]
                startMusic(previousMusic)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updatePlayerState(music: MusicData,action: PlayerAction){
        _musicState.value=_musicState.value.copy(
            playerState = PlayerState(music,action)
        )
    }

    private fun startProgressUpdate(){
        currentJob?.cancel()
        currentJob=viewModelScope.launch {
            while(isActive){
                _musicState.value=_musicState.value.copy(
                    sliderState = musicPlayer.getCurrentPosition().toFloat()
                )
                delay(1000)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
        musicPlayer.release()
    }

}