package com.narrowstudio.bigbrainz.ui.tasks.game3

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.narrowstudio.bigbrainz.R
import com.narrowstudio.bigbrainz.databinding.FragmentGame3L1Binding
import com.narrowstudio.bigbrainz.databinding.FragmentGame3L2Binding
import com.narrowstudio.bigbrainz.viewmodel.game3.Game3L1ViewModel
import com.narrowstudio.bigbrainz.viewmodel.game3.Game3L2ViewModel
import dagger.hilt.android.AndroidEntryPoint

// suppressing performClick warning
@SuppressLint("ClickableViewAccessibility")
@AndroidEntryPoint
class Game3L2Fragment : Fragment(R.layout.fragment_game_3_l_2), LifecycleOwner{

    private val g3L2ViewModel: Game3L2ViewModel by viewModels()

    var navController : NavController? = null

    // View Binding
    private var _binding: FragmentGame3L2Binding? = null
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
    ): View {

        _binding = FragmentGame3L2Binding.inflate(inflater, container,false)
        val view = binding.root

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        g3L2ViewModel.init()

        g3L2ViewModel.gameState.observe(viewLifecycleOwner, Observer {
            layoutVisibilityHandler()
            targetPositioning()
            targetVisibilityHandler()
            middleTextHandler()
            topTextHandler()
            bottomTextHandler()
        })

        g3L2ViewModel.openScore.observe(viewLifecycleOwner, Observer {
            openScoreFragment()
        })


        binding.startButton.setOnClickListener(View.OnClickListener {
            startButtonClicked()
        })

        binding.gameTarget.setOnTouchListener(View.OnTouchListener{v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    targetClicked()
                }
            }
            true
        })

        binding.gameFakeTarget.setOnTouchListener(View.OnTouchListener{v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    fakeTargetClicked()
                }
            }
            true
        })

        binding.gameLayout.setOnTouchListener(View.OnTouchListener{v, event ->
            when(event.action){
            MotionEvent.ACTION_DOWN -> {
                gameLayoutClicked()
            }
        }
            true
        })


        return view
    }

    private fun startButtonClicked(){
        g3L2ViewModel.startButtonClicked()
    }

    private fun targetClicked(){
        g3L2ViewModel.targetClicked()
    }

    private fun fakeTargetClicked(){
        g3L2ViewModel.fakeTargetClicked()
    }

    private fun gameLayoutClicked(){
        g3L2ViewModel.gameLayoutClicked()
    }


    private fun targetVisibilityHandler(){
        when (g3L2ViewModel.gameState.value){
            1 -> {
                binding.gameTarget.visibility = View.VISIBLE
                binding.gameFakeTarget.visibility = View.INVISIBLE
            }
            11 -> {
                binding.gameTarget.visibility = View.INVISIBLE
                binding.gameFakeTarget.visibility = View.VISIBLE
            }
            else -> {
                binding.gameTarget.visibility = View.INVISIBLE
                binding.gameFakeTarget.visibility = View.INVISIBLE
            }
        }
    }

    private fun targetPositioning(){
        when (g3L2ViewModel.gameState.value){
            1, 11 -> {
                binding.targetGlHorizontal.setGuidelinePercent(g3L2ViewModel.targetPosition[0])
                binding.targetGlVertical.setGuidelinePercent(g3L2ViewModel.targetPosition[1])
            }
        }
    }

    private fun layoutVisibilityHandler(){
        when (g3L2ViewModel.gameState.value){
            0 -> {
                binding.startLayout.visibility = View.VISIBLE
                binding.gameLayout.visibility = View.INVISIBLE
                binding.middleTextLayout.visibility = View.INVISIBLE
            }
            1, 11 -> {
                binding.startLayout.visibility = View.INVISIBLE
                binding.gameLayout.visibility = View.VISIBLE
                binding.middleTextLayout.visibility = View.INVISIBLE
            }
            10 -> {
                binding.startLayout.visibility = View.INVISIBLE
                binding.gameLayout.visibility = View.VISIBLE
                binding.middleTextLayout.visibility = View.INVISIBLE
            }
            2 -> {
                binding.startLayout.visibility = View.INVISIBLE
                binding.gameLayout.visibility = View.INVISIBLE
                binding.middleTextLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun middleTextHandler(){
        when (g3L2ViewModel.gameState.value ){
            2 -> binding.middleText.text = getString(R.string.g3l2_incorrect)
            else -> binding.middleText.text = ""
        }
    }

    private fun topTextHandler(){
        binding.topTextView.text = getString(R.string.g3l2_remaining,
            g3L2ViewModel.remaining)
    }

    private fun bottomTextHandler(){
        binding.bottomTextView.text = getString(R.string.g3l2_info)
    }

    private fun openScoreFragment(){
        if (g3L2ViewModel.openScore.value == true) {
            navController!!.navigate(R.id.action_game3L1Fragment_to_game3L2ScoreFragment)
        }
    }



}