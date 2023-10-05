package com.narrowstudio.bigbrainz.ui.tasks.game2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.narrowstudio.bigbrainz.viewmodel.game2.Game2ScoreViewModel
import com.narrowstudio.bigbrainz.R
import com.narrowstudio.bigbrainz.databinding.FragmentGame2ScoreBinding
import com.narrowstudio.bigbrainz.viewmodel.game2.Game2l2ScoreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Game2l2ScoreFragment : Fragment(R.layout.fragment_game_2_l_2_score), LifecycleOwner {

    private val g2l2scoreViewModel : Game2l2ScoreViewModel by viewModels()
    var navController : NavController? = null


    // View Binding
    private var _binding: FragmentGame2ScoreBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: Game2ScoreViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGame2ScoreBinding.inflate(inflater, container, false)
        val view = binding.root

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)






        g2l2scoreViewModel.saves.observe(viewLifecycleOwner){
            // opening database
            setScores()
        }

        g2l2scoreViewModel.init()
        setScores()

        // home button
        binding.g2scoreButtonHome.setOnClickListener(View.OnClickListener { view ->
            navController!!.navigate(R.id.action_game2l2ScoreFragment_to_mainScreenFragment)
        })

        // repeat button
        binding.g2scoreButtonRepeat.setOnClickListener(View.OnClickListener { view ->
            navController!!.navigate(R.id.action_game2l2ScoreFragment_to_game2l2Fragment)
        })

        /*requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    //navController!!.navigate(R.id.action_game2ScoreFragment_to_game2Fragment)
                    findNavController().navigate(R.id.action_game2ScoreFragment_to_game2Fragment)
                }
            }
        )*/



        return view
    }





    private fun setScores(){
        g2l2scoreViewModel.getTimesFromDB()
        binding.g2scoreScoreText.text = getString(R.string.score_sec, g2l2scoreViewModel.lastTimeString)
        binding.g2scoreBestScoreText.text = getString(R.string.best_score_sec, g2l2scoreViewModel.bestTimeString)
        binding.g2scoreAverageScoreText.text = getString(R.string.average_score_sec, g2l2scoreViewModel.averageTimeString)

    }





}