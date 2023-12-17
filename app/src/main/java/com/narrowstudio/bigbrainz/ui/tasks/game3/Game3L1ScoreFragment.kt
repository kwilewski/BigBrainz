package com.narrowstudio.bigbrainz.ui.tasks.game3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.narrowstudio.bigbrainz.R
import com.narrowstudio.bigbrainz.databinding.FragmentGame2ScoreBinding
import com.narrowstudio.bigbrainz.viewmodel.game2.Game2l3ScoreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Game3L1ScoreFragment : Fragment(R.layout.fragment_game_2_l_3_score), LifecycleOwner {

    private val g2l3scoreViewModel : Game2l3ScoreViewModel by viewModels()
    var navController : NavController? = null


    // View Binding
    private var _binding: FragmentGame2ScoreBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: Game2l3ScoreViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGame2ScoreBinding.inflate(inflater, container, false)
        val view = binding.root

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)






        g2l3scoreViewModel.saves.observe(viewLifecycleOwner){
            // opening database
            setScores()
        }

        g2l3scoreViewModel.init()
        setScores()

        // home button
        binding.g2scoreButtonHome.setOnClickListener(View.OnClickListener { view ->
            navController!!.navigate(R.id.action_game2l3ScoreFragment_to_mainScreenFragment)
        })

        // repeat button
        binding.g2scoreButtonRepeat.setOnClickListener(View.OnClickListener { view ->
            navController!!.navigate(R.id.action_game2l3ScoreFragment_to_game2l3Fragment)
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
        g2l3scoreViewModel.getTimesFromDB()
        binding.g2scoreScoreText.text = getString(R.string.score_sec, g2l3scoreViewModel.lastTimeString)
        binding.g2scoreBestScoreText.text = getString(R.string.best_score_sec, g2l3scoreViewModel.bestTimeString)
        binding.g2scoreAverageScoreText.text = getString(R.string.average_score_sec, g2l3scoreViewModel.averageTimeString)

    }





}