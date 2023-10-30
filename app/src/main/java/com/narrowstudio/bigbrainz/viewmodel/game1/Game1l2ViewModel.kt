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
import kotlin.random.Random

@HiltViewModel
class Game1l2ViewModel @Inject constructor(
    private val g1Dao: G1Dao
) : ViewModel() {

    /**    ---------------------- gameState ----------------------------------
     *      0 - not running
     *      1 - displaying the code
     *      2 - displaying intercolor
     *      3 - reading the input
     *      4 - displaying wrong
     */
    val gameState: MutableLiveData<Int> = MutableLiveData()

    // time of starting the timer in millis
    private var startTime: Long = 0


    // sends info to fragment to open score fragment
    var openScore: MutableLiveData<Boolean> = MutableLiveData()

    // length of the first challenge
    private val lengthAtTheBeginning = 5
    // length of level
    var lengthToBeShown = lengthAtTheBeginning
    // increment of length at each level
    private val levelUp = 1

    // time for the code in millis
    private val displayTime = 2000

    // time for intercolor in millis
    private val interColorTIme = 1000

    // counting red screens after wrong click
    private var wrongColorCounter = 0
    private var wrongColorMaxCount = 3

    // time of displaying red after wrong click
    private val wrongColorRedTime = 300
    private val wrongColorGreyTime = 150

    val saves = g1Dao.getEntries().asLiveData()


    private var handler = Handler(Looper.getMainLooper())

    private val scope = CoroutineScope(Dispatchers.Main)

    private var runnable: java.lang.Runnable = object: java.lang.Runnable {
        override fun run(){
            when (gameState.value){
                1 -> {
                    if (System.currentTimeMillis() >= startTime + displayTime){
                        if (counter < lengthToBeShown) {
                            colorToInter()
                        } else {
                            colorToInput()
                        }
                    }
                }
                2 -> {
                    if (System.currentTimeMillis() >= startTime + interColorTIme){
                        interToColor()
                    }
                }
                4 -> { // timer for displaying red flashed
                    when (buttonColor){
                        0 -> {
                            if (System.currentTimeMillis() >= startTime + wrongColorRedTime){
                                wrongRed()
                            }
                        }
                        4 -> {
                            if (System.currentTimeMillis() >= startTime + wrongColorGreyTime){
                                wrongGrey()
                            }
                        }
                    }

                }
            }

            handler.postDelayed(this, 1)
        }
    }




    fun init(){
        stopTimer()
        buttonColor = 4
        colorArray.clear()
        inputArray.clear()
        openScore.postValue(false)
        counter = 0
        wrongColorCounter = 0
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
        if (inputArray.size == lengthToBeShown){
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
        if (lengthToBeShown == lengthAtTheBeginning) {
            gameState.postValue(4)
            lengthToBeShown = lengthAtTheBeginning
            buttonColor = 4
            startTime = System.currentTimeMillis()
            startTimer()
        } else {
            // if any successful pattern, open score
            insertNewEntry()
            openScore.postValue(true)
        }
    }

    private fun nextLevel(){
        lengthToBeShown += levelUp
        Log.d("Game status", "Level up. Current level: $lengthToBeShown")
        init()
    }

    // transition from state 1 to 2
    private fun colorToInter(){
        buttonColor = 4
        gameState.postValue(2)
        startTime = System.currentTimeMillis()
    }

    // transition from state 1 to 3
    private fun colorToInput(){
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

    private fun wrongRed(){
        if (++wrongColorCounter < wrongColorMaxCount){
            buttonColor = 4
            startTime = System.currentTimeMillis()
            gameState.postValue(4)
        } else {
            stopTimer()
            init()
        }
    }

    private fun wrongGrey(){
        startTime = System.currentTimeMillis()
        buttonColor = 0
        gameState.postValue(4)
    }

    private fun randomizeColor(): Int{
        return (0..3).random(Random(System.currentTimeMillis()))
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
            g1Dao.insert(G1DBEntry(101, lengthToBeShown - 1))
        }
    }

}