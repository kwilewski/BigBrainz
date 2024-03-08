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
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random

@HiltViewModel
class Game1l3ViewModel @Inject constructor(
    private val g1Dao: G1Dao
) : ViewModel() {

    /**    ---------------------- gameState ----------------------------------
     *      0 - not running
     *      1 - displaying the numbers
     *      2 - displaying intercolor
     *      3 - reading the input
     *      4 - displaying wrong
     *      5 - displaying correct
     */
    val gameState: MutableLiveData<Int> = MutableLiveData()

    // list of indexes of buttons
    var indexList: List<Int> = ArrayList()

    // list of correctly pressed tiles to be displayed
    val correctTiles: MutableLiveData<Int> = MutableLiveData()

    // time of starting the timer in millis
    private var startTime: Long = 0

    // sends info to fragment to open score fragment
    var openScore: MutableLiveData<Boolean> = MutableLiveData()

    // progress bar filler
    val progressBar: MutableLiveData<Int> = MutableLiveData()

    // length of the first challenge
    private val levelAtTheBeginning = 3

    // length of level
    var levelToBeShown = levelAtTheBeginning

    // increment of length at each level
    private val levelUp = 1

    // time for the table in millis
    private val displayTime = 3000

    // time for intercolor in millis
    private val interColorTIme = 1500

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
                        codeToInter()
                    }
                }
                2 -> {
                    if (System.currentTimeMillis() >= startTime + interColorTIme){
                        interToInput()
                    } else {
                        progressBar.postValue(progressBarCalc(System.currentTimeMillis() - startTime))
                    }
                }
                4 -> {
                    if (System.currentTimeMillis() >= startTime + wrongTime){
                        wrongToHandler()
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
        levelToBeShown = levelAtTheBeginning
        openScore.postValue(false)
        correctTiles.postValue(0)
        progressBar.postValue(0)
        gameState.postValue(0)
    }

    fun mainButtonPressed(){
        when (gameState.value){
            0 -> {
                startGame()
            }
            else -> {
                // do nothing
            }
        }
    }

    fun tileButtonPressed(id: Int){
        if (gameState.value != 3) return
        val current: Int? = correctTiles.value
        // if the value is equal to the one from array, add +1 to counter
        if (current?.let { indexList[it] } == id){
            correctTiles.postValue(current + 1)
        } else {
            inputToWrong()
        }
        // if new current is equal to level, go to next level
        if (current != null) {
            if (current + 1 == levelToBeShown){
                inputToCorrect()
            }
        }

    }



    private fun startGame(){
        Log.d("Game status", "Game started")
        randomizeIndexes()
        gameState.postValue(1)
        startTime = System.currentTimeMillis()
        startTimer()
    }

    private fun restartGame(){
        // if any successful pattern, save
        if (levelToBeShown == levelAtTheBeginning) {
            gameState.postValue(4)
            levelToBeShown = levelAtTheBeginning
            startTime = System.currentTimeMillis()
            startTimer()
        } else {
            // if any successful pattern, open score
            insertNewEntry()
            gameState.postValue(4)
            startTime = System.currentTimeMillis()
            startTimer()
        }
    }

    private fun nextLevel(){
        levelToBeShown += levelUp
        Log.d("Game status", "Level up. Current level: $levelToBeShown")
        if (levelToBeShown > 25) {
            insertNewEntry()
            openScore.postValue(true)
        }
        gameState.postValue(5)
        correctTiles.postValue(0)
        startTime = System.currentTimeMillis()
        startTimer()
    }

    // transition from state 1 to 2
    private fun codeToInter(){
        gameState.postValue(2)
        startTime = System.currentTimeMillis()
        startTimer()
    }

    // transition from state 2 to 3
    private fun interToInput(){
        stopTimer()
        gameState.postValue(3)
    }

    // transition from 3 to 4
    private fun inputToWrong(){
        restartGame()
    }

    // transition from 3 to 5
    private fun inputToCorrect(){
        nextLevel()
    }

    // handles if the VM should open score or restart the game
    private fun wrongToHandler(){
        if (levelToBeShown == levelAtTheBeginning){
            wrongToStart()
        } else {
            wrongToOpenScore()
        }
    }

    // transition from 4 to 0
    // if the wasn't correct input
    private fun wrongToStart(){
        init()
    }

    // when any input was correct
    private fun wrongToOpenScore(){
        openScore.postValue(true)
    }


    // transition from 5 to 0
    private fun correctToStart(){
        stopTimer()
        gameState.postValue(0)
    }

    private fun randomizeIndexes(){
        indexList = (1 .. 25).shuffled().take(levelToBeShown)
    }

    // returning percentage for progressBar
    private fun progressBarCalc(delta: Long): Int {
        return ((delta.toFloat() / interColorTIme.toFloat())*100).roundToInt()
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
            g1Dao.insert(G1DBEntry(103, levelToBeShown - 1))
        }
    }

}