package com.narrowstudio.bigbrainz.ui.tasks.game2

import android.os.Bundle
import android.util.TypedValue
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
import com.narrowstudio.bigbrainz.databinding.FragmentGame2L2Binding
import com.narrowstudio.bigbrainz.viewmodel.game2.Game2l2ViewModel
import com.narrowstudio.bigbrainz.viewmodel.TimerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Game2l2Fragment : Fragment(R.layout.fragment_game_2_l_2), LifecycleOwner {

    private val g2l2ViewModel : Game2l2ViewModel by viewModels()
    private lateinit var gameState: LiveData<Int>

    // opening score
    private lateinit var openScore: LiveData<Boolean>

    var navController : NavController? = null

    // View Binding
    private var _binding: FragmentGame2L2Binding? = null
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
        _binding = FragmentGame2L2Binding.inflate(inflater, container, false)
        val view = binding.root


        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)


        g2l2ViewModel.init()
        //passing color array to VM
        g2l2ViewModel.colorList = requireContext().resources.getIntArray(R.array.g2l2)




        //updating button color
        gameState = g2l2ViewModel.gameState
        gameState.observe(viewLifecycleOwner, Observer {
            updateButtonColor()
            updateButtonText()
        })

        g2l2ViewModel.shouldGameBeRestarted.observe(viewLifecycleOwner, Observer {
            restartGame()
        })



        //opening score fragment
        openScore = g2l2ViewModel.openScore
        openScore.observe(viewLifecycleOwner, Observer{
            openScoreFragment()
        })


        g2l2ViewModel.currentButtonColor.observe(viewLifecycleOwner, Observer {
            if (g2l2ViewModel.gameState.value == 1){
                binding.g2Button.setBackgroundColor(g2l2ViewModel.currentButtonColor.value!!)
            }
        })


        /*g2_button.setOnClickListener {
            g2ViewModel.buttonPressed()
        }*/


        binding.g2Button.setOnTouchListener(View.OnTouchListener{v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    g2l2ViewModel.buttonPressed()
                    //binding.g2Button.performClick()
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
                requireActivity().theme.resolveAttribute(com.google.android.material.R.attr.backgroundColor, typedValue, true)
                if (typedValue.resourceId != 0) {
                    binding.g2Button.setBackgroundResource(typedValue.resourceId)
                } else {
                    // this should work whether there was a resource id or not
                    binding.g2Button.setBackgroundResource(typedValue.data)
                }
                binding.g2Button.setImageResource(R.drawable.ic_baseline_touch)
            }
            1 -> {
                binding.g2Button.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.colorButtonNotReady))
                binding.g2Button.setImageDrawable(null)
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
                val typedValue = TypedValue()
                requireActivity().theme.resolveAttribute(com.google.android.material.R.attr.backgroundColor, typedValue, true)
                if (typedValue.resourceId != 0) {
                    binding.g2Button.setBackgroundResource(typedValue.resourceId)
                } else {
                    // this should work whether there was a resource id or not
                    binding.g2Button.setBackgroundResource(typedValue.data)
                }
                binding.g2Button.setImageDrawable(null)
            }
        }
        binding.topTextView.text = getString(R.string.g2l1_remaining, g2l2ViewModel.remainingMeasurements)
        binding.bottomTextView.text = getString(R.string.g2l2_info)
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
            //Game2ScoreFragment().show((activity as AppCompatActivity).supportFragmentManager, "Score")
            navController!!.navigate(R.id.action_game2l2Fragment_to_game2l2ScoreFragment)
        }
    }


}