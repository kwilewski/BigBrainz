package com.narrowstudio.bigbrainz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.narrowstudio.bigbrainz.data.G2DBEntry
import com.narrowstudio.bigbrainz.data.G2Dao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

//for Dagger 2.31+
@HiltViewModel
class Game2ScoreViewModel @Inject constructor(
    private val g2Dao: G2Dao
): ViewModel() {

    var lastTime: Float = 0f
    var bestTime: Float = 0f
    var averageTime : Float = 0f

    var lastTimeString: String = ""
    var bestTimeString: String = ""
    var averageTimeString: String = ""

    val saves = g2Dao.getEntries().asLiveData()


    private val scope = CoroutineScope(Dispatchers.Main)


    fun init(){
        getLastTimeFromDB()
        getBestTimeFromDB()
        getAverageTimeFromDB()
    }

    fun getTimesFromDB(){
        getLastTimeFromDB()
        getBestTimeFromDB()
        getAverageTimeFromDB()
    }


    fun getLastTimeFromDB(){
        val entries: Int? = saves.value?.size
        if(entries != null) {
            lastTime = (saves.value!![entries - 1].averageTime.toFloat()) / 1000f
        }
        formatLastTime()
    }

    private fun getBestTimeFromDB(){
        val entries: Int? = saves.value?.size
        var best: Long = 0
        if (entries != null){
            best = saves.value!![0].averageTime
            for (i in 0 until entries!!){
                if (saves.value!![i].averageTime < best) best = saves.value!![i].averageTime
            }
        }
        bestTime = best.toFloat() / 1000f
        formatBestTime()
    }

    private fun getAverageTimeFromDB(){
        val entries: Int? = saves.value?.size
        if (entries != 0){
            var sum: Long = 0

            //summing total average time of saved entries
            if(entries != null && entries > 1) {
                for (i in 0 until entries){
                    sum += saves.value!![i].averageTime
                }
                averageTime = (sum.toFloat()) / (entries * 1000).toFloat()

            }

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