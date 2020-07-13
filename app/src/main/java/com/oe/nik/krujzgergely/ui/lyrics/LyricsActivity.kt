package com.oe.nik.krujzgergely.ui.lyrics

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.oe.nik.krujzgergely.R
import com.oe.nik.krujzgergely.models.LyricsModel
import com.oe.nik.krujzgergely.models.enums.TypeOfTheRecycler
import com.oe.nik.krujzgergely.ui.createlyricsitem.CreateLyricsActivity
import com.oe.nik.krujzgergely.ui.lyricsItem.LyricsItemActiviy
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView


class LyricsActivity : AppCompatActivity(), IonLyricsSelected {

    private lateinit var lyricsViewModel : LyricsActivityViewModel

    private lateinit var allLyricsActivityAdapter : LyricsActivityAdapter
    private lateinit var favoriteLyricsActivityAdapter : LyricsActivityAdapter
    private lateinit var jazzLyricsActivityAdapter : LyricsActivityAdapter
    private lateinit var hiphopLyricsActivityAdapter : LyricsActivityAdapter
    private lateinit var rockLyricsActivityAdapter : LyricsActivityAdapter
    private lateinit var metalLyricsActivityAdapter : LyricsActivityAdapter
    private lateinit var punkLyricsActivityAdapter : LyricsActivityAdapter
    private lateinit var popLyricsActivityAdapter : LyricsActivityAdapter
    private lateinit var countryLyricsActivityAdapter : LyricsActivityAdapter
    private lateinit var operaLyricsActivityAdapter : LyricsActivityAdapter

    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyricses)

        setAdapters()
        setLyricsActivityViewModel()

        loadLyricsMultiSnapRecyclerView(TypeOfTheRecycler.ALL)
        loadLyricsMultiSnapRecyclerView(TypeOfTheRecycler.FAVORITE)
        loadLyricsMultiSnapRecyclerView(TypeOfTheRecycler.JAZZ)
        loadLyricsMultiSnapRecyclerView(TypeOfTheRecycler.HIPHOP)
        loadLyricsMultiSnapRecyclerView(TypeOfTheRecycler.ROCK)
        loadLyricsMultiSnapRecyclerView(TypeOfTheRecycler.METAL)
        loadLyricsMultiSnapRecyclerView(TypeOfTheRecycler.PUNK)
        loadLyricsMultiSnapRecyclerView(TypeOfTheRecycler.POP)
        loadLyricsMultiSnapRecyclerView(TypeOfTheRecycler.COUNTRY)
        loadLyricsMultiSnapRecyclerView(TypeOfTheRecycler.OPERA)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.lyrics_activity_menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId)
        {
            R.id.CreateNewLyrics -> startCreateLyricsActivity()
            R.id.SearchField -> searchBetweenAllLyrics(item)
            else -> super.onOptionsItemSelected(item)
        }

    private fun loadLyricsMultiSnapRecyclerView(typeOfTheRecycler: TypeOfTheRecycler)
    {
        linearLayoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        when(typeOfTheRecycler)
        {
            TypeOfTheRecycler.ALL -> startLoadingTheLyrics(R.id.all_recycler_view,typeOfTheRecycler)
            TypeOfTheRecycler.FAVORITE -> startLoadingTheLyrics(R.id.favourite_recycler_view,typeOfTheRecycler)
            TypeOfTheRecycler.JAZZ -> startLoadingTheLyrics(R.id.jazz_recycler_view,typeOfTheRecycler)
            TypeOfTheRecycler.HIPHOP -> startLoadingTheLyrics(R.id.hiphop_recycler_view,typeOfTheRecycler)
            TypeOfTheRecycler.ROCK -> startLoadingTheLyrics(R.id.rock_recycler_view,typeOfTheRecycler)
            TypeOfTheRecycler.METAL -> startLoadingTheLyrics(R.id.metal_recycler_view,typeOfTheRecycler)
            TypeOfTheRecycler.PUNK -> startLoadingTheLyrics(R.id.punk_recycler_view,typeOfTheRecycler)
            TypeOfTheRecycler.POP -> startLoadingTheLyrics(R.id.pop_recycler_view,typeOfTheRecycler)
            TypeOfTheRecycler.COUNTRY -> startLoadingTheLyrics(R.id.country_recycler_view,typeOfTheRecycler)
            TypeOfTheRecycler.OPERA -> startLoadingTheLyrics(R.id.opera_recycler_view,typeOfTheRecycler)
        }
    }

    private fun startLoadingTheLyrics(recyclerId : Int, typeOfTheRecycler: TypeOfTheRecycler)
    {
        val lyricsRecyclerView = findViewById<MultiSnapRecyclerView>(recyclerId)

        when(typeOfTheRecycler)
        {
            TypeOfTheRecycler.ALL -> setRecyclerViewComponents(lyricsRecyclerView,typeOfTheRecycler,allLyricsActivityAdapter)
            TypeOfTheRecycler.FAVORITE -> setRecyclerViewComponents(lyricsRecyclerView,typeOfTheRecycler,favoriteLyricsActivityAdapter)
            TypeOfTheRecycler.JAZZ -> setRecyclerViewComponents(lyricsRecyclerView,typeOfTheRecycler,jazzLyricsActivityAdapter)
            TypeOfTheRecycler.HIPHOP -> setRecyclerViewComponents(lyricsRecyclerView,typeOfTheRecycler,hiphopLyricsActivityAdapter)
            TypeOfTheRecycler.ROCK -> setRecyclerViewComponents(lyricsRecyclerView,typeOfTheRecycler,rockLyricsActivityAdapter)
            TypeOfTheRecycler.METAL -> setRecyclerViewComponents(lyricsRecyclerView,typeOfTheRecycler,metalLyricsActivityAdapter)
            TypeOfTheRecycler.PUNK -> setRecyclerViewComponents(lyricsRecyclerView,typeOfTheRecycler,punkLyricsActivityAdapter)
            TypeOfTheRecycler.POP -> setRecyclerViewComponents(lyricsRecyclerView,typeOfTheRecycler,popLyricsActivityAdapter)
            TypeOfTheRecycler.COUNTRY -> setRecyclerViewComponents(lyricsRecyclerView,typeOfTheRecycler,countryLyricsActivityAdapter)
            TypeOfTheRecycler.OPERA -> setRecyclerViewComponents(lyricsRecyclerView,typeOfTheRecycler,operaLyricsActivityAdapter)
        }

    }

    private fun setRecyclerViewComponents(currentRecyclerView: MultiSnapRecyclerView,
                                          typeOfTheRecycler: TypeOfTheRecycler,
                                          lyricsActivityAdapter : LyricsActivityAdapter)
    {
        currentRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = lyricsActivityAdapter }

        getDataFromTheViewModel(typeOfTheRecycler, lyricsActivityAdapter)
    }

    private fun getDataFromTheViewModel(typeOfTheRecycler : TypeOfTheRecycler,lyricsActivityAdapter : LyricsActivityAdapter)
    {
        val currentLyricsLiveData  : LiveData<List<LyricsModel>> = when(typeOfTheRecycler)
        {
            TypeOfTheRecycler.ALL -> lyricsViewModel.getAllLyricsFromLocalDB()
            TypeOfTheRecycler.FAVORITE -> lyricsViewModel.getFavoriteLyricsFromLocalDB()
            TypeOfTheRecycler.JAZZ -> lyricsViewModel.getJazzLyricsFromLocalDB()
            TypeOfTheRecycler.HIPHOP -> lyricsViewModel.getHipHopLyricsFromLocalDB()
            TypeOfTheRecycler.ROCK -> lyricsViewModel.getRockLyricsFromLocalDB()
            TypeOfTheRecycler.METAL -> lyricsViewModel.getMetalLyricsFromLocalDB()
            TypeOfTheRecycler.PUNK -> lyricsViewModel.getPunkLyricsFromLocalDB()
            TypeOfTheRecycler.POP -> lyricsViewModel.getPopLyricsFromLocalDB()
            TypeOfTheRecycler.COUNTRY -> lyricsViewModel.getCountryLyricsFromLocalDB()
            TypeOfTheRecycler.OPERA -> lyricsViewModel.getOperaLyricsFromLocalDB()
        }
        getLyricsFromDb(currentLyricsLiveData,lyricsActivityAdapter)
    }

    private fun setAdapters()
    {
        allLyricsActivityAdapter = LyricsActivityAdapter(this, mutableListOf())
        favoriteLyricsActivityAdapter = LyricsActivityAdapter(this, mutableListOf())
        jazzLyricsActivityAdapter = LyricsActivityAdapter(this, mutableListOf())
        hiphopLyricsActivityAdapter = LyricsActivityAdapter(this, mutableListOf())
        rockLyricsActivityAdapter = LyricsActivityAdapter(this, mutableListOf())
        metalLyricsActivityAdapter = LyricsActivityAdapter(this, mutableListOf())
        punkLyricsActivityAdapter = LyricsActivityAdapter(this, mutableListOf())
        popLyricsActivityAdapter = LyricsActivityAdapter(this, mutableListOf())
        countryLyricsActivityAdapter = LyricsActivityAdapter(this, mutableListOf())
        operaLyricsActivityAdapter = LyricsActivityAdapter(this, mutableListOf())
    }

    private fun searchBetweenAllLyrics(item: MenuItem) : Boolean
    {
        val searchView = item.actionView as SearchView
        
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query: String?): Boolean { return true }

            override fun onQueryTextChange(searchText: String?): Boolean
            {
                searchIfTheSearchFieldIsNotEmpty(searchText!!)
                return true
            }
        })
        return true
    }

    private fun startCreateLyricsActivity() : Boolean
    {
        startActivity(Intent(this,CreateLyricsActivity::class.java))
        return true
    }

    private fun setLyricsActivityViewModel()
    {
        lyricsViewModel = ViewModelProvider(this).get(LyricsActivityViewModel::class.java)
    }

    private fun getLyricsFromDb(lyricsLiveData : LiveData<List<LyricsModel>>, adapter: LyricsActivityAdapter)
    {
        lyricsLiveData.observe(this, Observer {lyricsList -> adapter.swapData(lyricsList) })
    }

    private fun searchIfTheSearchFieldIsNotEmpty(searchText : String)
    {
        when(searchText.isNotEmpty())
        {
            true -> allLyricsActivityAdapter.getSearchedDataFromTheList(searchText)
            false -> allLyricsActivityAdapter.noItemFoundInLyricsListWhenSearched()
        }
    }

    override fun onLyricsSelected() { startActivity( Intent(this, LyricsItemActiviy::class.java)) }
}