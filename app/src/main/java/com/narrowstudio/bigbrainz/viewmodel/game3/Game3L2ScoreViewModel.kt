package com.narrowstudio.bigbrainz.viewmodel.game3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.narrowstudio.bigbrainz.data.G2DBEntry
import com.narrowstudio.bigbrainz.data.G2Dao
import com.narrowstudio.bigbrainz.data.G3DBEntry
import com.narrowstudio.bigbrainz.data.G3Dao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

//for Dagger 2.31+
@HiltViewModel
class Game3L2ScoreViewModel @Inject constructor(
    private val g3Dao: G3Dao
): ViewModel() {

    var lastTime: Float = 0f
    var bestTime: Float = 0f
    var averageTime : Float = 0f

    var lastTimeString: String = ""
    var bestTimeString: String = ""
    var averageTimeString: String = ""
    val gameID: Int = 301

    val saves = g3Dao.getEntries().asLiveData()
    private var savesList: MutableList<G3DBEntry> = mutableListOf()


    private val scope = CoroutineScope(Dispatchers.Main)


    fun init(){
        filterEntries()
        getLastTimeFromDB()
        getBestTimeFromDB()
        getAverageTimeFromDB()
    }

    fun getTimesFromDB(){
        filterEntries()
        getLastTimeFromDB()
        getBestTimeFromDB()
        getAverageTimeFromDB()
    }

    private fun filterEntries(){
        if (saves.value?.size == null) return
        for (entry in saves.value!!){
            if (entry.gameID == gameID) {
                savesList.add(entry)
            }
        }
    }


    fun getLastTimeFromDB(){
        val entries: Int = savesList.size
        if (entries != 0) lastTime = (savesList[entries - 1].time.toFloat()) / 1000f
        formatLastTime()
    }

    private fun getBestTimeFromDB(){
        val entries: Int = savesList.size
        var best: Int = 0
        if (entries != 0){
            best = savesList[0].time
            for (i in 0 until entries){
                if (savesList[i].time < best) best = savesList[i].time
            }
            bestTime = best.toFloat() / 1000f
        }
        formatBestTime()
    }

    private fun getAverageTimeFromDB(){
        val entries: Int = savesList.size
        if (entries != 0){
            var sum: Long = 0
            //summing total average time of saved entries
            for (i in 0 until entries) {
                sum += savesList[i].time
            }
            averageTime = (sum.toFloat()) / (entries * 1000).toFloat()

        } else averageTime = 2137f
        formatAverageTime()
    }

    private fun formatLastTime(){
        // formatting the string to show 3 digits after dot
        lastTimeString = String.format("%.3f", lastTime)
    }

    private fun formatBestTime(){
        // formatting the string to show 3 digits after dot
        bestTimeString = String.format("%.3f", bestTime)
    }

    private fun formatAverageTime(){
        // formatting the string to show 3 digits after dot
        averageTimeString = String.format("%.3f", averageTime)
    }


}