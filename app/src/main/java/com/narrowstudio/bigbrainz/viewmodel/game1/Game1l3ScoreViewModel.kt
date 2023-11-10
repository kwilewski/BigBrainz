package com.narrowstudio.bigbrainz.viewmodel.game1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.narrowstudio.bigbrainz.data.G1DBEntry
import com.narrowstudio.bigbrainz.data.G1Dao
import com.narrowstudio.bigbrainz.data.G2DBEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class Game1l3ScoreViewModel @Inject constructor(
    private val g1Dao: G1Dao
) : ViewModel() {
    var lastScore: Int = 0
    var bestScore: Int = 0

    val gameID: Int = 103

    val saves = g1Dao.getEntries().asLiveData()
    private var savesList: MutableList<G1DBEntry> = mutableListOf()


    private val scope = CoroutineScope(Dispatchers.Main)


    fun init(){
        filterEntries()
        getLastScoreFromDB()
        getBestScoreFromDB()
    }

    fun getScoresFromDB(){
        filterEntries()
        getLastScoreFromDB()
        getBestScoreFromDB()
    }

    private fun filterEntries(){
        if (saves.value?.size == null) return
        for (entry in saves.value!!){
            if (entry.gameID == gameID) {
                savesList.add(entry)
            }
        }
    }


    private fun getLastScoreFromDB(){
        val entries: Int = savesList.size
        if (entries != 0) lastScore = savesList[entries - 1].score
    }

    private fun getBestScoreFromDB(){
        val entries: Int = savesList.size
        var best: Int = 0
        if (entries != 0){
            best = savesList[0].score
            for (i in 0 until entries){
                if (savesList[i].score > best) best = savesList[i].score
            }
            bestScore = best
        }
    }


}