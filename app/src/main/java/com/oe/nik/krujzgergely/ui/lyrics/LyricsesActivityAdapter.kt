package com.oe.nik.krujzgergely.ui.lyrics

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oe.nik.krujzgergely.R
import com.oe.nik.krujzgergely.databinding.RecyclerItemLyricsModelBinding
import com.oe.nik.krujzgergely.models.LyricsModel

class LyricsesActivityAdapter(private var context: Context, private var LyricsList : MutableList<LyricsModel>)
    : RecyclerView.Adapter<LyricsesActivityAdapter.ViewHolder>()
{
    private val layoutinflater : LayoutInflater = LayoutInflater.from(context)

    private lateinit var listener: IonLyricsSelected

    companion object
    {
        lateinit var currentLyrics : LyricsModel
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ViewHolder
    {
        when(context)
        {
            is IonLyricsSelected -> listener = context as IonLyricsSelected
        }

        val recyclerItemLyricsModelBinding =
            RecyclerItemLyricsModelBinding.inflate(layoutinflater, parent, false)

        return ViewHolder(recyclerItemLyricsModelBinding.root, recyclerItemLyricsModelBinding)
    }

    override fun getItemCount(): Int = LyricsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { holder.setData(LyricsList[position]) }

    fun swapData(lyricsList: List<LyricsModel>)
    {
        this.LyricsList.clear()
        this.LyricsList.addAll(lyricsList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private var view: View, private val recyclerItemLyricsModelBinding: RecyclerItemLyricsModelBinding)
        : RecyclerView.ViewHolder(view)
    {
        private val songTypeImageview: ImageView = view.findViewById<ImageView>(R.id.song_image)

        fun setData(lyricsModel: LyricsModel?)
        {
            view.setOnClickListener {

            currentLyrics = lyricsModel!!

            listener.onLyricsSelected()}

            setImage(songTypeImageview, lyricsModel!!.song_type)

            recyclerItemLyricsModelBinding.lyricsModel = lyricsModel
        }

        fun setImage(song_imageview: ImageView, song_type: String)
        {
            when(song_type)
            {
                "JAZZ" -> Glide.with(context).load(R.drawable.jazz).into(song_imageview)
                "HIPHOP" -> Glide.with(context).load(R.drawable.hiphop).into(song_imageview)
                "ROCK" -> Glide.with(context).load(R.drawable.rock).into(song_imageview)
                "METAL" -> Glide.with(context).load(R.drawable.metal).into(song_imageview)
                "PUNK" -> Glide.with(context).load(R.drawable.punk).into(song_imageview)
                "POP" -> Glide.with(context).load(R.drawable.pop).into(song_imageview)
                "COUNTRY" -> Glide.with(context).load(R.drawable.country).into(song_imageview)
                else -> Glide.with(context).load(R.drawable.opera).into(song_imageview)
            }
        }
    }
}