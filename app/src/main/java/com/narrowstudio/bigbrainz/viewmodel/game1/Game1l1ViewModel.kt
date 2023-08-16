package com.narrowstudio.bigbrainz.viewmodel.game1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.narrowstudio.bigbrainz.data.G1DBEntry
import com.narrowstudio.bigbrainz.data.G1Dao
import com.narrowstudio.bigbrainz.data.G2DBEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class Game1l1ViewModel @Inject constructor(
    private val g1Dao: G1Dao
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.Main)

    /**    ---------------------- gameState ----------------------------------
     *      0 - not running
     *      1 - displaying the color
     *      2 - displaying intercolor
     *      3 - reading the input
     */
    val gameState: MutableLiveData<Int> = MutableLiveData()

    // time of starting the timer in millis
    private var startTime: Long = 0

    // list of colors received from fragment
    lateinit var colorList: ArrayList<Int>

    // array with indexes of generated colors
    val colorArray: MutableLiveData<ArrayList<Int>> = MutableLiveData()

    val saves = g1Dao.getEntries().asLiveData()




    fun init(){
        gameState.postValue(0)
    }



    //-------------------------------------------------- DB
    private fun insertNewEntry() {
        scope.launch {
            g1Dao.insert(G1DBEntry(101, 0L))
        }
    }

}