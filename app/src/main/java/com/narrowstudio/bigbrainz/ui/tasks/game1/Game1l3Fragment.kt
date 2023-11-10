package com.narrowstudio.bigbrainz.ui.tasks.game1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.narrowstudio.bigbrainz.R
import com.narrowstudio.bigbrainz.databinding.FragmentGame1L3Binding
import com.narrowstudio.bigbrainz.viewmodel.game1.Game1l3ViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Game1l3Fragment : Fragment(R.layout.fragment_game_1_l_3), LifecycleOwner {

    private val g1l3ViewModel: Game1l3ViewModel by viewModels()

    // opening score
    private lateinit var openScore: LiveData<Boolean>

    var navController : NavController? = null

    // View Binding
    private var _binding: FragmentGame1L3Binding? = null
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
        _binding = FragmentGame1L3Binding.inflate(inflater, container, false)
        val view = binding.root

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        g1l3ViewModel.init()

        binding.g1l3StartButton.setOnTouchListener(View.OnTouchListener{v, event ->
            g1l3ViewModel.mainButtonPressed()
            true
        })

        binding.g1l3Button1.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(1)
        })
        binding.g1l3Button2.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(2)
        })
        binding.g1l3Button3.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(3)
        })
        binding.g1l3Button4.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(4)
        })
        binding.g1l3Button5.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(5)
        })
        binding.g1l3Button6.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(6)
        })
        binding.g1l3Button7.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(7)
        })
        binding.g1l3Button8.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(8)
        })
        binding.g1l3Button9.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(9)
        })
        binding.g1l3Button10.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(10)
        })
        binding.g1l3Button11.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(11)
        })
        binding.g1l3Button12.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(12)
        })
        binding.g1l3Button13.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(13)
        })
        binding.g1l3Button14.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(14)
        })
        binding.g1l3Button15.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(15)
        })
        binding.g1l3Button16.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(16)
        })
        binding.g1l3Button17.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(17)
        })
        binding.g1l3Button18.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(18)
        })
        binding.g1l3Button19.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(19)
        })
        binding.g1l3Button20.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(20)
        })
        binding.g1l3Button21.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(21)
        })
        binding.g1l3Button22.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(22)
        })
        binding.g1l3Button23.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(23)
        })
        binding.g1l3Button24.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(24)
        })
        binding.g1l3Button25.setOnClickListener(View.OnClickListener {v ->
            buttonClicked(25)
        })




        g1l3ViewModel.gameState.observe(viewLifecycleOwner, Observer{
            buttonIconHandler()
            layoutVisibilityHandler()
            setButtons()
            setButtonsGameState3()
            topTextHandler()
            middleTextHandler()
            bottomTextHandler()
        })

        g1l3ViewModel.correctTiles.observe(viewLifecycleOwner, Observer{
            setButtonsGameState3()
        })


        //opening score fragment
        openScore = g1l3ViewModel.openScore
        openScore.observe(viewLifecycleOwner, Observer{
            openScoreFragment()
        })

        // swtting progress bar
        g1l3ViewModel.progressBar.observe(viewLifecycleOwner, Observer {
            progressBarHandler()
        })




        return view
    }

    private fun buttonClicked(id: Int){
        g1l3ViewModel.tileButtonPressed(id)
    }


    private fun topTextHandler(){
        when (g1l3ViewModel.gameState.value){
            0, 1, 2 -> {
                binding.g1l3TopTextView.text = getString(R.string.g1l3_tile_amount,
                    g1l3ViewModel.levelToBeShown)
            }
            3 -> {
                binding.g1l3TopTextView.text = getString(R.string.g1l3_tile_amount,
                    g1l3ViewModel.levelToBeShown)
            }
            else -> {
                binding.g1l3TopTextView.text = ""
            }
        }
    }

    private fun middleTextHandler(){
        when (g1l3ViewModel.gameState.value){
            4 -> {
                binding.g1l3MiddleText.text = getString(R.string.g1l3_incorrect)
            }
            5 -> {
                binding.g1l3MiddleText.text = getString(R.string.g1l3_correct)
            }
            else -> {
                binding.g1l3MiddleText.text = ""
            }

        }
    }

    private fun bottomTextHandler(){
        when (g1l3ViewModel.gameState.value){
            0, 1, 2, 3, 4, 5 -> {
                binding.g1l3BottomTextView.text = getString(R.string.g1l3_info)
            }
            else -> {
                binding.g1l3BottomTextView.text = ""
            }
        }
    }

    // setting button values for every element in indexList
    private fun setButtons(){
        when (g1l3ViewModel.gameState.value){
            1 -> {
                for (i in 1..g1l3ViewModel.indexList.size) {
                    setButtonText(g1l3ViewModel.indexList[i - 1], i)
                }
            }
            3 -> {
                setButtonsGameState3()
            }
            else -> {
                resetButtonText()
            }
        }

    }

    private fun setButtonsGameState3(){
        if (g1l3ViewModel.gameState.value != 3) return
        if (g1l3ViewModel.correctTiles.value == null) return
        for (i in 1 ..g1l3ViewModel.correctTiles.value!!){
            setButtonText(g1l3ViewModel.indexList[i - 1], i)
        }
    }

    // setting individual button #id vith predefined value
    private fun setButtonText(id: Int, value: Int){
        when (id){
            1 -> binding.g1l3Button1.text = value.toString()
            2 -> binding.g1l3Button2.text = value.toString()
            3 -> binding.g1l3Button3.text = value.toString()
            4 -> binding.g1l3Button4.text = value.toString()
            5 -> binding.g1l3Button5.text = value.toString()
            6 -> binding.g1l3Button6.text = value.toString()
            7 -> binding.g1l3Button7.text = value.toString()
            8 -> binding.g1l3Button8.text = value.toString()
            9 -> binding.g1l3Button9.text = value.toString()
            10 -> binding.g1l3Button10.text = value.toString()
            11 -> binding.g1l3Button11.text = value.toString()
            12 -> binding.g1l3Button12.text = value.toString()
            13 -> binding.g1l3Button13.text = value.toString()
            14 -> binding.g1l3Button14.text = value.toString()
            15 -> binding.g1l3Button15.text = value.toString()
            16 -> binding.g1l3Button16.text = value.toString()
            17 -> binding.g1l3Button17.text = value.toString()
            18 -> binding.g1l3Button18.text = value.toString()
            19 -> binding.g1l3Button19.text = value.toString()
            20 -> binding.g1l3Button20.text = value.toString()
            21 -> binding.g1l3Button21.text = value.toString()
            22 -> binding.g1l3Button22.text = value.toString()
            23 -> binding.g1l3Button23.text = value.toString()
            24 -> binding.g1l3Button24.text = value.toString()
            25 -> binding.g1l3Button25.text = value.toString()
        }
    }

    private fun resetButtonText(){
        binding.g1l3Button1.text = ""
        binding.g1l3Button2.text = ""
        binding.g1l3Button3.text = ""
        binding.g1l3Button4.text = ""
        binding.g1l3Button5.text = ""
        binding.g1l3Button6.text = ""
        binding.g1l3Button7.text = ""
        binding.g1l3Button8.text = ""
        binding.g1l3Button9.text = ""
        binding.g1l3Button10.text = ""
        binding.g1l3Button11.text = ""
        binding.g1l3Button12.text = ""
        binding.g1l3Button13.text = ""
        binding.g1l3Button14.text = ""
        binding.g1l3Button15.text = ""
        binding.g1l3Button16.text = ""
        binding.g1l3Button17.text = ""
        binding.g1l3Button18.text = ""
        binding.g1l3Button19.text = ""
        binding.g1l3Button20.text = ""
        binding.g1l3Button21.text = ""
        binding.g1l3Button22.text = ""
        binding.g1l3Button23.text = ""
        binding.g1l3Button24.text = ""
        binding.g1l3Button25.text = ""
    }



    private fun buttonIconHandler(){
        when (g1l3ViewModel.gameState.value){
            0 -> {
                binding.g1l3StartButton.setImageResource(R.drawable.ic_baseline_touch)
            }
            else -> {
                binding.g1l3StartButton.setImageDrawable(null)
            }
        }
    }

    private fun progressBarHandler(){
        binding.g1l3ProgressBar.progress = g1l3ViewModel.progressBar.value!!
    }


    private fun layoutVisibilityHandler(){
        when (g1l3ViewModel.gameState.value){
            0 -> {
                binding.g1l3StartLayout.visibility = View.VISIBLE
                binding.g1l3GridLayout.visibility = View.INVISIBLE
                binding.g1l3ProgressLayout.visibility = View.INVISIBLE
                binding.g1l3TextLayout.visibility = View.INVISIBLE
            }
            1 -> {
                binding.g1l3StartLayout.visibility = View.INVISIBLE
                binding.g1l3GridLayout.visibility = View.VISIBLE
                binding.g1l3ProgressLayout.visibility = View.INVISIBLE
                binding.g1l3TextLayout.visibility = View.INVISIBLE
            }
            2 -> {
                binding.g1l3StartLayout.visibility = View.INVISIBLE
                binding.g1l3GridLayout.visibility = View.INVISIBLE
                binding.g1l3ProgressLayout.visibility = View.VISIBLE
                binding.g1l3TextLayout.visibility = View.INVISIBLE
            }
            3 -> {
                binding.g1l3StartLayout.visibility = View.INVISIBLE
                binding.g1l3GridLayout.visibility = View.VISIBLE
                binding.g1l3ProgressLayout.visibility = View.INVISIBLE
                binding.g1l3TextLayout.visibility = View.INVISIBLE
            }
            4 -> {
                binding.g1l3StartLayout.visibility = View.INVISIBLE
                binding.g1l3GridLayout.visibility = View.INVISIBLE
                binding.g1l3ProgressLayout.visibility = View.INVISIBLE
                binding.g1l3TextLayout.visibility = View.VISIBLE
            }
            5 -> {
                binding.g1l3StartLayout.visibility = View.INVISIBLE
                binding.g1l3GridLayout.visibility = View.INVISIBLE
                binding.g1l3ProgressLayout.visibility = View.INVISIBLE
                binding.g1l3TextLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun openScoreFragment(){
        if (openScore.value == true) {
            navController!!.navigate(R.id.action_game1l3Fragment_to_game1l3ScoreFragment)
        }
    }



}