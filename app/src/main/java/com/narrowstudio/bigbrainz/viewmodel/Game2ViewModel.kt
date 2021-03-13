package com.narrowstudio.bigbrainz.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import kotlinx.coroutines.delay

class Game2ViewModel : ViewModel() {

    private var isGameRunning: Boolean = false;
    private var blankTime: Int = 0
    private lateinit var timeLD: LiveData<Long>

    private var startTime: Long = 0
    private var millisecondTime: Long = 0
    private var millisecondLD: MutableLiveData<Long> = MutableLiveData()
    private var isRunning: MutableLiveData<Boolean> = MutableLiveData()
    private var isRunningBoolean: Boolean = false
    private var handler = Handler(Looper.getMainLooper())


    private var runnable: Runnable = object:  Runnable {
        override fun run(){
            millisecondTime = System.currentTimeMillis() - startTime
            millisecondLD.postValue(millisecondTime)
            handler.postDelayed(this, 1)
        }
    }



    fun init(){
        isGameRunning = false
        isRunning.postValue(false)
        millisecondLD.postValue(0)
    }

    fun buttonPressed(){
        if (isGameRunning){ //handle click when game is running
            isGameRunning = false
            millisecondTime = stopTimer()
            millisecondLD.postValue(millisecondTime)
        } else {    //start the game - timer
            blankTime = randomizeTime()
            setBlankTime(blankTime)
            startTimer()
            isGameRunning = true
        }
    }


    fun getTimeInMillisLD(): MutableLiveData<Long>{
        return millisecondLD
    }

    fun getTimeAsString(): String{
        return millisecondTime.toString()
    }

    private fun randomizeTime(): Int {
        return (3000..8000).random()
    }




    //----------------------------------------------------------    Timer

    fun isTimerRunning(): LiveData<Boolean>{
        return isRunning
    }

    fun getTime(): LiveData<Long>{
        return millisecondLD
    }

    fun getTimeAsLong(): Long{
        return millisecondTime
    }


    fun setBlankTime(time: Int) {
        blankTime = time
    }

    suspend fun startTimer(){
        delay(blankTime.toLong())
        startTime = System.currentTimeMillis()
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, 1)
        isRunning.postValue(true
    }

    fun stopTimer(): Long{
        handler.removeCallbacks(runnable)
        isRunning.postValue(false)
        return millisecondTime
    }


}