package com.narrowstudio.bigbrainz.ui.tasks.game3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.narrowstudio.bigbrainz.R
import com.narrowstudio.bigbrainz.databinding.FragmentGame3L1Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Game3L1Fragment : Fragment(R.layout.fragment_game_3_l_1), LifecycleOwner{

    var navController : NavController? = null

    // View Binding
    private var _binding: FragmentGame3L1Binding? = null
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
    ): View? {

        _binding = FragmentGame3L1Binding.inflate(inflater, container,false)
        val view = binding.root

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)






        return view
    }



}