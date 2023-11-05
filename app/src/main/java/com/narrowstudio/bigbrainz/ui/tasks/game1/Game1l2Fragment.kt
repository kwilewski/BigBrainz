package com.narrowstudio.bigbrainz.ui.tasks.game1

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.narrowstudio.bigbrainz.R
import com.narrowstudio.bigbrainz.databinding.FragmentGame1L2Binding
import com.narrowstudio.bigbrainz.viewmodel.game1.Game1l2ViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Game1l2Fragment : Fragment(R.layout.fragment_game_1_l_2), LifecycleOwner {

    private val g1l2ViewModel: Game1l2ViewModel by viewModels()

    // opening score
    private lateinit var openScore: LiveData<Boolean>

    var navController : NavController? = null

    // View Binding
    private var _binding: FragmentGame1L2Binding? = null
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
        _binding = FragmentGame1L2Binding.inflate(inflater, container, false)
        val view = binding.root

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        g1l2ViewModel.init()

        binding.g1l2StartButton.setOnTouchListener(View.OnTouchListener{v, event ->
            g1l2ViewModel.mainButtonPressed()
            true
        })


        g1l2ViewModel.gameState.observe(viewLifecycleOwner, Observer{
            buttonIconHandler()
            layoutVisibilityHandler()
            topTextHandler()
            middleTextHandler()
            bottomTextHandler()
        })


        //opening score fragment
        openScore = g1l2ViewModel.openScore
        openScore.observe(viewLifecycleOwner, Observer{
            openScoreFragment()
        })

        binding.g1l2SequenceButton.setOnClickListener(View.OnClickListener {v ->
            sequenceButtonClicked()
        })



        return view
    }

    private fun sequenceButtonClicked(){
        // readind the code from the EditText
        val code: Long? = binding.g1l2SequenceInput.text.toString().toLongOrNull()
        if (code != null) {
            g1l2ViewModel.codeEntered(code)
        }
        // removing the code from ET after the click
        binding.g1l2SequenceInput.setText("")

        // hiding the keyboard after the click
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun topTextHandler(){
        when (g1l2ViewModel.gameState.value){
            0, 1, 2 -> {
                binding.g1l2TopTextView.text = getString(R.string.g1l2_code_length,
                    g1l2ViewModel.lengthToBeShown)
            }
            3 -> {
                binding.g1l2TopTextView.text = getString(R.string.g1l2_code_length,
                    g1l2ViewModel.lengthToBeShown)
            }
            else -> {
                binding.g1l2TopTextView.text = ""
            }
        }
    }

    private fun middleTextHandler(){
        when (g1l2ViewModel.gameState.value){
            1 -> {
                binding.g1l2SequenceText.text = g1l2ViewModel.code.value.toString()
            }
            2 -> {
                binding.g1l2SequenceText.text = ""
            }
            4 -> {
                binding.g1l2SequenceText.text = getString(R.string.g1l2_incorrect, g1l2ViewModel.code.value)
            }
            5 -> {
                binding.g1l2SequenceText.text = getString(R.string.g1l2_correct)
            }
            else -> {
                binding.g1l2SequenceText.text = ""
            }

        }
    }

    private fun bottomTextHandler(){
        when (g1l2ViewModel.gameState.value){
            0, 1, 2, 3, 4, 5 -> {
                binding.g1l2BottomTextView.text = getString(R.string.g1l2_info)
            }
            else -> {
                binding.g1l2BottomTextView.text = ""
            }
        }
    }


    private fun buttonIconHandler(){
        when (g1l2ViewModel.gameState.value){
            0 -> {
                binding.g1l2StartButton.setImageResource(R.drawable.ic_baseline_touch)
            }
            else -> {
                binding.g1l2StartButton.setImageDrawable(null)
            }
        }
    }


    private fun layoutVisibilityHandler(){
        when (g1l2ViewModel.gameState.value){
            0 -> {
                binding.g1l2StartLayout.visibility = View.VISIBLE
                binding.g1l2SequenceLayout.visibility = View.INVISIBLE
                binding.g1l2InputLayout.visibility = View.INVISIBLE
            }
            1 -> {
                binding.g1l2StartLayout.visibility = View.INVISIBLE
                binding.g1l2SequenceLayout.visibility = View.VISIBLE
                binding.g1l2InputLayout.visibility = View.INVISIBLE
            }
            2 -> {
                binding.g1l2StartLayout.visibility = View.INVISIBLE
                binding.g1l2SequenceLayout.visibility = View.INVISIBLE
                binding.g1l2InputLayout.visibility = View.INVISIBLE
            }
            3 -> {
                binding.g1l2StartLayout.visibility = View.INVISIBLE
                binding.g1l2SequenceLayout.visibility = View.INVISIBLE
                binding.g1l2InputLayout.visibility = View.VISIBLE
            }
            4 -> {
                binding.g1l2StartLayout.visibility = View.INVISIBLE
                binding.g1l2SequenceLayout.visibility = View.VISIBLE
                binding.g1l2InputLayout.visibility = View.INVISIBLE
            }
            5 -> {
                binding.g1l2StartLayout.visibility = View.INVISIBLE
                binding.g1l2SequenceLayout.visibility = View.VISIBLE
                binding.g1l2InputLayout.visibility = View.INVISIBLE
            }
        }
    }

    private fun openScoreFragment(){
        if (openScore.value == true) {
            navController!!.navigate(R.id.action_game1l2Fragment_to_game1l2ScoreFragment)
        }
    }



}