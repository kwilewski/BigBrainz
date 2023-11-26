package com.narrowstudio.bigbrainz.viewmodel.game3

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.narrowstudio.bigbrainz.data.G1DBEntry
import com.narrowstudio.bigbrainz.data.G3DBEntry
import com.narrowstudio.bigbrainz.data.G3Dao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class Game3L1ViewModel @Inject constructor(
    private val g3Dao: G3Dao
) : ViewModel(){

    /**    ---------------------- gameState ----------------------------------
     *      0 - not running
     *      1 - displaying the game
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
    private val minWaitTime = 200

    // max time of waiting for a target in millis
    private val maxWaitTime = 1500

    // time of displaying wrong message
    private val wrongTime = 2000

    val saves = g3Dao.getEntries().asLiveData()

    private var handler = Handler(Looper.getMainLooper())

    private val scope = CoroutineScope(Dispatchers.Main)

    private var runnable: java.lang.Runnable = object: java.lang.Runnable {
        override fun run(){
            when (gameState.value){
                1 -> {
                    // TODO game running
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

    }




    private fun restartGame(){
        gameState.postValue(4)
        startTime = System.currentTimeMillis()
        startTimer()
    }


    // transition from 3 to 4
    private fun inputToWrong(){
        restartGame()
    }

    // transition from 4 to 0
    private fun wrongToStart(){
        init()
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
            g3Dao.insert(G3DBEntry(301, 2137))
        }
    }

}