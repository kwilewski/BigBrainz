package com.narrowstudio.bigbrainz.ui.tasks.game1

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.narrowstudio.bigbrainz.R
import com.narrowstudio.bigbrainz.databinding.FragmentGame1L1Binding
import com.narrowstudio.bigbrainz.viewmodel.game1.Game1l1ViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

        binding.g1DisplayButton.setOnTouchListener(View.OnTouchListener{v, event ->
            g1l1ViewModel.mainButtonPressed()
            true
        })

        g1l1ViewModel.gameState.observe(viewLifecycleOwner, Observer{
            buttonColorHandler()
            buttonIconHandler()
            buttonVisibilityHandler()
        })

        binding.g1Color1Button.setOnClickListener(View.OnClickListener {v ->
            g1l1ViewModel.colorButtonPressed(0)
        })
        binding.g1Color2Button.setOnClickListener(View.OnClickListener {v ->
            g1l1ViewModel.colorButtonPressed(1)
        })
        binding.g1Color3Button.setOnClickListener(View.OnClickListener {v ->
            g1l1ViewModel.colorButtonPressed(2)
        })
        binding.g1Color4Button.setOnClickListener(View.OnClickListener {v ->
            g1l1ViewModel.colorButtonPressed(3)
        })


        return view
    }


    private fun buttonColorHandler(){
        when (g1l1ViewModel.buttonColor){
            0 -> {
                binding.g1DisplayButton.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.g1Red))
            }
            1 -> {
                binding.g1DisplayButton.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.g1Green))
            }
            2 -> {
                binding.g1DisplayButton.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.g1Blue))
            }
            3 -> {
                binding.g1DisplayButton.setBackgroundColor(ContextCompat.getColor(this.requireContext(), R.color.g1Yellow))
            }
            4 -> {
                val typedValue = TypedValue()
                requireActivity().theme.resolveAttribute(R.attr.colorButtonWaiting, typedValue, true)
                if (typedValue.resourceId != 0) {
                    binding.g1DisplayButton.setBackgroundResource(typedValue.resourceId)
                } else {
                    // this should work whether there was a resource id or not
                    binding.g1DisplayButton.setBackgroundResource(typedValue.data)
                }
            }
        }
    }

    private fun buttonIconHandler(){
        when (g1l1ViewModel.gameState.value){
            0 -> {
                binding.g1DisplayButton.setImageResource(R.drawable.ic_baseline_touch)
            }
            else -> {
                binding.g1DisplayButton.setImageDrawable(null)
            }
        }
    }

    private fun buttonVisibilityHandler(){
        when (g1l1ViewModel.gameState.value){
            0, 1, 2, 4 -> {
                binding.g1ColorDisplay.visibility = View.VISIBLE
                binding.g1ButtonsGrid.visibility = View.INVISIBLE
            }
            3 -> {
                binding.g1ColorDisplay.visibility = View.INVISIBLE
                binding.g1ButtonsGrid.visibility = View.VISIBLE
            }
        }
    }



}