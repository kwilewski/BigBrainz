package com.narrowstudio.bigbrainz.viewmodel

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.narrowstudio.bigbrainz.data.G2DBEntry
import com.narrowstudio.bigbrainz.data.G2Dao
import kotlinx.coroutines.*
import java.lang.Runnable
import javax.inject.Inject

class Game2ViewModel @ViewModelInject constructor(
    private val g2Dao: G2Dao
) : ViewModel() {


    private var blankTime:Long = 0
    private lateinit var timeLD: LiveData<Long>
    private var timeArray: ArrayList<Long> = ArrayList()

    private var startTime: Long = 0
    private var millisecondTime: Long = 0
    private var millisecondLD: MutableLiveData<Long> = MutableLiveData()
    private var averageTime: MutableLiveData<Long> = MutableLiveData()
    private var isButtonClickable: MutableLiveData<Boolean> = MutableLiveData()
    private var shouldGameBeRestarted: MutableLiveData<Boolean> = MutableLiveData()
    private var gameState: MutableLiveData<Int> = MutableLiveData()
    /* ---------------------- gameState
    0 - not running
    1 - running red
    2 - running green
     */

    val saves = g2Dao.getEntries().asLiveData()
    var totalAverage: MutableLiveData<Float> = MutableLiveData()





    private var handler = Handler(Looper.getMainLooper())


    private val scope = CoroutineScope(Dispatchers.Main)

    private var runnable: Runnable = object:  Runnable {
        override fun run(){
            if(System.currentTimeMillis() >= blankTime) {
                millisecondTime = System.currentTimeMillis() - blankTime
                millisecondLD.postValue(millisecondTime)
                if (gameState.value != 2){
                    gameState.postValue(2)
                }
            } else {
                if (gameState.value != 1){
                    gameState.postValue(1)
                }
            }
            handler.postDelayed(this, 1)
        }
    }



    fun init(){
        millisecondLD.postValue(0)
        isButtonClickable.postValue(false)
        shouldGameBeRestarted.postValue(false)
        gameState.postValue(0)
        resetTimeArray()
        calculateTotalAverage()
    }

    fun buttonPressed(){
        when (gameState.value){
            0 -> {
                val randomTime = randomizeTime()
                setBlankTime(randomTime)
                scope.launch {
                    startTimer()
                }
                isButtonClickable.postValue(false)
                gameState.postValue(1)
            }
            1 -> {
                restartGame()
            }
            2 -> {
                millisecondTime = stopTimer()
                millisecondLD.postValue(millisecondTime)
                isButtonClickable.postValue(false)
                handleTimeArray()
                gameState.postValue(0)
            }
        }

    }

    private fun calculateTotalAverage(){
        val entries: Int? = saves.value?.size
        if (entries != 0){
            var sum: Long = 0

            //summing total average time of saved entries
            if(entries != null && entries > 1) {
                for (i in 0 until entries){
                    sum += saves.value!![i].averageTime
                }
                totalAverage.postValue(sum.toFloat() / (entries * 1000))
            }
        } else {
            totalAverage.postValue(2137f)
        }
    }

    private fun handleTimeArray(){
        // first add time to the array, then process
        timeArray.add(millisecondTime)
        val repeats: Int = 5
        if (timeArray.size == repeats) {
            var average: Long = 0
            for (i in 0 until repeats){
                average += timeArray[i]
                if (i == repeats-1){ //if last step, divide the sum by repeats
                    average /= repeats
                }
            }
            averageTime.postValue(average)
            insertNewEntry()
            resetTimeArray()
            calculateTotalAverage()
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





    //----------------------------------------------------------    Timer


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

    fun getTotalTimeAsString(): String{
        val time: Float = totalAverage.value!!.minus(totalAverage.value!!.rem(1000))
        //context.resources.getString(R.string.wda)
        return " "
    }



    private fun setBlankTime(time: Int) {
        blankTime = System.currentTimeMillis() + time.toLong()
    }

    private suspend fun startTimer(){
        //if(gameState.value != 0){
            isButtonClickable.postValue(true)
            startTime = System.currentTimeMillis()
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, 1)
        //}
    }

    private fun stopTimer(): Long{
        handler.removeCallbacks(runnable)
        gameState.postValue(0)
        return millisecondTime
    }

    private fun resetTimeArray(){
        for (i in 0 until timeArray.size) {
            timeArray.removeAt(0)
        }
    }

    private fun restartGame(){
        shouldGameBeRestarted.postValue(true)
        resetValues()
    }

    private fun resetValues(){
        handler.removeCallbacks(runnable)
        isButtonClickable.postValue(false)
        gameState.postValue(0)
        resetTimeArray()
    }



    //-------------------------------------------------- DB
    private fun insertNewEntry() {
        scope.launch {
            g2Dao.insert(G2DBEntry(201, averageTime.value!!))
        }
    }


}