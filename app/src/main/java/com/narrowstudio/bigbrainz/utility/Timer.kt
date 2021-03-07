package com.narrowstudio.bigbrainz.utility

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.delay
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class MyTimer() {

    private var startTime: Long = 0
    private var millisecondTime: Long = 0
    private var isRunning: Boolean = false
    private var blankTime: Int = 0
    private var handler = Handler(Looper.getMainLooper())


    private var runnable: Runnable = object:  Runnable {
        override fun run(){
            millisecondTime = System.currentTimeMillis() - startTime
            handler.postDelayed(this, 1)
        }
    }





    fun isTimerRunning(): Boolean{
        return isRunning
    }

    fun getTime(): Long{
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
        isRunning = true
        return true
    }

    fun stopTimer(): Long{
        handler.removeCallbacks(runnable)
        isRunning = false
        return millisecondTime
    }

}