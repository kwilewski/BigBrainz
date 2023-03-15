package com.narrowstudio.bigbrainz.ui.tasks

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.narrowstudio.bigbrainz.viewmodel.Game2ScoreViewModel
import com.narrowstudio.bigbrainz.R
import com.narrowstudio.bigbrainz.databinding.FragmentGame2Binding
import com.narrowstudio.bigbrainz.databinding.FragmentGame2ScoreBinding
import com.narrowstudio.bigbrainz.viewmodel.Game2ViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Game2ScoreFragment : Fragment(R.layout.fragment_game2_score), LifecycleOwner {

    private val g2scoreViewModel : Game2ScoreViewModel by viewModels()
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

        // home button
        binding.g2scoreButtonHome.setOnClickListener(View.OnClickListener { view ->
            navController!!.navigate(R.id.action_game2ScoreFragment_to_mainScreenFragment)
        })

        // repeat button
        binding.g2scoreButtonRepeat.setOnClickListener(View.OnClickListener { view ->
            navController!!.navigate(R.id.action_game2ScoreFragment_to_game2Fragment)
        })

        return inflater.inflate(R.layout.fragment_game2_score, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(Game2ScoreViewModel::class.java)
    }





}