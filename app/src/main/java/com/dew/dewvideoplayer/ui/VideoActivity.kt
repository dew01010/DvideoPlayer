package com.dew.dewvideoplayer.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dew.dewvideoplayer.R
import com.dew.dewvideoplayer.adpater.VideoAdapter
import com.dew.dewvideoplayer.model.Video
import com.dew.dewvideoplayer.util.Util
import com.dew.dewvideoplayer.viewModel.VideoViewModel
import kotlinx.android.synthetic.main.activity_video.*
import java.io.IOException
import java.lang.Exception


class VideoActivity : AppCompatActivity(), SurfaceHolder.Callback, MediaPlayer.OnPreparedListener,
    CustomMediaController.MediaPlayerControl, VideoAdapter.VideoAdapterListener {

    private lateinit var mAdapter: VideoAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var videoHolder: SurfaceHolder
    private lateinit var anchorView: FrameLayout
    private lateinit var videoList: ArrayList<Video>
    private lateinit var toolbar: Toolbar
    private val videoViewModel: VideoViewModel by viewModels()

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {

    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        player?.setDisplay(holder)
        player?.prepareAsync()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        loadingBar?.visibility = View.GONE
        controller?.setMediaPlayer(this);
        controller?.setAnchorView(anchorView)
        player?.start()
    }

    override fun start() {
        if (player != null) {
            return player?.start()!!;
        }
    }

    override fun pause() {
        if (player != null) {
            return player?.pause()!!;
        }
    }


    override fun getDuration(): Int {
        if (player != null) {
            return player?.duration!!;
        } else {
            return 0
        }
    }

    override fun getCurrentPosition(): Int {
        if (player != null) {
            return player?.currentPosition ?: 0
        } else {
            return 0
        }
    }

    override fun seekTo(pos: Int) {
        if (player != null) {
            return player?.seekTo(pos)!!;
        }
    }

    override fun isPlaying(): Boolean {
        if (player != null) {
            return player?.isPlaying()!!;
        } else {
            return false
        }
    }

    override fun getBufferPercentage(): Int {
        return 0
    }

    override fun canPause(): Boolean {
        return true
    }

    override fun canSeekBackward(): Boolean {
        return true
    }

    override fun canSeekForward(): Boolean {
        return true
    }

    override fun isFullScreen(): Boolean {
        return requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        videoViewModel.cVideoPos = player?.currentPosition ?: 0
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            if (player?.isPlaying == true) {
                player?.stop()
            }
        }
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            if (player?.isPlaying == true) {
                player?.stop()
            }
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun toggleFullScreen() {
        if (isFullScreen()) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        controller?.updateFullScreen()
    }

    var player: MediaPlayer? = null
    var controller: CustomMediaController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        loadingBar?.visibility = View.GONE
        supportActionBar?.title = getString(R.string.app_name)
        if (resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
            supportActionBar?.show()
        } else {
            supportActionBar?.hide()
        }
        videoHolder = videoSurface.holder
        recyclerView = findViewById(R.id.rv)
        anchorView = findViewById(R.id.vs_container)
        videoHolder.addCallback(this)

        player = videoViewModel.getMediaPlayer()
        controller = CustomMediaController(this, true)
        c_layout?.setOnClickListener {
            if (!recyclerView.isVisible) {
                rorate_Clockwise(arrow!!)
                recyclerView.visibility = View.VISIBLE
            } else {
                rorate_AntiClockwise(arrow!!)
                recyclerView.visibility = View.GONE
            }
        }
        videoViewModel.cVideo.observe(this, Observer {
            loadingBar?.visibility = View.VISIBLE
            startVideo(it)
        })
        videoViewModel.mData.observe(this, Observer {
            videoList.clear()
            videoList.addAll(it)
            mAdapter.notifyDataSetChanged()
        })
        setUpAdapter()
        videoViewModel.loadVideo()
    }

    private fun startVideo(video: Video) {
        try {
            player?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            player?.setDataSource(
                this,
                Uri.parse(video.url)
            )
            player?.setOnPreparedListener(this)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun onPause() {
        super.onPause()
        player?.stop()
    }

    override fun onStop() {
        super.onStop()
        player?.stop()
    }



    override fun onTouchEvent(event: MotionEvent): Boolean {
        controller?.show()
        return false
    }


    private fun setUpAdapter() {
        videoList = arrayListOf()
        mAdapter = VideoAdapter(
            videoList,
            this, this
        )
        val mLayoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.adapter = mAdapter
    }

    override fun onClickRow(video: Video) {
//        player?.reset()
//        startVideo(video)
    }

    fun rorate_Clockwise(view: View) {
        val rotate = ObjectAnimator.ofFloat(view, "rotation", 180f, 0f)
        rotate.duration = 500
        rotate.start()
    }

    fun rorate_AntiClockwise(view: View) {
        val rotate = ObjectAnimator.ofFloat(view, "rotation", 0f, 180f)
        rotate.duration = 500
        rotate.start()
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        try {
//            player?.stop()
//            player?.release()
//        }catch (e:Exception){
//        }
//    }


}
