package com.narrowstudio.bigbrainz.viewmodel

import androidx.lifecycle.ViewModel
import com.narrowstudio.bigbrainz.utility.MyTimer
import java.util.*
import java.util.logging.Handler

class Game2ViewModel : ViewModel() {

    private var isGameRunning: Boolean = false;
    private var timer: MyTimer = MyTimer()
    private var timeInMillis: Long = 0



    fun init(){
        isGameRunning = false
    }

    fun buttonPressed(){
        if (isGameRunning){ //handle click when game is running
            isGameRunning = timer.isTimerRunning()
            timeInMillis = timer.stopTimer()
        } else {    //start the game - timer
            isGameRunning = timer.isTimerRunning()
            timer.startTimer()
        }
    }

    fun getTimeAsString(): String{
        return timeInMillis.toString()
    }


}