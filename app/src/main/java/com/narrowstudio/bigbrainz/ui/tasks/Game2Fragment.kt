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
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.narrowstudio.bigbrainz.R
import com.narrowstudio.bigbrainz.databinding.FragmentGame2Binding
import com.narrowstudio.bigbrainz.viewmodel.Game2ViewModel
import com.narrowstudio.bigbrainz.viewmodel.TimerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Game2Fragment : Fragment(R.layout.fragment_game_2), LifecycleOwner {

    private val g2ViewModel : Game2ViewModel by viewModels()
    private lateinit var timerViewModel: TimerViewModel
    private lateinit var gameState: LiveData<Int>
    private lateinit var time: LiveData<Long>

    // opening score
    private lateinit var openScore: LiveData<Boolean>

    var navController : NavController? = null

    // used by jetpack View Binding
    private var _binding: FragmentGame2Binding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        _binding = FragmentGame2Binding.inflate(inflater, container, false)
        val view = binding.root

        navController = Navigation.findNavController(view)

        g2ViewModel.init()
        g2ViewModel.getTimeInMillisLD().observe(viewLifecycleOwner, Observer {
            binding.g2Textview.text = g2ViewModel.getTimeAsString()
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


        //saves
        g2ViewModel.saves.observe(viewLifecycleOwner) {
            //TODO stats
            if (g2ViewModel.saves.value?.size != 0) {
                binding.g2Textview.text = "Average time: " + g2ViewModel.getTotalTimeAsString() //g2ViewModel.saves.value!![0].averageTime
            }
        }

        //opening score fragment
        openScore = g2ViewModel.openScore
        openScore.observe(viewLifecycleOwner, Observer{
            openScoreFragment()
        })


        /*g2_button.setOnClickListener {
            g2ViewModel.buttonPressed()
        }*/


        binding.g2Button.setOnTouchListener(View.OnTouchListener{v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    binding.g2Textview.text = "Average time: " + g2ViewModel.getTotalTimeAsString()
                    g2ViewModel.buttonPressed()
                    //g2_button.performClick()
                }
            }
            true
        })

        return view
    }

    private fun updateTimer() {
        binding.g2Textview.text = g2ViewModel.getTimeAsString()
    }

    private fun updateButtonColor(){
        when(gameState.value){
            0 -> binding.g2Button.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.colorButtonWaiting))
            2 -> binding.g2Button.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.colorButtonReady))
            else -> binding.g2Button.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.colorButtonNotReady))
        }
    }

    private fun gameFinished(){
        binding.g2Textview.text = "Your average time is: " + g2ViewModel.getAverageTimeAsString()
    }

    private fun restartGame(){
        if(g2ViewModel.getShouldGameBeRestarted().value == true) {
            binding.g2Textview.text = getString(R.string.game_over)
        } else {
            binding.g2Textview.text = ""
        }
    }

    private fun openScoreFragment(){
        if (openScore.value == true) {
            navController!!.navigate(R.id.action_game2Fragment_to_game2ScoreFragment)
        }
    }


}