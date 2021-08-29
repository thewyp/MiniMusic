package com.thewyp.minimusic.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewyp.minimusic.exoplayer.MusicService
import com.thewyp.minimusic.exoplayer.MusicServiceConnection
import com.thewyp.minimusic.exoplayer.currentPlaybackPosition
import com.thewyp.minimusic.other.Constants.UPDATE_PLAYER_POSITION_INTERVAL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    musicServiceConnection: MusicServiceConnection
) : ViewModel() {

    private val playbackStateCompat = musicServiceConnection.playbackState

    private val _curSongDuration = MutableLiveData<Long>()
    val curSongDuration = _curSongDuration

    private val _curPlayerPosition = MutableLiveData<Long>()
    val curPlayerPosition = _curPlayerPosition

    init {
        updateCurrentPlayerPosition()
    }

    private fun updateCurrentPlayerPosition() {
        viewModelScope.launch {
            while (true) {
                val pos = playbackStateCompat.value?.currentPlaybackPosition
                pos?.let {
                    if (_curPlayerPosition.value != it) {
                        _curPlayerPosition.postValue(it)
                        _curSongDuration.postValue(MusicService.curSongDuration)
                    }
                }
                delay(UPDATE_PLAYER_POSITION_INTERVAL)
            }
        }
    }

}