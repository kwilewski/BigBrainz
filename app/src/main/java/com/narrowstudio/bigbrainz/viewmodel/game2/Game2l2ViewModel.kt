package com.narrowstudio.bigbrainz.viewmodel.game2

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import com.narrowstudio.bigbrainz.R
import com.narrowstudio.bigbrainz.data.G2DBEntry
import com.narrowstudio.bigbrainz.data.G2Dao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.lang.Runnable
import javax.inject.Inject
import kotlin.random.Random

//for Dagger 2.31+
@HiltViewModel
class Game2l2ViewModel @Inject constructor(
    private val g2Dao: G2Dao
) : ViewModel() {


    var blankTime: Long = 0
    private var timeArray: ArrayList<Long> = ArrayList()

    private var startTime: Long = 0
    var millisecondTime: Long = 0
    var millisecondLD: MutableLiveData<Long> = MutableLiveData()
    var averageTime: MutableLiveData<Long> = MutableLiveData()
    private var isButtonClickable: MutableLiveData<Boolean> = MutableLiveData()
    var shouldGameBeRestarted: MutableLiveData<Boolean> = MutableLiveData()

    /**    ---------------------- gameState ----------------------------------
     *     0 - not running
     *     1 - running "red" - all but green
     *     2 - running green
     *     3 - displaying wrong
     */
    var gameState: MutableLiveData<Int> = MutableLiveData()

    // sends color to the fragment
    var currentButtonColor: MutableLiveData<Int> = MutableLiveData()
    private var colorCounter: Int = 0
    private val maxColorCounter: Int = 6
    lateinit var colorList: IntArray
    //val colorList: IntArray = intArrayOf(R.color.g2l2Green, R.color.g2l2Red, R.color.g2l2Blue, R.color.g2l2Yellow)

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
            if (System.currentTimeMillis() >= blankTime){
                when(gameState.value) {
                    2 -> {
                        millisecondTime = System.currentTimeMillis() - blankTime
                        millisecondLD.postValue(millisecondTime)
                    }
                    3 -> {
                        init()
                    }
                    else -> {
                        buttonColorHandler()
                    }
                }
            }
            handler.postDelayed(this, 1)
        }
    }



    fun init(){
        millisecondLD.postValue(0)
        resetValues()
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
                millisecondTime = 0
                colorCounter = 0
                isButtonClickable.postValue(false)
                buttonColorHandler()
                gameState.postValue(1)
            }
            1 -> {
                restartGame()
            }
            2 -> {
                stopTimer()
                isButtonClickable.postValue(false)
                handleTimeArray()
                gameState.postValue(0)
            }
        }

    }

    private fun buttonColorHandler(){
        // always start with red
        if(gameState.value == 0) {
            currentButtonColor.postValue(R.color.g2l2Red)
            colorCounter++
            shouldGameBeRestarted.postValue(false)
            return
        }

        // when restarting show grey
        if (gameState.value == 2){
            colorCounter = 0
            currentButtonColor.postValue(R.color.colorButtonWaiting)
            return
        }

        // if color counter reaches max show green
        if (colorCounter > maxColorCounter){
            currentButtonColor.postValue(colorList[0])
            gameState.postValue(2)
            isButtonClickable.postValue(true)
            return
        }

        // randomize new color
        val newColorIndex = randomizeColor()
        if (newColorIndex == 0){
            gameState.postValue(2)
            isButtonClickable.postValue(true)
        } else {
            //if the button is green, go without setting new threshold for timer, otherwise set new
            setBlankTime(randomizeTime())
        }
        colorCounter++
        currentButtonColor.postValue(colorList[newColorIndex])
    }

    private fun calculateTotalAverage(){
        val entries: Int? = saves.value?.size
        if (entries != 0){
            var sum: Long = 0

            //summing total average time of saved entries
            if((entries != null) && (entries > 1)) {
                for (i in 0 until entries){
                    sum += saves.value!![i].averageTime
                }
                totalAverage.postValue(sum.toFloat() / (entries * 1000).toFloat())
            }

        } else {
            totalAverage.postValue(0f)
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
        return (500..1500).random(Random(System.currentTimeMillis()))
    }

    private fun randomizeColor(): Int {
        return (colorList.indices).random(Random(System.currentTimeMillis()))
    }





    private fun setBlankTime(time: Int) {
        blankTime = System.currentTimeMillis() + time.toLong()
    }

    private fun setWrongTime(){
        blankTime = System.currentTimeMillis() + wrongTime
    }

    private fun startTimer(){
        //if(gameState.value != 0){
            isButtonClickable.postValue(true)
            startTime = System.currentTimeMillis()
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, 1)
        //}
    }

    private fun stopTimer(){
        handler.removeCallbacks(runnable)
        gameState.postValue(0)
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
        colorCounter = 0
        millisecondTime = 0
        millisecondLD.postValue(0)
        remainingMeasurements = repeats
    }


    // opening score after the game
    private fun openScore(){
        openScore.postValue(true)
    }



    //-------------------------------------------------- DB
    private fun insertNewEntry() {
        scope.launch {
            g2Dao.insert(G2DBEntry(202, averageTime.value!!))
        }
    }


}