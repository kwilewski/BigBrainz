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
     *      5 - displaying next level
     */
    val gameState: MutableLiveData<Int> = MutableLiveData()

    // time of starting the timer in millis
    private var startTime: Long = 0

    // random code shown
    val code: MutableLiveData<Int> = MutableLiveData()


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
                    }
                }
                4 -> {
                    if (System.currentTimeMillis() >= startTime + wrongTime){

                    }
                }
                5 -> {
                    if (System.currentTimeMillis() >= startTime + wrongTime){

                    }
                }
            }

            handler.postDelayed(this, 1)
        }
    }




    fun init(){
        stopTimer()
        openScore.postValue(false)
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


    private fun startGame(){
        Log.d("Game status", "Game started")
        gameState.postValue(1)
        startTime = System.currentTimeMillis()
        startTimer()
    }

    private fun restartGame(){
        // if any successful pattern, save
        if (lengthToBeShown == lengthAtTheBeginning) {
            gameState.postValue(4)
            lengthToBeShown = lengthAtTheBeginning
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
    private fun codeToInter(){
        gameState.postValue(2)
        startTime = System.currentTimeMillis()
    }

    // transition from state 2 to 3
    private fun interToInput(){
        stopTimer()
        gameState.postValue(3)
    }

    // transition from 3 to 4
    private fun inputToWrong(){
        gameState.postValue(4)
        startTime = System.currentTimeMillis()
    }

    // transition from 3 to 5
    private fun inputToCorrect(){
        gameState.postValue(5)
        startTime = System.currentTimeMillis()
    }



    private fun randomizeCode(): Int{
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
            g1Dao.insert(G1DBEntry(102, lengthToBeShown - 1))
        }
    }

}