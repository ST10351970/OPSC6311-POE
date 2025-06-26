package com.example.middleman

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import android.content.Intent
import com.example.middleman.databinding.ActivityHowItWorksBinding

class HowItWorks : AppCompatActivity() {

    private lateinit var binding: ActivityHowItWorksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHowItWorksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the back button
        binding.backButton.setOnClickListener {
            finish()
        }

        // Set up video button to open YouTube
        binding.watchVideoButton.setOnClickListener {
            openYouTubeVideo()
        }
    }

    private fun openYouTubeVideo() {
        val videoId = "-c4sVoqBn1I"

        // Try to open in YouTube app first
        val youtubeIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
        youtubeIntent.putExtra("force_fullscreen", true)

        // If YouTube app is not available, open in browser
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/$videoId"))

        try {
            startActivity(youtubeIntent)
        } catch (e: Exception) {
            startActivity(browserIntent)
        }
    }
}