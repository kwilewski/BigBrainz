package com.narrowstudio.bigbrainz.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.narrowstudio.bigbrainz.utility.MyTimer
import java.util.*
import java.util.logging.Handler

class Game2ViewModel : ViewModel() {

    private var isGameRunning: Boolean = false;
    private var timer: MyTimer = MyTimer()
    private var timeInMillis: Long = 0
    private val timeInMillisLD : MutableLiveData<Long> = MutableLiveData()
    private var blankTime: Int = 0
    private var isBlankTimeGone = false



    fun init(){
        isGameRunning = false
        timeInMillisLD.postValue(timeInMillis)
    }

    fun buttonPressed(){
        if (isGameRunning){ //handle click when game is running
            isGameRunning = false
            timeInMillis = timer.stopTimer()
            timeInMillisLD.postValue(timeInMillis)
        } else {    //start the game - timer
            blankTime = randomizeTime()
            timer.setBlankTime(blankTime)
            isBlankTimeGone = timer.startTimer()
            isGameRunning = true
        }
    }


    fun getTimeInMillisLD(): MutableLiveData<Long>{
        return timeInMillisLD
    }

    fun getTimeAsString(): String{
        return timeInMillis.toString()
    }

    private fun randomizeTime(): Int {
        return (3000..8000).random()
    }


}