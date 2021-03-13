package com.narrowstudio.bigbrainz.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class TimerViewModel : ViewModel(){

    private var startTime: Long = 0
    private var millisecondTime: Long = 0
    private var millisecondLD: MutableLiveData<Long> = MutableLiveData()
    private var isRunning: MutableLiveData<Boolean> = MutableLiveData()
    private var isRunningBoolean: Boolean = false
    private var blankTime: Int = 0
    private var handler = Handler(Looper.getMainLooper())


    private var runnable: Runnable = object:  Runnable {
        override fun run(){
            millisecondTime = System.currentTimeMillis() - startTime
            millisecondLD.postValue(millisecondTime)
            handler.postDelayed(this, 1)
        }
    }

    fun init(){
        isRunning.postValue(false)
        millisecondLD.postValue(0)
    }




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

    suspend fun startTimer(): Boolean{
        delay(blankTime.toLong())
        startTime = System.currentTimeMillis()
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, 1)
        isRunning.postValue(true)
        return true
    }

    fun stopTimer(): Long{
        handler.removeCallbacks(runnable)
        isRunning.postValue(false)
        return millisecondTime
    }

}