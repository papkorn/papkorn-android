package com.nuller.popkorn.activities

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.MimeTypes
import com.google.common.collect.ImmutableList
import com.nuller.popkorn.R


class TestActivity : AppCompatActivity() {

    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var bundle: Bundle

    // creating a variable for exoplayer
    private lateinit var playerView: PlayerView

    // url of video which we are loading.
    private var videoURL = "http://dl2.gemescape.com/FILM/How%20to%20Train%20Your%20Dragon%202010/How.To.Train.Your.Dragon.2010.480p.BluRay.Pahe.%40Gemovies.mkv"

    //"http://dl4.gemescape.com/FILM/2022/Top%20Gun%20Maverick/Top.Gun.Maverick.2022.IMAX.480p.WEB-DL.x264.Pahe.PK.mkv"
    private var subtitleUrl = "https://5fb0e2805042ef0018e46033.iran.liara.space/subtitles/1646971/How.to.Train.Your.Dragon.2.2014.720p.WEB-DL-AAC2.0.H264-RARBG.srt"
    //"https://5fb0e2805042ef0018e46033.iran.liara.space/subtitles/1745960/Top.Gun.Maverick.2022.HC.IMAX.WEB-DL.Fa.%5BUTF8%5D.srt"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        supportActionBar?.hide();
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        setContentView(R.layout.activity_test)

        Toast.makeText(applicationContext,"درحال بارگیری",Toast.LENGTH_SHORT).show()
        Toast.makeText(applicationContext,"شکیبا باشید",Toast.LENGTH_SHORT).show()

        bundle = intent.extras!!
        videoURL = bundle.getString("video")!!
        subtitleUrl = bundle.getString("subtitle")!!

        playerView = findViewById(R.id.video_view)

        simpleExoPlayer = SimpleExoPlayer.Builder(this).build()
        playerView.player = simpleExoPlayer

        val subtitle = MediaItem.SubtitleConfiguration.Builder(Uri.parse(subtitleUrl))
            .setMimeType(MimeTypes.APPLICATION_SUBRIP)
            .setLanguage("fa")
            .setSelectionFlags(C.SELECTION_FLAG_DEFAULT)
            .build()

        val mediaItemSubtitle = MediaItem.Subtitle(Uri.parse(subtitleUrl),
            MimeTypes.TEXT_VTT,
            "en",
            C.SELECTION_FLAG_DEFAULT)

        val mediaItem=MediaItem.Builder()
            .setUri(Uri.parse(videoURL))
            .setSubtitleConfigurations(ImmutableList.of(subtitle))
            .build()
        simpleExoPlayer.setMediaItem(mediaItem)
        simpleExoPlayer.prepare()
        simpleExoPlayer.playWhenReady = true


//        val assetSrtUri = Uri.parse(("file:///android_asset/subtitle.srt"))
//        val subtitle = MediaItem.SubtitleConfiguration.Builder(assetSrtUri)
//            .setMimeType(MimeTypes.APPLICATION_SUBRIP)
//            .setLanguage("en")
//            .setSelectionFlags(C.SELECTION_FLAG_DEFAULT)
//            .build()
//
//        val assetVideoUri = Uri.parse(("file:///android_asset/video.mp4"))
//        val mediaItem = MediaItem.Builder()
//            .setUri(assetVideoUri)
//            .setSubtitleConfigurations(ImmutableList.of(subtitle))
//            .build()

//        simpleExoPlayer.setMediaItem(mediaItem)
//
//        simpleExoPlayer.prepare()
//        simpleExoPlayer.play()


    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer.stop()
    }
}