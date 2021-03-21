package com.narrowstudio.bigbrainz.ui.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.narrowstudio.bigbrainz.R
import com.narrowstudio.bigbrainz.viewmodel.Game2ViewModel
import com.narrowstudio.bigbrainz.viewmodel.TimerViewModel
import kotlinx.android.synthetic.main.fragment_game_2.*


class Game2Fragment : Fragment(R.layout.fragment_game_2), LifecycleOwner {

    private lateinit var g2ViewModel : Game2ViewModel
    private lateinit var timerViewModel: TimerViewModel
    private lateinit var timeInMillisLD: MutableLiveData<Long>
    private lateinit var gameState: LiveData<Int>
    private lateinit var time: LiveData<Long>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        g2ViewModel = ViewModelProvider(this).get(com.narrowstudio.bigbrainz.viewmodel.Game2ViewModel::class.java)
        g2ViewModel.init()
        g2ViewModel.getTimeInMillisLD().observe(viewLifecycleOwner, Observer {
            g2_textview.text = g2ViewModel.getTimeAsString()
        })

        timerViewModel = ViewModelProvider(this).get(TimerViewModel::class.java)
        timerViewModel.init()

        //updating timer text
        time = g2ViewModel.getTime()
        time.observe(viewLifecycleOwner, Observer {
            updateTimer()
        } )

        //updating button color
        gameState = g2ViewModel.getGameState()
        gameState.observe(viewLifecycleOwner, Observer {
            updateButtonColor()
        })

        g2ViewModel.getAverageTime().observe(viewLifecycleOwner, Observer {
            gameFinished()
        })
        g2ViewModel.getShouldGameBeRestarted().observe(viewLifecycleOwner, Observer {
            restartGame()
        })


        /*g2_button.setOnClickListener {
            g2ViewModel.buttonPressed()
        }*/


        g2_button.setOnTouchListener(View.OnTouchListener{v, event ->
            val action = event.action
            when(action){
                MotionEvent.ACTION_DOWN -> {
                    g2ViewModel.buttonPressed()}
            }
            true
        })


    }

    private fun updateTimer() {
        g2_textview.text = g2ViewModel.getTimeAsString()
    }

    private fun updateButtonColor(){
        when(gameState.value){
            0 -> g2_button.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.colorButtonWaiting))
            2 -> g2_button.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.colorButtonReady))
            else -> g2_button.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.colorButtonNotReady))
        }
    }

    private fun gameFinished(){
        g2_textview.text = "Your average time is: " + g2ViewModel.getAverageTimeAsString()
    }

    private fun restartGame(){
        if(g2ViewModel.getShouldGameBeRestarted().value == true) {
            g2_textview.text = getString(R.string.game_over)
        } else {
            g2_textview.text = ""
        }
    }


}