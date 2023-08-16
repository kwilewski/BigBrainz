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
import com.narrowstudio.bigbrainz.databinding.FragmentGame1L1Binding
import com.narrowstudio.bigbrainz.viewmodel.game1.Game1l1ViewModel

class Game1l1Fragment : Fragment(R.layout.fragment_game_1_l_1), LifecycleOwner {

    private val g1l1ViewModel:  Game1l1ViewModel by viewModels()

    var navController : NavController? = null

    // View Binding
    private var _binding: FragmentGame1L1Binding? = null
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
        _binding = FragmentGame1L1Binding.inflate(inflater, container, false)
        val view = binding.root

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        g1l1ViewModel.init()


        return view
    }

}