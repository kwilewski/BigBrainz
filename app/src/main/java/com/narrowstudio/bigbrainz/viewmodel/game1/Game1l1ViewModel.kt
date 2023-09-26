package com.narrowstudio.bigbrainz.viewmodel.game1

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.narrowstudio.bigbrainz.data.G1DBEntry
import com.narrowstudio.bigbrainz.data.G1Dao
import com.narrowstudio.bigbrainz.data.G2DBEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class Game1l1ViewModel @Inject constructor(
    private val g1Dao: G1Dao
) : ViewModel() {

    /**    ---------------------- gameState ----------------------------------
     *      0 - not running
     *      1 - displaying the color
     *      2 - displaying intercolor
     *      3 - reading the input
     *      4 - displaying wrong
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
    // list of colors received from fragment
    val colorList: ArrayList<Int> = ArrayList()

    // array with indexes of generated colors
    private var colorArray: ArrayList<Int> = ArrayList()

    private var inputArray: ArrayList<Int> = ArrayList()

    var buttonColor: Int = 4

    // length of the first challenge
    private val colorsAtTheBeginning = 5
    // length of level
    private var colorsToBeShown = colorsAtTheBeginning
    // counter of already shown colors
    private var counter = 0
    // increment of length at each level
    private val levelUp = 1

    // time for each color in millis
    private val displayTime = 600

    // time for intercolor in millis
    private val interColorTIme = 250

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
                        if (counter < colorsToBeShown) {
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
        gameState.postValue(4)
        colorsToBeShown = colorsAtTheBeginning
        buttonColor = 4
        startTime = System.currentTimeMillis()
        startTimer()
    }

    private fun nextLevel(){
        colorsToBeShown += levelUp
        Log.d("Game status", "Level up. Current level: $colorsToBeShown")
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
            Log.d("Game status", "Game restarted")
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
            g1Dao.insert(G1DBEntry(101, 0L))
        }
    }

}