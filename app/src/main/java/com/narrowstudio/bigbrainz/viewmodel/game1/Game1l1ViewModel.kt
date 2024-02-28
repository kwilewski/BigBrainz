package com.narrowstudio.bigbrainz.viewmodel.game1

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.narrowstudio.bigbrainz.data.G1DBEntry
import com.narrowstudio.bigbrainz.data.G1Dao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.random.Random

@HiltViewModel
class Game1l1ViewModel @Inject constructor(
    private val g1Dao: G1Dao
) : ViewModel() {

    /**    ---------------------- gameState ----------------------------------
     *      0 - not running
     *      1 - displaying the color
     *      2 - displaying intercolor
     *      20 - displaying between final color and reading
     *      3 - reading the input
     *      4 - displaying wrong
     *      5 - displaying correct
     */
    val gameState: MutableLiveData<Int> = MutableLiveData()

    // time of starting the timer in millis
    private var startTime: Long = 0

    /**     ---------------------- buttonColor ----------------------------------
     *      0 - Red
     *      1 - Green
     *      2 - Blue
     *      3 - Yellow
     *      4 - Grey
     */

    // array with indexes of generated colors
    private var colorArray: ArrayList<Int> = ArrayList()

    var inputArray: ArrayList<Int> = ArrayList()

    val colorPressedLD:MutableLiveData<Boolean> = MutableLiveData()

    // sends info to fragment to open score fragment
    var openScore: MutableLiveData<Boolean> = MutableLiveData()

    // progress bar filler
    val progressBar: MutableLiveData<Int> = MutableLiveData()

    var buttonColor: Int = 4

    // length of the first challenge
    private val colorsAtTheBeginning = 3
    // length of level
    var colorsToBeShown = colorsAtTheBeginning
    // counter of already shown colors
    var counter = 0
    // increment of length at each level
    private val levelUp = 1

    // time for each color in millis
    private val displayTime = 600

    // time for intercolor in millis
    private val interColorTIme = 250

    // time for progress in millis
    private val progressTIme = 1500

    // time of displaying wrong message
    private val wrongTime = 2000

    // time of displaying correct message
    private val correctTime = 2000


    val saves = g1Dao.getEntries().asLiveData()


    private var handler = Handler(Looper.getMainLooper())

    private val scope = CoroutineScope(Dispatchers.Main)

    private var runnable: java.lang.Runnable = object: java.lang.Runnable {
        override fun run(){
            when (gameState.value){
                1 -> {
                    if (System.currentTimeMillis() >= startTime + displayTime){
                        if (counter < colorsToBeShown) {
                            colorToInter()
                        } else {
                            colorToProgress()
                        }
                    }
                }
                2 -> {
                    if (System.currentTimeMillis() >= startTime + interColorTIme){
                        interToColor()
                    }
                }
                20 -> {
                    if (System.currentTimeMillis() >= startTime + progressTIme){
                        progressToInput()
                    } else {
                        progressBar.postValue(progressBarCalc(System.currentTimeMillis() - startTime))
                    }
                }
                4 -> {
                    if (System.currentTimeMillis() >= startTime + wrongTime){
                        wrongToStart()
                    }
                }
                5 -> {
                    if (System.currentTimeMillis() >= startTime + correctTime){
                        correctToStart()
                    }
                }
            }

            handler.postDelayed(this, 1)
        }
    }




    fun init(){
        stopTimer()
        colorArray.clear()
        inputArray.clear()
        openScore.postValue(false)
        progressBar.postValue(0)
        counter = 0
        buttonColor = 4
    }

    fun mainButtonPressed(){
        when (gameState.value){
            0 -> {
                startGame()
            }
            1 -> {
                // do nothing
            }
            2 -> {
                // do nothing
            }
            3 -> {
                // do nothing
            }
            4 -> {
                // do nothing
            }
        }
    }

    fun colorButtonPressed(color: Int){
        inputArray.add(color)
        colorPressedLD.postValue(true)
        if (color != colorArray[inputArray.size-1]){
            restartGame()
            return
        }
        if (inputArray.size == colorsToBeShown){
            nextLevel()
            return
        }

    }

    private fun startGame(){
        Log.d("Game status", "Game started")
        interToColor()
        startTimer()
    }

    private fun restartGame(){
        // if any successful pattern, save
        if (colorsToBeShown == colorsAtTheBeginning) {
            gameState.postValue(4)
            colorsToBeShown = colorsAtTheBeginning
            buttonColor = 4
            startTime = System.currentTimeMillis()
            startTimer()
        } else {
            // if any successful pattern, open score
            insertNewEntry()
            openScore.postValue(true)
        }
    }

    // function for fragment to reset starting amount of tries
    fun restartStartingValue(){
        colorsToBeShown = colorsAtTheBeginning
    }

    private fun nextLevel(){
        colorsToBeShown += levelUp
        Log.d("Game status", "Level up. Current level: $colorsToBeShown")
        gameState.postValue(5)
        startTime = System.currentTimeMillis()
        startTimer()
    }

    // transition from state 1 to 2
    private fun colorToInter(){
        buttonColor = 4
        gameState.postValue(2)
        startTime = System.currentTimeMillis()
    }

    // transition from state 1 to 20
    private fun colorToProgress(){
        gameState.postValue(20)
        startTime = System.currentTimeMillis()
    }

    // transition from state 20 to 3
    private fun progressToInput(){
        stopTimer()
        gameState.postValue(3)
    }

    // transition from state 2 to 1
    private fun interToColor(){
        counter++
        colorArray.add(randomizeColor())
        buttonColor = colorArray.last()
        gameState.postValue(1)
        startTime = System.currentTimeMillis()
    }


    // transition from 4 to 0
    private fun wrongToStart(){
        restartStartingValue()
        init()
    }

    // transition from 5 to 0
    private fun correctToStart(){
        init()
        gameState.postValue(0)
    }


    private fun randomizeColor(): Int{
        return (0..3).random(Random(System.currentTimeMillis()))
    }

    // returning percentage for progressBar
    private fun progressBarCalc(delta: Long): Int {
        return ((delta.toFloat() / progressTIme.toFloat())*100).roundToInt()
    }

    private fun startTimer(){
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, 1)
    }

    private fun stopTimer(){
        handler.removeCallbacks(runnable)
        gameState.postValue(0)
    }

    //-------------------------------------------------- DB
    private fun insertNewEntry() {
        scope.launch {
            g1Dao.insert(G1DBEntry(101, colorsToBeShown - 1))
        }
    }

}