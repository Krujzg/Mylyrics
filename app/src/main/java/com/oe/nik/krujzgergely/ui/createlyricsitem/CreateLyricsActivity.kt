package com.oe.nik.krujzgergely.ui.createlyricsitem

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.oe.nik.krujzgergely.R
import com.oe.nik.krujzgergely.databinding.ActivityCreateNewlyricsBinding
import com.oe.nik.krujzgergely.ui.lyrics.LyricsActivity

class CreateLyricsActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_newlyrics)

        val createLyricsActivityViewModel = ViewModelProvider(this).get(CreateLyricsActivityViewModel::class.java)

        DataBindingUtil.setContentView<ActivityCreateNewlyricsBinding>(this, R.layout.activity_create_newlyrics).apply {
            this.lifecycleOwner = this@CreateLyricsActivity
            this.createLyricsModel = createLyricsActivityViewModel
        }
    }
}