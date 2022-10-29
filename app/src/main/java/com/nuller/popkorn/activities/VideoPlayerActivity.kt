package com.nuller.popkorn.activities

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.nuller.popkorn.R
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout

class VideoPlayerActivity : AppCompatActivity() {

    private var mLibVLC: LibVLC? = null
    private var mMediaPlayer: MediaPlayer? = null
    private lateinit var vlcVideoLayout: VLCVideoLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        vlcVideoLayout = findViewById(R.id.view_vlc_layout)

        mLibVLC = LibVLC(this, ArrayList<String>().apply {
            add("--no-drop-late-frames")
            add("--no-skip-frames")
            add("--rtsp-tcp")
            add("-vvv")
        })

        mMediaPlayer = MediaPlayer(mLibVLC)
        mMediaPlayer?.attachViews(vlcVideoLayout, null, false, false)

        Media(mLibVLC, Uri.parse("http://dl4.gemescape.com/FILM/2022/Top%20Gun%20Maverick/Top.Gun.Maverick.2022.IMAX.480p.WEB-DL.x264.Pahe.PK.mkv")).apply {
            setHWDecoderEnabled(true, false);
            addOption(":network-caching=150");
            addOption(":clock-jitter=0");
            addOption(":clock-synchro=0");
            mMediaPlayer?.media = this
        }.release()
        mMediaPlayer!!.play()

    }
}