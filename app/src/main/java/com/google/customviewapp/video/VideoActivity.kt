package com.google.customviewapp.video

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.customviewapp.R
import com.google.customviewapp.databinding.ActionVideoBinding


class VideoActivity : AppCompatActivity(R.layout.action_video) {

    private val binding: ActionVideoBinding by lazy {
        ActionVideoBinding.inflate(layoutInflater)
    }

    private var arrayList = mutableListOf(

        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"

    )
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val mediaController = MediaController(this)
        mediaController.setAnchorView(binding.videoView)
        binding.videoView.apply {
            setMediaController(mediaController)
            setVideoURI(Uri.parse(arrayList[index]))
            requestFocus()
            setOnPreparedListener { mp ->
                mp.setOnVideoSizeChangedListener { _, _, _ ->
                    setMediaController(mediaController)
                    mediaController.setAnchorView(this)
                }
            }
            setOnCompletionListener { mp ->
                Toast.makeText(applicationContext, "Video over", Toast.LENGTH_SHORT).show()
                if (index++ == arrayList.size) {
                    index = 0
                    mp.release()
                    Toast.makeText(applicationContext, "Videos completed", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    setVideoURI(Uri.parse(arrayList[index]))
                    start()
                }
            }
            setOnErrorListener { _, what, extra ->
                Log.d("API123", "What $what extra $extra")
                false
            }
        }
    }

}