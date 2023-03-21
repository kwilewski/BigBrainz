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
    }

    private fun getBestTimeFromDB(){


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
                averageTime = (sum.toFloat()) / 1000f

            }

        } else averageTime = 2137f
    }


}