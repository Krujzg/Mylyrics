package com.oe.nik.krujzgergely.ui.updatelyricsitem

import android.app.Application
import android.app.NotificationManager
import androidx.core.content.ContextCompat
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.oe.nik.krujzgergely.data.LyricsDatabase
import com.oe.nik.krujzgergely.models.enums.CrudType
import com.oe.nik.krujzgergely.models.LyricsModel
import com.oe.nik.krujzgergely.repository.LyricsRepository
import com.oe.nik.krujzgergely.ui.lyrics.LyricsActivityAdapter
import com.oe.nik.krujzgergely.util.sendNotification
import kotlinx.coroutines.launch

class UpdateLyricsItemViewModel (application: Application) : AndroidViewModel(application), Observable
{
    private val notificationManager = ContextCompat.getSystemService(application,
        NotificationManager::class.java) as NotificationManager

    private var repository: LyricsRepository

    private var lyricsModel : LyricsModel = LyricsActivityAdapter.currentLyrics

    private val callbacks: PropertyChangeRegistry by lazy { PropertyChangeRegistry() }

    @Bindable
    var displayedPerformer  = MutableLiveData<String>()

    @Bindable
    var displayedTitle = MutableLiveData<String>()

    @Bindable
    var displayedLyricsText = MutableLiveData<String>()

    @Bindable
    var displayedyoutubelink = MutableLiveData<String>()

    @Bindable
    var displayedspotifylink = MutableLiveData<String>()

    @Bindable
    var displayedyoutubeMusiclink = MutableLiveData<String>()

    @Bindable
    var toggleButtonFavorite = MutableLiveData<Boolean>()

    init
    {
        val lyricsDao= LyricsDatabase
            .getDatabase(application,viewModelScope,application.resources)
            .lyricsDao()
        this.repository = LyricsRepository(lyricsDao)
        onDisplayContents()
    }
    
    private fun onDisplayContents()
    {
        onDisplayPerformerContent()
        onDisplaySongTitleContent()
        onDisplayLyricsContent()
        onDisplayYoutubeLinkContent()
        onDisplayFavoriteContent()
        onDisplaySpotifyLinkContent()
        onDisplayYoutubeMusicLinkContent()
    }

    private fun updateLyricsModelLocally()
    {
        lyricsModel.apply {
            performer = displayedPerformer.value!!
            title = displayedTitle.value!!
            lyrics_text = displayedLyricsText.value!!
            youtubeLink = displayedyoutubelink.value!!
            spotifyLink = displayedspotifylink.value!!
            favourite = toggleButtonFavorite.value!!
        }
    }

    private fun sendNotification(title :String,message : String)
    {
        notificationManager.sendNotification(title, message, CrudType.UPDATE,getApplication())
    }

    private fun onDisplayPerformerContent() {displayedPerformer.value = lyricsModel.performer }
    private fun onDisplaySongTitleContent() {displayedTitle.value = lyricsModel.title }
    private fun onDisplayLyricsContent() {displayedLyricsText.value = lyricsModel.lyrics_text }
    private fun onDisplayYoutubeLinkContent() {displayedyoutubelink.value = lyricsModel.youtubeLink }
    private fun onDisplaySpotifyLinkContent() {displayedspotifylink.value = lyricsModel.spotifyLink }
    private fun onDisplayFavoriteContent() {toggleButtonFavorite.value = lyricsModel.favourite }
    private fun onDisplayYoutubeMusicLinkContent() {displayedyoutubeMusiclink.value = lyricsModel.youtubeMusicLink }

    fun updateLyricsFromLocalDb()
    {
        updateLyricsModelLocally()
        viewModelScope.launch { repository.updateInDb(lyricsModel) }
        sendNotification("You have edited this Lyrics:", "${displayedPerformer.value} - ${displayedTitle.value}")
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) { callbacks.add(callback) }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) { callbacks.remove(callback) }
}