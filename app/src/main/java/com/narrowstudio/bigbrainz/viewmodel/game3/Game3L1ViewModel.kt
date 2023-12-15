package com.narrowstudio.bigbrainz.viewmodel.game3

import android.os.Debug
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.narrowstudio.bigbrainz.data.G1DBEntry
import com.narrowstudio.bigbrainz.data.G3DBEntry
import com.narrowstudio.bigbrainz.data.G3Dao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class Game3L1ViewModel @Inject constructor(
    private val g3Dao: G3Dao
) : ViewModel(){

    /**    ---------------------- gameState ----------------------------------
     *      0 - not running
     *      1 - displaying the target
     *      10 - displaying blank screen
     *      2 - displaying wrong
     */
    val gameState: MutableLiveData<Int> = MutableLiveData()

    // time of starting the timer in millis
    private var startTime: Long = 0

    // sends info to fragment to open score fragment
    val openScore: MutableLiveData<Boolean> = MutableLiveData()

    // amount of repeats
    val repeats = 10

    // counter of already pressed
    var counter = 0

    // min time of waiting for a target in millis
    private val minWaitTime: Long = 200

    // max time of waiting for a target in millis
    private val maxWaitTime: Long = 1500

    // random wait time for the target to appear
    private var waitTime = 0

    // time of displaying wrong message
    private val wrongTime = 2000

    // border where the target can't be placed in percent
    private val border = 5

    // position of the target
    val targetPosition: ArrayList<Float> = ArrayList()

    // storing time results of the test
    private var resultArray: ArrayList<Long> = ArrayList()

    val saves = g3Dao.getEntries().asLiveData()

    private var handler = Handler(Looper.getMainLooper())

    private val scope = CoroutineScope(Dispatchers.Main)

    private var runnable: java.lang.Runnable = object: java.lang.Runnable {
        override fun run(){
            when (gameState.value){
                10 -> {
                    if (System.currentTimeMillis() >= startTime + waitTime){
                        showTarget()
                    }
            }
                2 -> {
                    if (System.currentTimeMillis() >= startTime + wrongTime){
                        wrongToStart()
                    }
                }
            }

            handler.postDelayed(this, 1)
        }
    }


    fun init(){
        stopTimer()
        openScore.postValue(false)
        gameState.postValue(0)
    }


    private fun startGame(){
        resultArray.clear()
        targetPosition.clear()
        startTime = System.currentTimeMillis()
        waitTime = randomizeWaitTime()
        gameState.postValue(10)
        startTimer()
    }


    private fun restartGame(){
        gameState.postValue(4)
        startTime = System.currentTimeMillis()
        startTimer()
    }

    private fun showTarget(){
        targetPosition.clear()
        targetPosition.add(randomizeCoordinate())
        targetPosition.add(randomizeCoordinate())
        val mess = "x: " + targetPosition[0] + " y: " +  targetPosition[1]
        Log.d("Game 3 position", mess)
        gameState.postValue(1)
        startTime = System.currentTimeMillis()
    }

    fun startButtonClicked(){
        startGame()
    }

    fun targetClicked(){
        val time = System.currentTimeMillis() - startTime
        stopTimer()
        resultArray.add(time)
        if (resultArray.size >= repeats){

        }
    }

    private fun displayBlank(){

    }

    private fun randomizeWaitTime(): Int{
        return (minWaitTime .. maxWaitTime).random(Random(System.currentTimeMillis())).toInt()
    }

    private fun randomizeCoordinate(): Float{
        // returning a Float from 0 to 1 - position on the layout
        return (border .. 100 - border).random(Random(System.nanoTime())).toFloat() * 0.01F
    }


    // transition from 3 to 4
    private fun inputToWrong(){
        restartGame()
    }

    // transition from 4 to 0
    private fun wrongToStart(){
        init()
    }

    private fun saveAndOpenScore(){

    }

    private fun startTimer(){
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, 1)
    }

    private fun stopTimer(){
        handler.removeCallbacks(runnable)
    }


    //-------------------------------------------------- DB
    private fun insertNewEntry() {
        scope.launch {
            g3Dao.insert(G3DBEntry(301, 2137, 420, 2137))
        }
    }

}