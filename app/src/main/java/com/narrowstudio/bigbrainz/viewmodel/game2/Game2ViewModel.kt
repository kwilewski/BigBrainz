package com.narrowstudio.bigbrainz.viewmodel.game2

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import com.narrowstudio.bigbrainz.data.G2DBEntry
import com.narrowstudio.bigbrainz.data.G2Dao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.lang.Runnable
import javax.inject.Inject
import kotlin.random.Random

//for Dagger 2.31+
@HiltViewModel
class Game2ViewModel @Inject constructor(
    private val g2Dao: G2Dao
) : ViewModel() {


    private var blankTime:Long = 0
    private var timeArray: ArrayList<Long> = ArrayList()

    private var startTime: Long = 0
    private var millisecondTime: Long = 0
    private var millisecondLD: MutableLiveData<Long> = MutableLiveData()
    var averageTime: MutableLiveData<Long> = MutableLiveData()
    private var isButtonClickable: MutableLiveData<Boolean> = MutableLiveData()
    private var shouldGameBeRestarted: MutableLiveData<Boolean> = MutableLiveData()

/**    ---------------------- gameState ----------------------------------
 *    0 - not running
 *    1 - running red
 *    2 - running green
 *    3 - displaying wrong
**/
    private var gameState: MutableLiveData<Int> = MutableLiveData()

    // sends info to fragment to open score fragment
    var openScore: MutableLiveData<Boolean> = MutableLiveData()

    // nr of repetitions before score is opened
    private val repeats: Int = 5

    // time of wrong message
    private val wrongTime: Long = 1500

    // nr of measurements done
    var remainingMeasurements: Int = repeats




    val saves = g2Dao.getEntries().asLiveData()
    private var totalAverage: MutableLiveData<Float> = MutableLiveData()



    private var handler = Handler(Looper.getMainLooper())

    private val scope = CoroutineScope(Dispatchers.Main)

    private var runnable: Runnable = object:  Runnable {
        override fun run(){
            if(System.currentTimeMillis() >= blankTime) {
                millisecondTime = System.currentTimeMillis() - blankTime
                millisecondLD.postValue(millisecondTime)
                if (gameState.value == 1){
                    gameState.postValue(2)
                }
                if (gameState.value == 3){
                    init()
                }
            }
            handler.postDelayed(this, 1)
        }
    }



    fun init(){
        millisecondLD.postValue(0)
        resetValues()
        calculateTotalAverage()
    }

    fun buttonPressed(){
        when (gameState.value){
            0 -> {
                // starting the counter
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
        if (entries != 0 && entries != null){
            var sum: Long = 0
            //summing total average time of saved entries
            if(entries > 1) {
                for (i in 0 until entries){
                    sum += saves.value!![i].averageTime
                }
                totalAverage.postValue(sum.toFloat() / (entries * 1000).toFloat())
            }
        } else {
            totalAverage.postValue(2137f)
        }
    }


    // this function counts repeats and opens the score after 5
    private fun handleTimeArray(){
        // first add time to the array, then process
        timeArray.add(millisecondTime)
        // update remainingMeasurements for fragment
        remainingMeasurements = repeats - timeArray.size
        if (timeArray.size == repeats) {
            var average: Long = 0
            // counting average time
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
            gameState.postValue(0)
            openScore()
        }

    }

    fun getTimeInMillisLD(): MutableLiveData<Long>{
        return millisecondLD
    }


    private fun randomizeTime(): Int {
        return (2000..6000).random(Random(System.currentTimeMillis()))
    }





    //----------------------------------------------------------    Timer


    fun getTime(): LiveData<Long>{
        return millisecondLD
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

    private fun setWrongTime(){
        blankTime = System.currentTimeMillis() + wrongTime
    }

    private fun startTimer(){
            isButtonClickable.postValue(true)
            startTime = System.currentTimeMillis()
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, 1)
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
        startTimer()
        setWrongTime()
        gameState.postValue(3)
        isButtonClickable.postValue(false)
        shouldGameBeRestarted.postValue(true)
    }

    private fun resetValues(){
        handler.removeCallbacks(runnable)
        isButtonClickable.postValue(false)
        gameState.postValue(0)
        resetTimeArray()
        remainingMeasurements = repeats
    }


    // opening score after the game
    private fun openScore(){
        openScore.postValue(true)
    }



    //-------------------------------------------------- DB
    private fun insertNewEntry() {
        scope.launch {
            g2Dao.insert(G2DBEntry(201, averageTime.value!!))
        }
    }


}