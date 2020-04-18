package com.dew.dewvideoplayer.viewModel

import android.media.MediaPlayer
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dew.dewvideoplayer.model.Video
import com.dew.dewvideoplayer.util.Util

class VideoViewModel : ViewModel() {

    var mData = MutableLiveData<ArrayList<Video>>()
    var cVideo = MutableLiveData<Video>()
    var player:MediaPlayer?=null
    var cVideoPos=0

    fun loadVideo() {
        //load video from repo or device
        mData.value = Util.getVideoList()
        cVideo.value = mData.value!![0]
    }

    fun getMediaPlayer():MediaPlayer{
        if(player==null){
            player= MediaPlayer()
        }
        return player as MediaPlayer
    }

}