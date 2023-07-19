package com.narrowstudio.bigbrainz.ui.tasks

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.narrowstudio.bigbrainz.R
import com.narrowstudio.bigbrainz.databinding.FragmentGame2Binding
import com.narrowstudio.bigbrainz.databinding.FragmentGame2L3Binding
import com.narrowstudio.bigbrainz.viewmodel.Game2l2ViewModel
import com.narrowstudio.bigbrainz.viewmodel.Game2l3ViewModel
import com.narrowstudio.bigbrainz.viewmodel.TimerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Game2l3Fragment : Fragment(R.layout.fragment_game_2_l_3), LifecycleOwner {

    private val g2l3ViewModel : Game2l3ViewModel by viewModels()
    private lateinit var timerViewModel: TimerViewModel
    private lateinit var gameState: LiveData<Int>
    private lateinit var time: LiveData<Long>

    // opening score
    private lateinit var openScore: LiveData<Boolean>

    var navController : NavController? = null

    // View Binding
    private var _binding: FragmentGame2L3Binding? = null
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
        _binding = FragmentGame2L3Binding.inflate(inflater, container, false)
        val view = binding.root





        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)


        g2l3ViewModel.init()
        //passing color array to VM
        g2l3ViewModel.colorList = requireContext().resources.getIntArray(R.array.g2l2)
        //passing color names to VM
        g2l3ViewModel.colorNameList = requireContext().resources.getStringArray(R.array.g2l3_color_names)

        g2l3ViewModel.getTimeInMillisLD().observe(viewLifecycleOwner, Observer {

        })

        timerViewModel = ViewModelProvider(this)[TimerViewModel::class.java]
        timerViewModel.init()

        //updating timer text
        time = g2l3ViewModel.millisecondLD
        time.observe(viewLifecycleOwner, Observer {

        } )

        //updating button color
        gameState = g2l3ViewModel.gameState
        gameState.observe(viewLifecycleOwner, Observer {
            updateButtonColor()
        })

        g2l3ViewModel.averageTime.observe(viewLifecycleOwner, Observer {

        })
        g2l3ViewModel.shouldGameBeRestarted.observe(viewLifecycleOwner, Observer {
            restartGame()
        })


        //saves
        g2l3ViewModel.saves.observe(viewLifecycleOwner) {

        }

        //opening score fragment
        openScore = g2l3ViewModel.openScore
        openScore.observe(viewLifecycleOwner, Observer{
            openScoreFragment()
        })


        g2l3ViewModel.currentButtonColor.observe(viewLifecycleOwner, Observer {
            updateButtonColor()
        })

        g2l3ViewModel.currentColorName.observe(viewLifecycleOwner, Observer{
            updateButtonText()
        })


        g2l3ViewModel.buttonImageStatus.observe(viewLifecycleOwner, Observer {
            updateButtonIcon()
        })

        g2l3ViewModel.buttonTextStatus.observe(viewLifecycleOwner, Observer{
            updateButtonText()
        })

        binding.g2Button.setOnTouchListener(View.OnTouchListener{v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    g2l3ViewModel.buttonPressed()
                    //binding.g2Button.performClick()
                }
            }
            true
        })




        return view
    }


    private fun updateButtonColor(){
        when(gameState.value){
            0 -> binding.g2Button.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.colorButtonWaiting))
            3 -> binding.g2Button.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.colorButtonNotReady))
            else -> {
                binding.g2Button.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.colorButtonWaiting))
                binding.g2l3ColorLabel.setTextColor(g2l3ViewModel.currentButtonColor.value!!)
            }
        }
        binding.g2Textview.text = getString(R.string.g2l3_info, g2l3ViewModel.remainingMeasurements)
    }

    private fun updateButtonIcon(){
        if(g2l3ViewModel.buttonImageStatus.value!!){
            binding.g2Button.setImageResource(R.drawable.ic_baseline_touch)
            return
        }
        binding.g2Button.setImageDrawable(null)
    }

    private fun updateButtonText(){
        println("updateButtonText()")
        if(g2l3ViewModel.buttonTextStatus.value!!){
            binding.g2l3ColorLabel.text = g2l3ViewModel.currentColorName.value
            return
        }
        binding.g2l3ColorLabel.text = null
    }


    private fun restartGame(){
        if(g2l3ViewModel.shouldGameBeRestarted.value == true) {
            binding.g2Textview.text = getString(R.string.game_over)
        } else {
            binding.g2Textview.text = ""
        }
    }

    private fun openScoreFragment(){
        if (openScore.value == true) {
            //Game2ScoreFragment().show((activity as AppCompatActivity).supportFragmentManager, "Score")
            navController!!.navigate(R.id.action_game2l3Fragment_to_game2l3ScoreFragment)
        }
    }


}