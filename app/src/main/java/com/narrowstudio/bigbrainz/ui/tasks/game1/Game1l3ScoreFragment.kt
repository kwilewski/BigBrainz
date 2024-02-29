package com.narrowstudio.bigbrainz.ui.tasks.game1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.narrowstudio.bigbrainz.R
import com.narrowstudio.bigbrainz.databinding.FragmentGame1L1ScoreBinding
import com.narrowstudio.bigbrainz.databinding.FragmentGame1L3ScoreBinding
import com.narrowstudio.bigbrainz.viewmodel.game1.Game1l2ScoreViewModel
import com.narrowstudio.bigbrainz.viewmodel.game1.Game1l3ScoreViewModel
import com.narrowstudio.bigbrainz.viewmodel.game2.Game2ScoreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Game1l3ScoreFragment : Fragment(R.layout.fragment_game_1_l_3_score), LifecycleOwner {

    private val g1l3scoreViewModel : Game1l3ScoreViewModel by viewModels()
    var navController : NavController? = null


    // View Binding
    private var _binding: FragmentGame1L3ScoreBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: Game2ScoreViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGame1L3ScoreBinding.inflate(inflater, container, false)
        val view = binding.root

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)






        g1l3scoreViewModel.saves.observe(viewLifecycleOwner){
            // opening database
            setScores()
        }

        g1l3scoreViewModel.init()
        setScores()

        // home button
        binding.g1scoreButtonHome.setOnClickListener(View.OnClickListener { view ->
            navController!!.navigate(R.id.action_game1l3ScoreFragment_to_mainScreenFragment)
        })

        // repeat button
        binding.g1scoreButtonRepeat.setOnClickListener(View.OnClickListener { view ->
            navController!!.navigate(R.id.action_game1l3ScoreFragment_to_game1l3Fragment)
        })



        return view
    }





    private fun setScores(){
        g1l3scoreViewModel.getScoresFromDB()
        binding.g1scoreScoreText.text = getString(R.string.score, g1l3scoreViewModel.lastScore)
        binding.g1scoreBestScoreText.text = getString(R.string.best_score, g1l3scoreViewModel.bestScore)

    }
}