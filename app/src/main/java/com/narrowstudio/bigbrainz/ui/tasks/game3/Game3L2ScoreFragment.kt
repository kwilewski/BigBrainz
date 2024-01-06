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
import com.narrowstudio.bigbrainz.databinding.FragmentGame3L1ScoreBinding
import com.narrowstudio.bigbrainz.databinding.FragmentGame3L2ScoreBinding
import com.narrowstudio.bigbrainz.viewmodel.game2.Game2l3ScoreViewModel
import com.narrowstudio.bigbrainz.viewmodel.game3.Game3L2ScoreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Game3L2ScoreFragment : Fragment(R.layout.fragment_game_3_l_2_score), LifecycleOwner {

    private val g3L2scoreViewModel : Game3L2ScoreViewModel by viewModels()
    var navController : NavController? = null


    // View Binding
    private var _binding: FragmentGame3L2ScoreBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGame3L2ScoreBinding.inflate(inflater, container, false)
        val view = binding.root

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)



        g3L2scoreViewModel.saves.observe(viewLifecycleOwner){
            // opening database
            setScores()
        }

        g3L2scoreViewModel.init()
        setScores()

        // home button
        binding.buttonHome.setOnClickListener(View.OnClickListener { v ->
            navController!!.navigate(R.id.action_game3L2ScoreFragment_to_mainScreenFragment)
        })

        // repeat button
        binding.buttonRepeat.setOnClickListener(View.OnClickListener { v ->
            navController!!.navigate(R.id.action_game3L2ScoreFragment_to_game3L2Fragment)
        })



        return view
    }


    private fun setScores(){
        g3L2scoreViewModel.getTimesFromDB()
        binding.scoreText.text = getString(R.string.score_sec, g3L2scoreViewModel.lastTimeString)
        binding.bestScoreText.text = getString(R.string.best_score_sec, g3L2scoreViewModel.bestTimeString)
        binding.averageScoreText.text = getString(R.string.average_score_sec, g3L2scoreViewModel.averageTimeString)

    }





}