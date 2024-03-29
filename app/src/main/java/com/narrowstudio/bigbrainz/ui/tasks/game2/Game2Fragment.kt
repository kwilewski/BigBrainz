package com.narrowstudio.bigbrainz.ui.tasks.game2

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.narrowstudio.bigbrainz.R
import com.narrowstudio.bigbrainz.databinding.FragmentGame2Binding
import com.narrowstudio.bigbrainz.viewmodel.TimerViewModel
import com.narrowstudio.bigbrainz.viewmodel.game2.Game2ViewModel
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

    // View Binding
    private var _binding: FragmentGame2Binding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        g2ViewModel.init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentGame2Binding.inflate(inflater, container, false)
        val view = binding.root

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)


        g2ViewModel.init()
        g2ViewModel.getTimeInMillisLD().observe(viewLifecycleOwner, Observer {

        })

        timerViewModel = ViewModelProvider(this).get(TimerViewModel::class.java)
        timerViewModel.init()

        //updating timer text
        time = g2ViewModel.getTime()
        time.observe(viewLifecycleOwner, Observer {

        } )

        //updating button color
        gameState = g2ViewModel.getGameState()
        gameState.observe(viewLifecycleOwner, Observer {
            updateButtonColor()
            updateButtonText()
        })

        g2ViewModel.getAverageTime().observe(viewLifecycleOwner, Observer {

        })
        g2ViewModel.getShouldGameBeRestarted().observe(viewLifecycleOwner, Observer {
            restartGame()
        })


        //saves
        g2ViewModel.saves.observe(viewLifecycleOwner) {

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
                    g2ViewModel.buttonPressed()
                }
            }
            true
        })




        return view
    }


    private fun updateButtonColor(){
        when(gameState.value){
            0 -> {
                val typedValue = TypedValue()
                requireActivity().theme.resolveAttribute(R.attr.colorButtonWaiting, typedValue, true)
                if (typedValue.resourceId != 0) {
                    binding.g2Button.setBackgroundResource(typedValue.resourceId)
                } else {
                    // this should work whether there was a resource id or not
                    binding.g2Button.setBackgroundResource(typedValue.data)
                }
                binding.g2Button.setImageResource(R.drawable.ic_baseline_touch)
            }
            2 -> {
                binding.g2Button.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.colorButtonReady))
                binding.g2Button.setImageDrawable(null)
            }
            3 -> {
                val typedValue = TypedValue()
                requireActivity().theme.resolveAttribute(R.attr.colorButtonWaiting, typedValue, true)
                if (typedValue.resourceId != 0) {
                    binding.g2Button.setBackgroundResource(typedValue.resourceId)
                } else {
                    // this should work whether there was a resource id or not
                    binding.g2Button.setBackgroundResource(typedValue.data)
                }
                binding.g2Button.setImageResource(R.drawable.ic_baseline_touch)
                binding.g2Button.setImageDrawable(null)
            }
            else -> {
                binding.g2Button.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.colorButtonNotReady))
                binding.g2Button.setImageDrawable(null)
            }
        }
        binding.topTextView.text = getString(R.string.g2l1_remaining, g2ViewModel.remainingMeasurements)
        binding.g2Textview.text = getString(R.string.g2l1_info)
    }

    private fun updateButtonText(){
        when(gameState.value){
            3 -> {
                binding.middleText.text = getString(R.string.g2l1_incorrect)
            }
            else -> {
                binding.middleText.text = ""
            }
        }
    }


    private fun restartGame(){

    }

    private fun openScoreFragment(){
        if (openScore.value == true) {
            navController!!.navigate(R.id.action_game2Fragment_to_game2ScoreFragment)
        }
    }


}