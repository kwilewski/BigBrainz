package com.narrowstudio.bigbrainz.viewmodel.game1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.narrowstudio.bigbrainz.data.G1Dao
import com.narrowstudio.bigbrainz.data.G2DBEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Game1l1ViewModel @Inject constructor(
    private val g1Dao: G1Dao
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.Main)

    /**    ---------------------- gameState ----------------------------------
     *      0 - not running
     *      1 - displaying the cipher
     *      2 - reading the input
     */
    val gameState: MutableLiveData<Int> = MutableLiveData()

    // time of starting the timer in millis
    private var startTime: Long = 0





    fun init(){

    }



    //-------------------------------------------------- DB
    private fun insertNewEntry() {
        scope.launch {
            //g1Dao.insert(G2DBEntry(101, averageTime.value!!))
        }
    }

}