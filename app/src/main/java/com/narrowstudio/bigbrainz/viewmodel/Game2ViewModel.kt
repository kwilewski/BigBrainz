package com.narrowstudio.bigbrainz.viewmodel

import androidx.lifecycle.ViewModel
import java.util.*
import java.util.logging.Handler

class Game2ViewModel : ViewModel() {

    private var isGameRunning : Boolean = false;
    private var timer = Timer()

    public fun init(){
        isGameRunning = false
    }

    public fun buttonPressed(){
        if (isGameRunning){ //handle click when game is running

        } else {    //start the game - timer
            isGameRunning = true

        }
    }


}