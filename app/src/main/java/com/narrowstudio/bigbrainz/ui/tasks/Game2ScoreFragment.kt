package com.narrowstudio.bigbrainz.ui.tasks

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.narrowstudio.bigbrainz.viewmodel.Game2ScoreViewModel
import com.narrowstudio.bigbrainz.R
import com.narrowstudio.bigbrainz.databinding.FragmentGame2Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Game2ScoreFragment : Fragment(R.layout.fragment_game2_score) {

    // used by jetpack View Binding
    private var _binding: FragmentGame2Binding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: Game2ScoreViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game2_score, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(Game2ScoreViewModel::class.java)
    }

}