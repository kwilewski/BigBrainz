package com.narrowstudio.bigbrainz.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import kotlinx.coroutines.*
import java.lang.Runnable

class Game2ViewModel : ViewModel() {

    private var isGameRunning: Boolean = false;
    private var blankTime:Long = 0
    private lateinit var timeLD: LiveData<Long>
    private var timeArray: ArrayList<Long> = ArrayList()

    private var startTime: Long = 0
    private var millisecondTime: Long = 0
    private var millisecondLD: MutableLiveData<Long> = MutableLiveData()
    private var averageTime: MutableLiveData<Long> = MutableLiveData()
    private var isRunning: MutableLiveData<Boolean> = MutableLiveData()
    private var isButtonClickable: MutableLiveData<Boolean> = MutableLiveData()
    private var shouldGameBeRestarted: MutableLiveData<Boolean> = MutableLiveData()
    private var gameState: MutableLiveData<Int> = MutableLiveData()
    /* ---------------------- gameState
    0 - not running
    1 - running red
    2 - running green
     */

    private var isRunningBoolean: Boolean = false
    private var handler = Handler(Looper.getMainLooper())


    private val scope = CoroutineScope(Dispatchers.Main)

    private var runnable: Runnable = object:  Runnable {
        override fun run(){
            if(System.currentTimeMillis() >= blankTime) {
                millisecondTime = System.currentTimeMillis() - blankTime
                millisecondLD.postValue(millisecondTime)
                if (gameState.value!! != 2){
                    gameState.postValue(2)
                }
            } else {
                if (gameState.value!! != 1){
                    gameState.postValue(1)
                }
            }
            handler.postDelayed(this, 1)
        }
    }



    fun init(){
        isGameRunning = false
        isRunning.postValue(false)
        millisecondLD.postValue(0)
        isButtonClickable.postValue(false)
        shouldGameBeRestarted.postValue(false)
        gameState.postValue(0)
        resetTimeArray()
    }

    fun buttonPressed(){
        if (isGameRunning){ //handle click when game is running
            isGameRunning = false
            millisecondTime = stopTimer()
            if(millisecondTime > 0) {
                millisecondLD.postValue(millisecondTime)
                isButtonClickable.postValue(false)
                handleTimeArray()
            } else {
                restartGame()
            }
            gameState.postValue(0)
        } else {    //start the game - timer
            val randomTime = randomizeTime()
            setBlankTime(randomTime)
            scope.launch {
                startTimer()
            }
            isGameRunning = true
            isButtonClickable.postValue(false)
            gameState.postValue(1)
        }
    }

    private fun handleTimeArray(){
        // first add time to the array, then process
        timeArray.add(millisecondTime)
        val repeats: Int = 5
        var average: Long = 0
        if (timeArray.size == repeats) {
            for (i in 0 until repeats){
                average += timeArray[i]
                if (i == repeats-1){ //if last step, divide the sum by repeats
                    average /= repeats
                }
            }
            averageTime.postValue(average)
            resetTimeArray()
            //ToDO show medium time on the screen
        }

    }

    fun getTimeInMillisLD(): MutableLiveData<Long>{
        return millisecondLD
    }

    fun getTimeAsString(): String{
        val time: Float = millisecondTime.toFloat()/1000
        return time.toString() + "s"
    }

    fun getAverageTimeAsString(): String{
        val time: Float = averageTime.value!!.toFloat()/1000
        return time.toString() + "s"
    }

    private fun randomizeTime(): Int {
        return (2000..6000).random()
    }

    fun getIsGameRunning(): Boolean{
        return isGameRunning
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

    fun getIsButtonClickable(): LiveData<Boolean> {
        return isButtonClickable
    }

    fun getAverageTime(): LiveData<Long> {
        return averageTime
    }

    fun getGameState(): LiveData<Int> {
        return gameState
    }

    fun getShouldGameBeRestarted(): LiveData<Boolean>{
        return shouldGameBeRestarted
    }



    private fun setBlankTime(time: Int) {
        blankTime = System.currentTimeMillis() + time.toLong()
    }

    private suspend fun startTimer(){
        //delay(blankTime.toLong())
        if(isGameRunning){
            isButtonClickable.postValue(true)
            startTime = System.currentTimeMillis()
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, 1)
            isRunning.postValue(true)
        }
    }

    private fun stopTimer(): Long{
        handler.removeCallbacks(runnable)
        isRunning.postValue(false)
        return millisecondTime
    }

    private fun resetTimeArray(){
        for (i in 0 until timeArray.size-1) {
            timeArray.removeAt(0)
        }
    }

    private fun restartGame(){
        shouldGameBeRestarted.postValue(true)
        resetValues()
    }

    private fun resetValues(){
        isGameRunning = false
        isRunning.postValue(false)
        isButtonClickable.postValue(false)
        shouldGameBeRestarted.postValue(false)
        gameState.postValue(0)
        resetTimeArray()
    }


}